package net.pkhsolutions.idispatch.ejb.tickets;

import javax.ejb.ApplicationException;

/**
 *
 * @author peholmst
 */
@ApplicationException(rollback = true)
public class NoSuchReceiverException extends Exception {
}
