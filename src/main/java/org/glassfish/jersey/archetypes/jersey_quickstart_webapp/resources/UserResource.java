package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.resources;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.annotations.Secured;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.model.ErrorMessage;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.User;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services.UserService;

import com.google.inject.Inject;

/**
 * Root resource (exposed at "users" path)
 */
@Path("users")
@Singleton
public class UserResource {

	@Inject
	private UserService userService = new UserService();
	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext securityContext;

	private void addHateoasLinksToVehicle(User user, String userName) {
		// Clear existing links to prevent duplicates
		user.clearLinks();

		// Self-link (link to this specific vehicle)
		String selfUri = uriInfo.getBaseUriBuilder().path(UserResource.class).path(String.valueOf(userName)).build()
				.toString();
		user.addLink(selfUri, "self");

		// Link to rentals for this vehicle
		String rentalsUri = uriInfo.getBaseUriBuilder().path(RentalResource.class) // RentalResource class should be//
																					// Directly linking the method in
																					// RentalResource
				.resolveTemplate("id", userName).resolveTemplate("type", "users")// Resolve vehicleId in the URL
				.build().toString();
		user.addLink(rentalsUri, "rentals");
	}

	@GET
	@Secured
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUsers(@Context SecurityContext securityContext) {
		if (!securityContext.isUserInRole("admin")) {
			throw new WebApplicationException("Not authorized", 401);
		}

		return Response.ok(UserService.getAllUsers()).build();
	}

	@GET
	@Path("/{userName}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("userName") String userName) {

		User user = UserService.getUserById(userName);

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorMessage("User with ID " + userName + " not found", 404, null))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return Response.ok(user).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(User user) {
		User newUser = userService.addUser(user);
		addHateoasLinksToVehicle(user, user.getUserName());
		return Response.status(Response.Status.CREATED).entity(newUser).build();

	}

	@PUT
	@Path("/{userName}")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("userName") String userName, User user) {

		if (!securityContext.isUserInRole("admin") && !securityContext.isUserInRole("moderator")) {
			throw new WebApplicationException("Not authorized", 401);
		}
		User updatedUser = userService.updateUser(userName, user);

		if (updatedUser == null) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage("User not Found", 404, null))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return Response.ok(updatedUser).build();

	}

	@DELETE
	@Path("/{userName}")
	@Secured
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteUser(@PathParam("userName") String userName) {

		if (!securityContext.isUserInRole("admin")) {
			throw new WebApplicationException("Not authorized", 401);
		}
		boolean flag = userService.deleteUser(userName);
		if (flag == false) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage("User not Found", 404, null))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return Response.ok("Deleted").build();

	}
}
