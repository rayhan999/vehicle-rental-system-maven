package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.filters;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.User;

public class MySecurityContext implements SecurityContext {
	private User user;
	private boolean secure;

	public MySecurityContext(User user, boolean secure) {
		this.user = user;
		this.secure = secure;
	}

	@Override
	public Principal getUserPrincipal() {

		return this.user;

	}

	@Override
	public boolean isUserInRole(String role) {
		if (user.getRoles() != null) {
			return user.getRoles().contains(role);
		}
		return false;
	}

	@Override
	public boolean isSecure() {
		return secure;
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}
}