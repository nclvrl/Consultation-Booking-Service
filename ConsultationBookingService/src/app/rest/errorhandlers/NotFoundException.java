package app.rest.errorhandlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class NotFoundException extends RuntimeException implements
                ExceptionMapper<NotFoundException>
{
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String string) {
        super(string);
    }

    @Override
    public Response toResponse(NotFoundException exception)
    {
        ErrorResponse body = new ErrorResponse(404, "Not Found", exception.getMessage());

        return Response.status(404).entity(body)
                                    .type(MediaType.APPLICATION_JSON).build();
    }
}
