package app.saleBadger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import app.model.User;

// The users endpoint will be hosted at the URI path "/users"
@Path("/api/v1/users")
public class UserEndpoint {

    // TODO: update the class to suit your needs
    
    // The Java method will process HTTP GET requests
    @GET 
    @Path("{username}")
    // The Java method will produce content identified by the MIME Media
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username) {
        return null;
    }
}
