package app.saleBadger.authentication;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import app.model.User;

public class AuthenticationSecurityContext implements SecurityContext {
	
	private final User user;
	
	public AuthenticationSecurityContext(User user) {
		this.user = user;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAuthenticationScheme() {
		// TODO Auto-generated method stub
		return null;
	}

}
