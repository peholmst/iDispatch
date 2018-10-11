package net.pkhapps.idispatch.web.ui.common.event;

import com.vaadin.shared.Registration;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The purpose of this class is to make it possible for UIs to react to domain events and update themselves using server
 * push. There are several reasons it is not a good idea for the UIs do directly subscribe to the domain events:
 * <ul>
 * <li>All UIs will be updated within the same security context - the one that fired the event. This is very bad, since
 * other users' UIs would be updated thinking they belong to somebody else.</li>
 * <li>There is no limit to how many domain events you can trigger every second. However, a server push operation is
 * rather expensive and should not be done more than a few number every second, max. A mechanism is needed to queue
 * events and push them all together to the UI in a controlled manner. You don't want your UIs to become clogged down
 * with handling pushes all the time.</li>
 * </ul>
 * This class takes care of both of these problems by queuing domain events before pushing them to the UIs and by using
 * a separate thread context for every UI instance (this assumes any security context is loaded from the
 * {@link UI#getCurrent() current UI} and not from the current HTTP request/session since there would not be any
 * current HTTP request). In addition, this class supports pre-filtering of events. Only events that pass the filters
 * will result in a server push.
 */
@Component
public class UIEventBroker {

    private final static Logger LOGGER = LoggerFactory.getLogger(UIEventBroker.class);
    private final Map<UI, EventConsumerCollection> eventConsumers = new WeakHashMap<>();
    private final ReadWriteLock eventConsumersLock = new ReentrantReadWriteLock();
    private final ScheduledExecutorService executorService;
    private final int notificationIntervalMs;
    private final AtomicBoolean isShuttingDown = new AtomicBoolean(false);

    UIEventBroker(@UIPushQualifier ScheduledExecutorService executorService,
                  @Value("${ui-push.queue.notification-interval-ms}") int notificationIntervalMs) {
        this.executorService = executorService;
        this.notificationIntervalMs = notificationIntervalMs;
        LOGGER.info("Using a notification interval of {} ms", notificationIntervalMs);
        scheduleNextNotification();
    }

    @PreDestroy
    void destroy() {
        isShuttingDown.set(true);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    @SuppressWarnings("unused")
    public void onAnyApplicationEvent(Object event) {
        Objects.requireNonNull(event, "event must not be null");
        LOGGER.debug("Received event {}", event);
        // Handle the event asynchronously, we don't want to delay any request threads that may have fired the event
        // in the first place.
        executorService.execute(() -> handleEvent(event));
    }

    private void handleEvent(@NonNull Object event) {
        eventConsumersLock.readLock().lock();
        LOGGER.debug("Handling event {}", event);
        try {
            eventConsumers.values().forEach(consumerCollection -> consumerCollection.queueEvent(event));
        } finally {
            eventConsumersLock.readLock().unlock();
        }
    }

    private void scheduleNextNotification() {
        if (isShuttingDown.get()) {
            LOGGER.info("UIEventBroker is shutting down, will not schedule any more notifications");
            return;
        }
        executorService.schedule(() -> {
            try {
                notifyConsumers();
            } finally {
                scheduleNextNotification();
            }
        }, notificationIntervalMs, TimeUnit.MILLISECONDS);
    }

    private void notifyConsumers() {
        eventConsumersLock.readLock().lock();
        try {
            eventConsumers.values().forEach(EventConsumerCollection::notifyConsumers);
        } finally {
            eventConsumersLock.readLock().unlock();
        }
    }

    /**
     * Registers a new event consumer.
     *
     * @param ui            the UI that owns the consumer (needed for server push).
     * @param eventType     the type of event (can be a super class for an entire hierarchy of events) that the consumer is interested in.
     * @param eventConsumer the consumer that will be called when an event arrives.
     * @param eventFilter   an optional filter used to remove events that the consumer is not interested in.
     * @return a registration handle that can be used to remove (unregister) the consumer.
     */
    @NonNull
    public <E> Registration registerConsumer(@NonNull UI ui, @NonNull Class<E> eventType,
                                             @NonNull Consumer<E> eventConsumer,
                                             @Nullable Predicate<E> eventFilter) {
        Objects.requireNonNull(ui, "ui must not be null");
        eventConsumersLock.writeLock().lock();
        LOGGER.debug("Registering consumer {} from UI {} for events of type {} using filter {}", eventConsumer, ui,
                eventType, eventFilter);
        try {
            // TODO The event consumers should be removed from the map when the UI is GC:d, but we should also register
            // a detach listener that cleans up the map when the UI is detached.
            return eventConsumers.computeIfAbsent(ui, EventConsumerCollection::new)
                    .registerConsumer(eventType, eventConsumer, eventFilter);
        } finally {
            eventConsumersLock.writeLock().unlock();
        }
    }

    /**
     * Registers a new event consumer.
     *
     * @param ui            the UI that owns the consumer (needed for server push).
     * @param eventType     the type of event (can be a super class for an entire hierarchy of events) that the consumer is interested in.
     * @param eventConsumer the consumer that will be called when an event arrives.
     * @return a registration handle that can be used to remove (unregister) the consumer.
     */
    @NonNull
    public <E> Registration registerConsumer(@NonNull UI ui, @NonNull Class<E> eventType,
                                             @NonNull Consumer<E> eventConsumer) {
        return registerConsumer(ui, eventType, eventConsumer, null);
    }

    /**
     * Registers a new event consumer for the {@link UI#getCurrent() current UI}.
     *
     * @param eventType     the type of event (can be a super class for an entire hierarchy of events) that the consumer is interested in.
     * @param eventConsumer the consumer that will be called when an event arrives.
     * @param eventFilter   an optional filter used to remove events that the consumer is not interested in.
     * @return a registration handle that can be used to remove (unregister) the consumer.
     */
    @NonNull
    public <E> Registration registerConsumer(@NonNull Class<E> eventType,
                                             @NonNull Consumer<E> eventConsumer,
                                             @Nullable Predicate<E> eventFilter) {
        return registerConsumer(UI.getCurrent(), eventType, eventConsumer, eventFilter);
    }

    /**
     * Registers a new event consumer for the {@link UI#getCurrent() current UI}.
     *
     * @param eventType     the type of event (can be a super class for an entire hierarchy of events) that the consumer is interested in.
     * @param eventConsumer the consumer that will be called when an event arrives.
     * @return a registration handle that can be used to remove (unregister) the consumer.
     */
    @NonNull
    public <E> Registration registerConsumer(@NonNull Class<E> eventType,
                                             @NonNull Consumer<E> eventConsumer) {
        return registerConsumer(eventType, eventConsumer, null);
    }


    private class EventConsumerCollection {

        private final Set<EventConsumer<?>> eventConsumers = new HashSet<>();
        private final List<Runnable> notificationQueue = new LinkedList<>();
        private final UI ui;

        EventConsumerCollection(@NonNull UI ui) {
            this.ui = Objects.requireNonNull(ui, "ui must not be null");
        }

        void queueEvent(@NonNull Object event) {
            Objects.requireNonNull(event, "event must not be null");
            synchronized (notificationQueue) {
                eventConsumers.stream()
                        .filter(consumer -> consumer.isInterestedIn(event))
                        .map(consumer -> (Runnable) () -> consumer.handleEvent(event))
                        .forEach(notificationQueue::add);
            }
        }

        void notifyConsumers() {
            synchronized (notificationQueue) {
                if (notificationQueue.isEmpty()) {
                    return;
                }
            }
            executorService.execute(() -> {
                List<Runnable> notificationQueueCopy;
                synchronized (notificationQueue) {
                    notificationQueueCopy = new LinkedList<>(notificationQueue);
                    notificationQueue.clear();
                }
                if (!notificationQueueCopy.isEmpty() && ui.isAttached()) {
                    ui.access(() -> notificationQueueCopy.forEach(Runnable::run));
                }
            });
        }

        <E> Registration registerConsumer(@NonNull Class<E> eventType, @NonNull Consumer<E> eventConsumer,
                                          @Nullable Predicate<E> eventFilter) {
            EventConsumer<E> consumer = new EventConsumer<>(eventType, eventConsumer, eventFilter);
            synchronized (eventConsumers) {
                eventConsumers.add(consumer);
            }
            return (Registration) () -> {
                synchronized (eventConsumers) {
                    eventConsumers.remove(consumer);
                }
            };
        }
    }

    private class EventConsumer<E> {
        private final Class<E> eventType;
        private final Consumer<E> eventConsumer;
        private final Predicate<E> eventFilter;

        EventConsumer(@NonNull Class<E> eventType, @NonNull Consumer<E> eventConsumer,
                      @Nullable Predicate<E> eventFilter) {
            this.eventType = Objects.requireNonNull(eventType);
            this.eventConsumer = Objects.requireNonNull(eventConsumer);
            this.eventFilter = eventFilter;
        }

        boolean isInterestedIn(@Nullable Object event) {
            if (!eventType.isInstance(event)) {
                return false;
            }
            return eventFilter == null || eventFilter.test(eventType.cast(event));
        }

        void handleEvent(@NonNull Object event) {
            eventConsumer.accept(eventType.cast(event));
        }
    }
}
