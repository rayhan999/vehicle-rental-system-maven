package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.resources;

import java.util.List;

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

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.Rental;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services.RentalService;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.annotations.Secured;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.model.ErrorMessage;

@Path("/{type}/{id}/rentals")
@Singleton
public class RentalResource {

	RentalService rentalService = new RentalService();

	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext securityContext;

	private void addHateoasLinksToRental(Rental rental, int vehicleId, String userId) {
		// Clear existing links to prevent duplicates
		rental.clearLinks();

		// Self-link for users
		String selfFromUsersUri = uriInfo.getBaseUriBuilder().path(RentalResource.class)
				.resolveTemplate("type", "users").resolveTemplate("id", userId).path(String.valueOf(rental.getId()))
				.build().toString();
		rental.addLink(selfFromUsersUri, "self-from users");

		// Self-link for vehicles
		String selfFromVehiclesUri = uriInfo.getBaseUriBuilder().path(RentalResource.class)
				.resolveTemplate("type", "vehicles").resolveTemplate("id", vehicleId)
				.path(String.valueOf(rental.getId())).build().toString();
		rental.addLink(selfFromVehiclesUri, "self-from vehicles");

		// Link to the vehicle
		String vehicleUrl = uriInfo.getBaseUriBuilder().path(VehicleResource.class).path(String.valueOf(vehicleId))
				.build().toString();
		rental.addLink(vehicleUrl, "vehicle");

		// Link to the user
		String userUrl = uriInfo.getBaseUriBuilder().path(UserResource.class).path(userId).build().toString();
		rental.addLink(userUrl, "user");
	}

	// Get all rentals for a specific vehicle
	@GET
	@Secured
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getRentals(@PathParam("id") String id, @PathParam("type") String type) {

		List<Rental> rentals = null;
		if ("vehicles".equals(type)) {
			rentals = rentalService.getRentalsByVehicleId(Integer.valueOf(id));
		} else if ("users".equals(type)) {
			rentals = rentalService.getRentalsByUserId(id);
		} else if ("admin".equals(type)) {
			rentals = rentalService.getAllRentals();
		} else {

			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Invalid rental type. Use 'vehicles' or 'users'.").build();
		}

		return Response.ok(rentals).build();
	}

	// Create a new rental for a specific vehicle
	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRental(@PathParam("id") String id, Rental rental, @PathParam("type") String type) {

		Rental newRental = null;

		if ("vehicles".equals(type)) {
			newRental = rentalService.createRentalForVehicle(Integer.valueOf(id), rental, rental.getUserId());
			addHateoasLinksToRental(rental, Integer.valueOf(id), rental.getUserId()); // For vehicles

		} else if ("users".equals(type)) {
			newRental = rentalService.createRentalForVehicle(rental.getVehicleId(), rental, id);
			addHateoasLinksToRental(rental, rental.getVehicleId(), id);
			// Optionally add HATEOAS links for users
		}

		if (newRental == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorMessage("invalid input data", 400, null)).type(MediaType.APPLICATION_JSON).build();
		}

		return Response.status(Response.Status.CREATED).entity(newRental).build();
	}

	// Get details of a specific rental by its rentalId
	@GET
	@Path("/{rentalId}")
	@Secured
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getRental(@PathParam("type") String type, @PathParam("id") String id,
			@PathParam("rentalId") int rentalId) {
		Rental newRental = null;

		if ("vehicles".equals(type)) {

			newRental = rentalService.getRentalByIdForVehicle(Integer.valueOf(id), rentalId);
		} else if ("users".equals(type)) {
			newRental = rentalService.getRentalByIdForUser(id, rentalId);
		}
		if (newRental == null) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage("invalid input data", 400, null))
					.type(MediaType.APPLICATION_JSON).build();
		}

		return Response.ok(newRental).build();

	}

	// Update an existing rental for a specific vehicle
	@PUT
	@Path("/{rentalId}")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRental(@PathParam("type") String type, @PathParam("id") String id,
			@PathParam("rentalId") int rentalId, Rental updatedRental) {

		if (!securityContext.isUserInRole("admin") && !securityContext.isUserInRole("moderator")) {
			throw new WebApplicationException("Not authorized", 401);
		}
		Rental rental = null;
		if ("vehicles".equals(type)) {

			rental = rentalService.updateRentalForVehicle(Integer.valueOf(id), rentalId, updatedRental);
			addHateoasLinksToRental(rental, rental.getVehicleId(), rental.getUserId());
		} else if ("users".equals(type)) {
			rental = rentalService.updateRentalForUser(id, rentalId, updatedRental);
			addHateoasLinksToRental(rental, rental.getVehicleId(), rental.getUserId());
		}
		if (rental == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorMessage("Rental with ID " + rentalId + " not found", 404, null))
					.type(MediaType.APPLICATION_JSON).build();
		}

		return Response.ok(rental).build();

	}

	// Remove a rental by its rentalId
	@DELETE
	@Path("/{rentalId}")
	@Secured
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteRental(@PathParam("type") String type, @PathParam("id") String id,
			@PathParam("rentalId") int rentalId) {
		if (!securityContext.isUserInRole("admin")) {
			throw new WebApplicationException("Not authorized", 401);
		}
		boolean flag = false;
		if ("vehicles".equals(type)) {
			flag = rentalService.deleteRentalForVehicle(Integer.valueOf(id), rentalId);
		} else if ("users".equals(type)) {
			flag = rentalService.deleteRentalForUser(id, rentalId);
		}
		if (flag == false) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorMessage("Rental with ID " + rentalId + " not found", 404, null))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return Response.ok("Rental deleted").build();
	}

}
