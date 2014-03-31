package app.saleBadger;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.saleBadger.model.Role;
import app.saleBadger.model.dao.UserRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;

//The authentication resource will be hosted at the URI path "/auth"
@Path("v1/auth/")
// The Java method will produce content identified by the MIME Media
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({ Role.ADMIN, Role.USER })
public class AuthenticationResource {

	ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	UserRepository userRepository = context.getBean(UserRepository.class);

	@GET
	@Path("basic")
	public Response basicAuthentication() {

		return Response.status(Response.Status.NO_CONTENT).build();
	}

}
