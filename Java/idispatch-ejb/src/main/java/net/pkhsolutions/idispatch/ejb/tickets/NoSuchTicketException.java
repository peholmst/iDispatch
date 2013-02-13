package net.pkhsolutions.idispatch.ejb.tickets;

import javax.ejb.ApplicationException;
import net.pkhsolutions.idispatch.entity.Ticket;

/**
 * Exception thrown when an operation is attempted on a ticket that cannot be
 * found in the database.
 *
 * @author Petter Holmström
 */
@ApplicationException(rollback = true)
public class NoSuchTicketException extends TicketException {

    public NoSuchTicketException(Ticket ticket) {
        super(ticket);
    }
}
