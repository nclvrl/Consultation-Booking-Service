package app.rest.errorhandlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class BadRequestException extends RuntimeException implements
                ExceptionMapper<BadRequestException>
{
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super("Bad request");
    }

    public BadRequestException(String string) {
        super(string);
    }

    @Override
    public Response toResponse(BadRequestException exception)
    {
        ErrorResponse body = new ErrorResponse(400, "Bad Request", exception.getMessage());

        return Response.status(400).entity(body)
                                    .type(MediaType.APPLICATION_JSON).build();
    }
}
