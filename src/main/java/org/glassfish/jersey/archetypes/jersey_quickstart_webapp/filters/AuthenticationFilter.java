package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.filters;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Base64;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services.UserService;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.utils.JwtUtils;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.annotations.Secured;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.User;

@Secured
@Provider
@RequestScoped
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	public JwtUtils jwtUtil = new JwtUtils();
	public UserService userService = new UserService();


	@Override
	public void filter(ContainerRequestContext requestContext) {
		String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			abortWithUnauthorized(requestContext);
			return;
		}
		String token = authHeader.substring("Bearer ".length()).trim();
		try {

			String username = jwtUtil.extractUsername(token);
			if (username == null || !jwtUtil.validateToken(token, username)) {
				abortWithUnauthorized(requestContext);
				return;
			}
			User authenticatedUser = UserService.getUserById(username);
			boolean secure = requestContext.getSecurityContext().isSecure();

			requestContext.setSecurityContext(new MySecurityContext(authenticatedUser, secure));

		} catch (IllegalArgumentException e) {
			abortWithUnauthorized(requestContext);
		}
	}


	private void abortWithUnauthorized(ContainerRequestContext requestContext) {
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"MyRealm\"").build());
	}
}