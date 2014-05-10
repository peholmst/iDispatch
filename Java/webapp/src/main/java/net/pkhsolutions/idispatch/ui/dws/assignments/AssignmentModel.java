package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractProperty;
import com.vaadin.ui.UI;
import net.pkhsolutions.idispatch.boundary.AssignmentService;
import net.pkhsolutions.idispatch.boundary.events.AssignmentEvent;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.AssignmentType;
import net.pkhsolutions.idispatch.entity.AssignmentUrgency;
import net.pkhsolutions.idispatch.entity.Municipality;
import net.pkhsolutions.idispatch.ui.common.UIAttachable;
import net.pkhsolutions.idispatch.ui.dws.DwsUI;
import net.pkhsolutions.idispatch.utils.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Model for showing and editing a single {@link net.pkhsolutions.idispatch.entity.Assignment}. Changes are directly propagated to the database.
 */
@Component
@Scope(value = "prototype")
class AssignmentModel implements UIAttachable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Property<Assignment> assignment = new AbstractProperty<Assignment>() {

        private Assignment value;

        @Override
        public Assignment getValue() {
            return value;
        }

        @Override
        public void setValue(Assignment newValue) throws ReadOnlyException {
            final Assignment old = this.value;
            this.value = newValue;
            logger.debug("Updating assignment property, old = {}, new = {}", old, newValue);
            assignmentPropertySet.forEach(tp -> tp.assignmentChanged(old, newValue));
            fireValueChange();
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            throw new UnsupportedOperationException("This property is never read-only");
        }

        @Override
        public Class<? extends Assignment> getType() {
            return Assignment.class;
        }
    };
    private final Set<AssignmentProperty<?>> assignmentPropertySet = new HashSet<>();
    private final AssignmentProperty<String> address = new AssignmentProperty<>(String.class,
            Assignment::getAddress, Assignment::setAddress);
    private final AssignmentProperty<Municipality> municipality = new AssignmentProperty<>(Municipality.class,
            Assignment::getMunicipality, Assignment::setMunicipality);
    private final AssignmentProperty<String> description = new AssignmentProperty<>(String.class,
            Assignment::getDescription, Assignment::setDescription);
    private final AssignmentProperty<AssignmentType> type = new AssignmentProperty<>(AssignmentType.class,
            Assignment::getType, Assignment::setType);
    private final AssignmentProperty<AssignmentUrgency> urgency = new AssignmentProperty<>(AssignmentUrgency.class,
            Assignment::getUrgency, Assignment::setUrgency);
    private final AssignmentProperty<Date> opened = new AssignmentProperty<>(Date.class, Assignment::getOpened);
    private final AssignmentProperty<Date> closed = new AssignmentProperty<>(Date.class, Assignment::getClosed);
    private final AssignmentProperty<Long> id = new AssignmentProperty<>(Long.class, Assignment::getId);
    private final AssignmentProperty<Boolean> isOpen = new AssignmentProperty<>(Boolean.class, Assignment::isOpen);
    private final AssignmentProperty<Boolean> isClosed = new AssignmentProperty<>(Boolean.class, Assignment::isClosed);

    @Autowired
    AssignmentService assignmentService;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;
    @Autowired
    DwsUI ui;

    @EventBusListenerMethod
    void onTicketEvent(AssignmentEvent assignmentEvent) {
        logger.debug("Received assignment event {}", assignmentEvent);
        ui.access(() -> {
            if (assignmentEvent.getAssignment().equals(assignment().getValue())) {
                assignment().setValue(assignmentEvent.getAssignment());
            }
        });
    }

    public Property<Assignment> assignment() {
        return assignment;
    }

    public boolean isEditable() {
        Assignment assignment = assignment().getValue();
        return assignment != null && !assignment.isClosed();
    }

    public Property<Long> id() {
        return id;
    }

    public Property<Date> opened() {
        return opened;
    }

    public Property<Date> closed() {
        return closed;
    }

    public Property<AssignmentUrgency> urgency() {
        return urgency;
    }

    public Property<AssignmentType> type() {
        return type;
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

    public Property<Boolean> isClosed() {
        return isClosed;
    }

    public Property<Boolean> isOpen() {
        return isOpen;
    }

    @Override
    public void attachedToUI(UI ui) {
        eventBus.subscribe(this);
    }

    @Override
    public void detachedFromUI(UI ui) {
        eventBus.unsubscribe(this);
    }

    @FunctionalInterface
    interface Getter<T> {
        T get(Assignment assignment);
    }

    @FunctionalInterface
    interface Setter<T> {
        void set(Assignment assignment, T value);
    }

    private class AssignmentProperty<T> extends AbstractProperty<T> {

        private final Class<T> type;
        private final Getter<T> getter;
        private final Setter<T> setter;

        AssignmentProperty(Class<T> type, Getter<T> getter, Setter<T> setter) {
            this.type = type;
            this.getter = getter;
            this.setter = setter;
            if (setter == null) {
                setReadOnly(true);
            }
            assignmentPropertySet.add(this);
        }

        AssignmentProperty(Class<T> type, Getter<T> getter) {
            this(type, getter, null);
        }

        void assignmentChanged(Assignment oldAssignment, Assignment newAssignment) {
            if (oldAssignment == null ^ newAssignment == null) {
                fireValueChange();
            } else if (oldAssignment != null && newAssignment != null) {
                final T oldValue = getter.get(oldAssignment);
                final T newValue = getter.get(newAssignment);
                if (!Objects.equals(oldValue, newValue)) {
                    fireValueChange();
                }
            }
            fireReadOnlyStatusChange();
        }

        @Override
        public T getValue() {
            final Assignment assignment = assignment().getValue();
            return assignment == null ? null : getter.get(assignment);
        }

        @Override
        public void setValue(T newValue) throws ReadOnlyException {
            if (isReadOnly()) {
                throw new ReadOnlyException();
            }
            final Assignment assignment = assignment().getValue();
            if (assignment != null) {
                setter.set(assignment, newValue);
                final UpdateResult<Assignment> result = assignmentService.updateAssignment(assignment);
                if (result.isSuccessful()) {
                    assignment().setValue(result.getData().get());
                } else {
                    // TODO Inform user that something went wrong
                    assignment().setValue(assignmentService.findAssignment(assignment.getId()).get());
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
