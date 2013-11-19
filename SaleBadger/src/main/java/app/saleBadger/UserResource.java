package app.saleBadger;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.model.User;
import app.model.dao.UserRepository;
import app.model.dao.config.SpringMongoConfig;
import app.saleBadger.WebException.BadRequestException;
import app.saleBadger.WebException.ConflictException;
import app.saleBadger.WebException.NotFoundException;

// The users resource will be hosted at the URI path "/users"
@Path("v1/users/")
// The Java method will produce content identified by the MIME Media
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	// TODO: update the class to suit your needs
	ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	UserRepository userRepository = context.getBean(UserRepository.class);

	// The Java method will process HTTP GET requests
	@GET
	@Path("{username}")
	public User getUser(@PathParam("username") String username) {

		List<String> errors = new ArrayList<String>();
		User user = userRepository.findOne(username);

		if (user == null) {
			errors.add(username + " " + "does not exist");
			throw new NotFoundException(errors);
		} else {
			return user;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public User addUser(@Valid User user, @Context UriInfo uriInfo) {

		List<String> errors = new ArrayList<String>();
		User userInRepository = userRepository.findOne(user.getUsername());

		if (userInRepository != null) {
			// user exists in the repository
			// throw conflict
			errors.add(user.getUsername() + " " + "has already been taken");
			throw new ConflictException(errors, uriInfo.getBaseUriBuilder()
					.path("/users/{username}").build(user.getUsername()));
		} else {
			User result = userRepository.save(user);
			if (result != null) {
				return result;
			} else {
				// throw bad request
				errors.add("unknown internal errors");
				throw new BadRequestException(errors);
			}
		}
	}

	@PUT
	@Path("/{username}")
	public void updateUser(@Valid User user) {

	}

	@DELETE
	@Path("/{username}")
	public Response deleteUser(@PathParam("username") String username) {
		List<String> errors = new ArrayList<String>();
		User user = userRepository.findOne(username);

		if (user == null) {
			errors.add(username + " " + "does not exist");
			throw new NotFoundException(errors);
		} else {
			userRepository.delete(username);
			return Response.ok().build();
		}
	}

}
