package app.saleBadger;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.Size;
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
import app.saleBadger.validator.ErrorMessagesMapper;
import app.saleBadger.webexception.BadRequestException;
import app.saleBadger.webexception.ConflictException;
import app.saleBadger.webexception.NotFoundException;

// The users resource will be hosted at the URI path "/users"
@Path("users/")
// The Java method will produce content identified by the MIME Media
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	// TODO: update the class to suit your needs
	private final static ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final static UserRepository userRepository = context
			.getBean(UserRepository.class);

	// The Java method will process HTTP GET requests
	@GET
	@PermitAll
	@Path("{username}")
	public User getUser(
			@Size(min = 5, max = 20, message = "{user.wrong.username}")
			@PathParam("username") String username) {
		
		List<String> errors = new ArrayList<String>();
		User user = userRepository.findOne(username);

		if (user == null) {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		} else {
			return user;
		}
	}

	@POST
	@PermitAll
	@Consumes(MediaType.APPLICATION_JSON)
	public User addUser(@Valid User user, @Context UriInfo uriInfo) {

		List<String> errors = new ArrayList<String>();
		User userInRepository = userRepository.findOne(user.getUsername());

		if (userInRepository != null) {
			// user exists in the repository
			// throw conflict
			errors.add(ErrorMessagesMapper.getString("user.conflict.exist"));
			throw new ConflictException(errors, uriInfo.getBaseUriBuilder()
					.path("/users/{username}").build(user.getUsername()));
		} else {
			User result = userRepository.save(user);
			if (result != null) {
				return result;
			} else {
				// throw bad request
				errors.add(ErrorMessagesMapper.getString("app.unknown.error"));
				throw new BadRequestException(errors);
			}
		}
	}

	@PUT
	@PermitAll
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	public User updateUser(
			@Size(min = 5, max = 20, message = "{user.wrong.username}") 
			@PathParam("username") String username,
			@Valid User user) {
		
		List<String> errors = new ArrayList<String>();
		User userInRepository = userRepository.findOne(username);

		if (userInRepository != null) {
			User result = userRepository.save(user);
			if (result != null) {
				return result;
			} else {
				// throw bad request
				errors.add(ErrorMessagesMapper.getString("app.unknown.error"));
				throw new BadRequestException(errors);
			}
		} else {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		}
	}

	@DELETE
	@PermitAll
	@Path("/{username}")
	public Response deleteUser(
			@Size(min = 5, max = 20, message = "{user.wrong.username}") 
			@PathParam("username") String username) {
		
		List<String> errors = new ArrayList<String>();
		User user = userRepository.findOne(username);

		if (user == null) {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		} else {
			userRepository.delete(username);
			return Response.status(Response.Status.NO_CONTENT).build();
		}
	}

}
