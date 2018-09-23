package net.pkhapps.idispatch.adapter.rest;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * JAX-RS {@link ExceptionMapper} for {@link ConstraintViolationException}s.
 */
@Provider
public class ConstraintViolationJsonExceptionMapper extends AbstractJsonExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return toResponse(Response.Status.BAD_REQUEST.getStatusCode(), exception.getMessage());
    }
}
