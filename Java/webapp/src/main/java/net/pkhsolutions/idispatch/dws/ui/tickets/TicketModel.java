package net.pkhsolutions.idispatch.dws.ui.tickets;

import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractProperty;
import net.pkhsolutions.idispatch.domain.Municipality;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import net.pkhsolutions.idispatch.domain.tickets.TicketService;
import net.pkhsolutions.idispatch.domain.tickets.TicketType;
import net.pkhsolutions.idispatch.domain.tickets.TicketUrgency;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBusListenerMethod;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Model for showing and editing a single {@link net.pkhsolutions.idispatch.domain.tickets.Ticket}. Changes are directly propagated to the database. Remember to
 * subscribe the model to the application scoped {@link org.vaadin.spring.events.EventBus}.
 */
@Component
@Scope(value = "prototype")
class TicketModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Property<Ticket> ticket = new AbstractProperty<Ticket>() {

        private Ticket value;

        @Override
        public Ticket getValue() {
            return value;
        }

        @Override
        public void setValue(Ticket newValue) throws ReadOnlyException {
            final Ticket old = this.value;
            this.value = newValue;
            logger.debug("Updating ticket property, old = {}, new = {}", old, newValue);
            ticketPropertySet.forEach(tp -> tp.ticketChanged(old, newValue));
            fireValueChange();
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            throw new UnsupportedOperationException("This property is never read-only");
        }

        @Override
        public Class<? extends Ticket> getType() {
            return Ticket.class;
        }
    };
    private final Set<TicketProperty<?>> ticketPropertySet = new HashSet<>();
    private final TicketProperty<String> address = new TicketProperty<>(String.class,
            Ticket::getAddress, Ticket::setAddress);
    private final TicketProperty<Municipality> municipality = new TicketProperty<>(Municipality.class,
            Ticket::getMunicipality, Ticket::setMunicipality);
    private final TicketProperty<String> description = new TicketProperty<>(String.class,
            Ticket::getDescription, Ticket::setDescription);
    private final TicketProperty<TicketType> ticketType = new TicketProperty<>(TicketType.class,
            Ticket::getType, Ticket::setType);
    private final TicketProperty<TicketUrgency> urgency = new TicketProperty<>(TicketUrgency.class,
            Ticket::getUrgency, Ticket::setUrgency);
    private final TicketProperty<Date> ticketOpened = new TicketProperty<>(Date.class, Ticket::getTicketOpened);
    private final TicketProperty<Date> ticketClosed = new TicketProperty<>(Date.class, Ticket::getTicketClosed);
    private final TicketProperty<Long> id = new TicketProperty<>(Long.class, Ticket::getId);
    @Autowired
    TicketService ticketService;

    @EventBusListenerMethod
    void onTicketEvent(TicketEvent ticketEvent) {
        logger.debug("Received ticket event {}", ticketEvent);
        if (ticketEvent.getTicket().equals(ticket().getValue())) {
            ticket().setValue(ticketEvent.getTicket());
        }
    }

    public Property<Ticket> ticket() {
        return ticket;
    }

    public boolean isEditable() {
        Ticket ticket = ticket().getValue();
        return ticket != null && !ticket.isClosed();
    }

    public Property<Long> id() {
        return id;
    }

    public Property<Date> ticketOpened() {
        return ticketOpened;
    }

    public Property<Date> ticketClosed() {
        return ticketClosed;
    }

    public Property<TicketUrgency> urgency() {
        return urgency;
    }

    public Property<TicketType> ticketType() {
        return ticketType;
    }

    public Property<String> description() {
        return description;
    }

    public Property<Municipality> municipality() {
        return municipality;
    }

    public Property<String> address() {
        return address;
    }

    @FunctionalInterface
    interface Getter<T> {
        T get(Ticket ticket);
    }

    @FunctionalInterface
    interface Setter<T> {
        void set(Ticket ticket, T value);
    }

    class TicketProperty<T> extends AbstractProperty<T> {

        private final Class<T> type;
        private final Getter<T> getter;
        private final Setter<T> setter;

        TicketProperty(Class<T> type, Getter<T> getter, Setter<T> setter) {
            this.type = type;
            this.getter = getter;
            this.setter = setter;
            if (setter == null) {
                setReadOnly(true);
            }
            ticketPropertySet.add(this);
        }

        TicketProperty(Class<T> type, Getter<T> getter) {
            this(type, getter, null);
        }

        void ticketChanged(Ticket oldTicket, Ticket newTicket) {
            if (oldTicket == null ^ newTicket == null) {
                fireValueChange();
            } else if (oldTicket != null && newTicket != null) {
                final T oldValue = getter.get(oldTicket);
                final T newValue = getter.get(newTicket);
                if (!Objects.equals(oldValue, newValue)) {
                    fireValueChange();
                }
            }
            fireReadOnlyStatusChange();
        }

        @Override
        public T getValue() {
            final Ticket ticket = ticket().getValue();
            return ticket == null ? null : getter.get(ticket);
        }

        @Override
        public void setValue(T newValue) throws ReadOnlyException {
            if (isReadOnly()) {
                throw new ReadOnlyException();
            }
            final Ticket ticket = ticket().getValue();
            if (ticket != null) {
                setter.set(ticket, newValue);
                try {
                    ticket().setValue(ticketService.updateTicket(ticket));
                } catch (OptimisticLockingFailureException ex) {
                    logger.debug("Received optimistic locking exception while saving ticket with ID {}, refreshing", ticket.getId());
                    ticket().setValue(ticketService.retrieveTicket(ticket.getId()).get());
                }
            }
        }

        @Override
        public Class<? extends T> getType() {
            return type;
        }

        @Override
        public boolean isReadOnly() {
            return setter == null || super.isReadOnly() || !isEditable();
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            if (!newStatus && setter == null) {
                throw new UnsupportedOperationException("This property is always read-only");
            }
            super.setReadOnly(newStatus);
        }
    }

}
