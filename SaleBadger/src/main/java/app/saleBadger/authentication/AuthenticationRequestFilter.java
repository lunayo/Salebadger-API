package app.saleBadger.authentication;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws WebApplicationException {
		// TODO Auto-generated method stub
		
		
	}

}
