package net.pkhapps.idispatch.adapter.rest.infrastructure;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * JAX-RS {@link ExceptionMapper} for all exceptions that have not been handled elsewhere.
 */
@Provider
public class DefaultJsonExceptionMapper extends AbstractJsonExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        return toResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getMessage());
    }
}
