package net.pkhsolutions.idispatch.ejb.tickets;

import javax.ejb.ApplicationException;
import net.pkhsolutions.idispatch.entity.Ticket;

/**
 * Exception thrown when an operation is attempted on a ticket that has been
 * closed.
 *
 * @author Petter Holmström
 */
@ApplicationException(rollback = true)
public class TicketClosedException extends TicketException {

    public TicketClosedException(Ticket ticket) {
        super(ticket);
    }
}
