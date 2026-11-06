package app.rest.errorhandlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class ConflictException extends RuntimeException implements
                ExceptionMapper<ConflictException>
{
    private static final long serialVersionUID = 1L;

    public ConflictException() {
        super("Conflict with existing data");
    }

    public ConflictException(String string) {
        super(string);
    }

    @Override
    public Response toResponse(ConflictException exception)
    {
        ErrorResponse body = new ErrorResponse(409, "Conflict", exception.getMessage());

        return Response.status(409).entity(body)
                                    .type(MediaType.APPLICATION_JSON).build();
    }
}
