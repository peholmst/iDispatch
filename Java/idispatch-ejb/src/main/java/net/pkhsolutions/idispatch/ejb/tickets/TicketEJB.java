package net.pkhsolutions.idispatch.ejb.tickets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Validator;
import net.pkhsolutions.idispatch.ejb.common.ValidationFailedException;
import net.pkhsolutions.idispatch.ejb.resources.ResourceStatusEJB;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.AbstractResourceStatus;
import net.pkhsolutions.idispatch.entity.CurrentResourceStatus;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.entity.Ticket;
import net.pkhsolutions.idispatch.entity.TicketType;
import net.pkhsolutions.idispatch.entity.TicketUrgency;

/**
 * EJB for handling Tickets.
 *
 * @author Petter Holmström
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
// TODO Security
public class TicketEJB {

    @PersistenceContext
    EntityManager entityManager;
    @javax.annotation.Resource
    Validator validator;
    @Inject
    Event<DispatchNotificationCreatedEvent> dispatchNotificationCreatedEventBus;
    @EJB
    ResourceStatusEJB resourceStatusBean;

    /**
     * Opens a new ticket, stores it in the database, fires a
     * {@link TicketOpenedEvent} and returns the newly created ticket.
     */
    public Ticket openTicket() {
        Ticket newTicket = new Ticket.Builder().build();
        entityManager.persist(newTicket);
        return newTicket;
    }

    public Ticket findTicketById(Long id) {
        return entityManager.find(Ticket.class, id);
    }

    // TODO Discard ticket
    /**
     * Closes an existing ticket and fires a {@link TicketClosedEvent}.
     *
     * @throws NoSuchTicketException if the ticket was not found in the
     * database.
     * @throws TicketModifiedException if the ticket was changed by another user
     * after it was retrieved from the database.
     * @throws TicketClosedException if the ticket is already closed.
     */
    public void closeTicket(Ticket ticket) throws NoSuchTicketException, TicketModifiedException, TicketClosedException {
        verifyThatTicketExistsAndIsOpen(ticket);
        Ticket closedTicket = new Ticket.Builder(ticket).close().build();
        try {
            entityManager.merge(closedTicket);
            entityManager.flush();
        } catch (OptimisticLockException ex) {
            throw new TicketModifiedException(ticket);
        }
    }

    public List<OpenTicketDTO> findOpenTickets() {
        Query query = entityManager.createQuery("SELECT t.id, t.ticketType, t.ticketOpened, t.urgency FROM Ticket t WHERE t.ticketClosed IS NULL ORDER BY t.ticketOpened");
        List<OpenTicketDTO> resultList = new ArrayList<>();
        for (Object resultRow : query.getResultList()) {
            Object[] cells = (Object[]) resultRow;
            Long id = (Long) cells[0];
            StringBuilder ticketType = new StringBuilder();
            if (cells[1] != null) {
                ticketType.append(((TicketType) cells[1]).getCode());
            }
            if (cells[3] != null) {
                ticketType.append(((TicketUrgency) cells[3]).name());
            }
            Calendar ticketOpened = (Calendar) cells[2];
            resultList.add(new OpenTicketDTO(id, ticketOpened, ticketType.toString(), false, false, false)); // TODO Boolean flags
        }
        return resultList;
    }

    private Ticket findPersistentTicket(Ticket detachedTicket) throws NoSuchTicketException {
        Ticket persistentTicket = null;
        if (detachedTicket.isPersistent()) {
            persistentTicket = entityManager.find(Ticket.class, detachedTicket.getId());
        }
        if (persistentTicket == null) {
            throw new NoSuchTicketException(detachedTicket);
        }
        return persistentTicket;
    }

    private void verifyThatTicketExistsAndIsOpen(Ticket detachedTicket) throws NoSuchTicketException, TicketClosedException {
        if (findPersistentTicket(detachedTicket).isClosed()) {
            throw new TicketClosedException(detachedTicket);
        }
    }

    /**
     * Saves an existing ticket, fires a {@link TicketChangedEvent} and returns
     * the updated ticket.
     *
     * @throws NoSuchTicketException if the ticket was not found in the
     * database.
     * @throws TicketModifiedException if the ticket was changed by another user
     * after it was retrieved from the database.
     * @throws TicketClosedException if the ticket is closed.
     */
    public Ticket saveTicket(Ticket ticket) throws TicketModifiedException, TicketClosedException, NoSuchTicketException {
        verifyThatTicketExistsAndIsOpen(ticket);
        try {
            Ticket updatedTicket = entityManager.merge(ticket);
            entityManager.flush();
            return updatedTicket;
        } catch (OptimisticLockException ex) {
            throw new TicketModifiedException(ticket);
        }
    }

    public Ticket refreshTicket(Ticket entity) throws NoSuchTicketException {
        return findPersistentTicket(entity);
    }

    public List<TicketResourceDTO> findResourcesForTicket(Ticket ticket) throws NoSuchTicketException {
        return null;
    }

    public TicketResourceDTO assignResourceToTicket(Resource resource, Ticket ticket) throws TicketClosedException, NoSuchTicketException {
        /*       verifyThatTicketExistsAndIsOpen(ticket);
         CurrentResourceStatus status = resourceStatusBean.getCurrentStatus(resource);
         status.setTicket(ticket);
         status.setResourceState(ResourceState.ASSIGNED);
         status = resourceStatusBean.updateStatus(status);
         return new TicketResourceDTO(resource.getCallSign(), status.getStateChangeTimestamp(), null, null, null, null, null, false);*/
        return null;
    }

    public void dispatchAllResources(Ticket ticket) throws ValidationFailedException, TicketClosedException, NoSuchTicketException {
        verifyThatTicketExistsAndIsOpen(ticket);
        validateTicketForDispatching(ticket);
    }

    public void dispatchAssignedResources(Ticket ticket) throws ValidationFailedException, TicketClosedException, NoSuchTicketException {
        verifyThatTicketExistsAndIsOpen(ticket);
        validateTicketForDispatching(ticket);
        // TODO Implement me!
    }

    public void dispatchSelectedResources(Ticket ticket, Set<Resource> resources) throws ValidationFailedException, TicketClosedException, NoSuchTicketException {
        verifyThatTicketExistsAndIsOpen(ticket);
        validateTicketForDispatching(ticket);
        DispatchNotification notification = new DispatchNotification.Builder().fromTicket(ticket).withResources(resources).build();
        entityManager.persist(notification);
        entityManager.flush();
        dispatchNotificationCreatedEventBus.fire(new DispatchNotificationCreatedEvent(notification));
    }

    private void validateTicketForDispatching(Ticket ticket) throws ValidationFailedException {
        ValidationFailedException.throwIfNonEmpty(validator.validate(ticket, Ticket.DispatchValidation.class));
    }
}
