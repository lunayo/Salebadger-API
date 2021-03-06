package app.saleBadger;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.saleBadger.model.Role;
import app.saleBadger.model.User;
import app.saleBadger.model.dao.UserRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;
import app.saleBadger.validator.ErrorMessagesMapper;
import app.saleBadger.webexception.BadRequestException;
import app.saleBadger.webexception.ConflictException;
import app.saleBadger.webexception.NotFoundException;

// The users resource will be hosted at the URI path "/users"
@Path("v1/user/")
// The Java method will produce content identified by the MIME Media
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({Role.ADMIN, Role.RESTRICTED})
public class UserResource {

	ApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    UserRepository userRepository = context.getBean(UserRepository.class);

	// The Java method will process HTTP GET requests
	@GET
	@Path("{username}")
	public Response getUser(
			@Size(min = 5, max = 20, message = "{user.wrong.username}")
			@PathParam("username") String username,
			@Context Request request) {
		
		List<String> errors = new ArrayList<String>();
		
		User user = userRepository.findOne(username);
		if (user == null) {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		} 
		
		// Generate Etag out of hashCode of user
	    EntityTag tag = new EntityTag(Integer.toString(user.hashCode()));
        CacheControl cc = new CacheControl();
        // Set max age to one day
        cc.setMaxAge(86400);
        ResponseBuilder builder = request.evaluatePreconditions(tag);
        if (builder != null) {
            //means the preconditions have been met and the cache is valid
            //we just need to reset the cachecontrol max age (optional)
            builder.cacheControl(cc);
            return builder.build();
        }

        //preconditions are not met and the cache is invalid
        //need to send new value with reponse code 200 (OK)
        builder = Response.ok(user);
        //reset cache control and eTag (mandatory)
        builder.cacheControl(cc);
        builder.tag(tag);
        return builder.build();
		
	}

	@POST
	@PermitAll
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public User addUser(@Valid User user, @Context UriInfo uriInfo) {
		List<String> errors = new ArrayList<String>();

		if (userRepository.exists(user.getUsername())) {
			// user exists in the repository
			// throw conflict
			errors.add(ErrorMessagesMapper.getString("user.does.exist"));
			throw new ConflictException(errors, uriInfo.getBaseUriBuilder()
					.path("/users/{username}").build(user.getUsername()));
		} else {
			user.setDateCreated();
			user.setRole(Role.USER);
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
	@Path("{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	public User updateUser(
			@Size(min = 5, max = 20, message = "{user.wrong.username}") 
			@PathParam("username") String username,
			@Valid User user) {
		List<String> errors = new ArrayList<String>();
		
		user.setUsername(username);
		
		if (userRepository.exists(username)) {
			User storedUser = userRepository.findOne(username);
			// update user modified date
			user.setDateCreated(storedUser.getDateCreated());
			user.updateDateModified();
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
	@Path("{username}")
	public Response deleteUser(
			@Size(min = 5, max = 20, message = "{user.wrong.username}") 
			@PathParam("username") String username) {
		
		List<String> errors = new ArrayList<String>();

		if (!userRepository.exists(username)) {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		} else {
			userRepository.delete(username);
			return Response.status(Response.Status.NO_CONTENT).build();
		}
	}

}
