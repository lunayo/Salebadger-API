package app.saleBadger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import app.model.User;
import app.model.dao.UserCatalog;
import app.model.dao.UserCatalogMongo;
import app.saleBadger.WebException.NotFoundException;

// The users resource will be hosted at the URI path "/users"
@Path("/api/v1/users")
//The Java method will produce content identified by the MIME Media
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    // TODO: update the class to suit your needs
    
    // The Java method will process HTTP GET requests
    @GET 
    @Path("/{username}")
    public User getUser(@NotNull @PathParam("username") String username) {
    	
    	UserCatalog userCatalog = UserCatalogMongo.getInstance();
        User user = userCatalog.find(username);
        
        if (user != null) {
        	return user;
        }
        
        throw new NotFoundException(username);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addUser(@Valid User user) {
    	UserCatalog userCatalog = UserCatalogMongo.getInstance();
    	userCatalog.add(user);
    }
    
    @PUT
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void updateUser(@Valid User user) {
    	
    }
    
    @DELETE
    @Path("/{username}")
    public void deleteUser(@PathParam("username") String username) {
    	
    }
    
}
