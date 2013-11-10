package app.saleBadger;

import java.security.PublicKey;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.validation.constraints.NotNull;

import app.model.User;
import app.model.dao.UserCatalog;
import app.model.dao.UserCatalogMongo;

// The users endpoint will be hosted at the URI path "/users"
@Path("/api/v1/users")
//The Java method will produce content identified by the MIME Media
@Produces(MediaType.APPLICATION_JSON)
public class UserEndpoint {

    // TODO: update the class to suit your needs
    
    // The Java method will process HTTP GET requests
    @GET 
    @Path("/{username}")
    public User getUser(@NotNull @PathParam("username") String username) {
    	UserCatalog catalog = new UserCatalogMongo();
        return null;
    }
    
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public void addUser() {
    	// get the post parameters
    	
    }
    
    @PUT
    @Path("/{username}")
    @Consumes("application/x-www-form-urlencoded")
    public void updateUser(@PathParam("username") String username) {
    	
    }
    
    @DELETE
    @Path("/{username}")
    public void deleteUser(@PathParam("username") String username) {
    	
    }
    
}
