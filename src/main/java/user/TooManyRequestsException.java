package user;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class TooManyRequestsException extends WebApplicationException {
    public TooManyRequestsException(String message) {
        super(message, Response.Status.TOO_MANY_REQUESTS);
    }
}
