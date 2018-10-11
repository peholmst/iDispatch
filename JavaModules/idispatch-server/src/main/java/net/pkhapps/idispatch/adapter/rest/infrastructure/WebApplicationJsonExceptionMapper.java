package net.pkhapps.idispatch.adapter.rest.infrastructure;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * JAX-RS {@link ExceptionMapper} for {@link WebApplicationException}s.
 */
@Provider
public class WebApplicationJsonExceptionMapper extends AbstractJsonExceptionMapper
        implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        return toResponse(exception.getResponse().getStatus(), exception.getMessage());
    }
}
