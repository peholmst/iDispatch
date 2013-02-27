package net.pkhsolutions.idispatch.ejb.resources;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class NoStatusInformationFoundException extends Exception {
}
