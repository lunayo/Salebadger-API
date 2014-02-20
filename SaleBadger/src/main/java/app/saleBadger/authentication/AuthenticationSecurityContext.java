package app.saleBadger.authentication;

import java.security.Principal;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import app.saleBadger.model.Role;
import app.saleBadger.model.User;

public class AuthenticationSecurityContext implements SecurityContext {

	private UriInfo uriInfo;
	private final User user;

	public AuthenticationSecurityContext(@Context UriInfo uriInfo, User user) {
		this.uriInfo = uriInfo;
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
		// administrator role is the strongest
		// restricted is stronger than other roles
		if (role.equals(Role.RESTRICTED) && !role.equals(Role.ADMIN)) {
			// try get the path parameters
			String username = uriInfo.getPathParameters().getFirst("username");
			// check the path for username and compare with current user
			if (username != null && !username.isEmpty())
				return username.equals(user.getUsername());
			return false;
		}
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
