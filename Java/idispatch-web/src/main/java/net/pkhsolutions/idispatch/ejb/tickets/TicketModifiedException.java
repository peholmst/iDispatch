package net.pkhsolutions.idispatch.ejb.tickets;

import javax.ejb.ApplicationException;
import net.pkhsolutions.idispatch.entity.Ticket;

/**
 * Exception thrown when an operation is attempted on a ticket that has been
 * modified by another user since it was last retrieved from the database.
 *
 * @author Petter Holmström
 */
@ApplicationException(rollback = true)
public class TicketModifiedException extends TicketException {

    public TicketModifiedException(Ticket ticket) {
        super(ticket);
    }
}
