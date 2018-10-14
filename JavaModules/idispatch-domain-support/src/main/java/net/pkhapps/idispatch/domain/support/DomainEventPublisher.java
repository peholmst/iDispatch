package net.pkhapps.idispatch.domain.support;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The {@code DomainEventPublisher} is responsible for publishing {@link DomainEvent}s to registered listeners as
 * soon as the events occur.
 *
 * @see DomainEventStore
 */
@Slf4j
@ThreadSafe
public class DomainEventPublisher {

    /**
     * Predicate that accepts all domain events.
     *
     * @see #registerListener(Consumer, Predicate)
     */
    public static final Predicate<DomainEvent> ALL_EVENTS = (event) -> true;
    private final Set<ListenerFilterRecord> listeners = new HashSet<>();
    private final ReentrantReadWriteLock listenerLock = new ReentrantReadWriteLock();

    /**
     * Publishes the given {@code domainEvent} to all interested listeners.
     *
     * @param domainEvent the event to publish.
     */
    public void publish(@Nonnull DomainEvent domainEvent) {
        Objects.requireNonNull(domainEvent, "domainEvent must not be null");

        Set<ListenerFilterRecord> copyOfListeners;
        listenerLock.readLock().lock();
        try {
            copyOfListeners = new HashSet<>(listeners);
        } finally {
            listenerLock.readLock().unlock();
        }
        log.debug("Publishing event {} to listeners", domainEvent);
        copyOfListeners.stream()
                .filter(listener -> listener.test(domainEvent))
                .forEach(listener -> listener.accept(domainEvent));
    }

    /**
     * Registers a new listener to be notified of domain events. Any exceptions thrown by the listener will be
     * <em>silently ignored</em>.
     *
     * @param listener the listener to register.
     * @param filter   the predicate to use for determining what events the listener is interested in.
     * @return a registration handle that can be used to remove the listener.
     */
    @Nonnull
    public RegistrationHandle registerListener(@Nonnull Consumer<DomainEvent> listener,
                                               @Nonnull Predicate<DomainEvent> filter) {
        Objects.requireNonNull(listener, "listener must not be null");
        Objects.requireNonNull(filter, "filter must not be null");
        var record = new ListenerFilterRecord(listener, filter);
        addListener(record);
        return () -> removeListener(record);
    }

    private void addListener(@Nonnull ListenerFilterRecord record) {
        listenerLock.writeLock().lock();
        try {
            log.trace("Adding listener {}", record.listener);
            listeners.add(record);
        } finally {
            listenerLock.writeLock().unlock();
        }
    }

    private void removeListener(@Nonnull ListenerFilterRecord record) {
        listenerLock.writeLock().lock();
        try {
            log.trace("Removing listener {}", record.listener);
            listeners.remove(record);
        } finally {
            listenerLock.writeLock().unlock();
        }
    }

    /**
     * Registration handle returned by {@link #registerListener(Consumer, Predicate)} to allow clients to
     * remove the listener when no longer needed.
     */
    public interface RegistrationHandle {

        /**
         * Unregisters the listener from the {@link DomainEventPublisher}.
         */
        void unregister();
    }

    @AllArgsConstructor
    private static class ListenerFilterRecord implements Consumer<DomainEvent>, Predicate<DomainEvent> {
        private final Consumer<DomainEvent> listener;
        private final Predicate<DomainEvent> filter;

        @Override
        public void accept(DomainEvent domainEvent) {
            try {
                listener.accept(domainEvent);
            } catch (Exception ex) {
                log.error("Domain event listener " + listener + " threw exception, ignoring", ex);
            }
        }

        @Override
        public boolean test(DomainEvent domainEvent) {
            return filter.test(domainEvent);
        }
    }
}
