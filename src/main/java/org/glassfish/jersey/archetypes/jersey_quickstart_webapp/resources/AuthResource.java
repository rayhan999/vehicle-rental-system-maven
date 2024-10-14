package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.resources;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services.UserService;

import com.google.inject.Inject;

@Path("auth")
@Singleton
public class AuthResource {

	@Inject
	UserService userService = new UserService();

	public AuthResource() {
        System.out.println("AuthResource initialized.");
    }
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(UserCredentials credentials) {
		String token = userService.authenticateUser(credentials.getUsername(), credentials.getPassword());
		return Response.ok(new AuthResponse(token)).build();
	}

	public static class UserCredentials {
		private String userName;
		private String password;

		public String getUsername() {
			return userName;
		}

		public void setUsername(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	public static class AuthResponse {
		private String token;

		public AuthResponse(String token) {
			this.token = token;
		}

		public String getToken() {
			return token;
		}
	}
}
