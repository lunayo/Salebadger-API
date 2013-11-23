package app.saleBadger.authentication;

import java.security.Principal;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import app.model.User;

public class AuthenticationSecurityContext implements SecurityContext {

	@Context
    private UriInfo uriInfo;
	private final User user;

	public AuthenticationSecurityContext(User user) {
		this.user = user;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return new Principal() {

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return user.getUsername();
			}
		};
	}

	@Override
	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return role.equals(user.getRole());
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		 return "https".equals(uriInfo.getRequestUri().getScheme());
	}

	@Override
	public String getAuthenticationScheme() {
		// TODO Auto-generated method stub
		return SecurityContext.BASIC_AUTH;
	}

}
