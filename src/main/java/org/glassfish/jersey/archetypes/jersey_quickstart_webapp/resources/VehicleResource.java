package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.resources;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.ws.rs.*;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services.VehicleService;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.annotations.Secured;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.Vehicle;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.paging.VehicleFilterBean;

@Path("vehicles")
@Singleton
public class VehicleResource {

	VehicleService vehicleService = new VehicleService();
	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext securityContext;

	private void addHateoasLinksToVehicle(Vehicle vehicle, int vehicleId) {
		// Clear existing links to prevent duplicates
		vehicle.clearLinks();

		// Self-link (link to this specific vehicle)
		String selfUri = uriInfo.getBaseUriBuilder().path(VehicleResource.class).path(String.valueOf(vehicleId)).build()
				.toString();
		vehicle.addLink(selfUri, "self");

		// Link to rentals for this vehicle
		String rentalsUri = uriInfo.getBaseUriBuilder().path(RentalResource.class) // RentalResource class should be//
																					// Directly linking the method in
																					// RentalResource
				.resolveTemplate("id", vehicleId).resolveTemplate("type", "vehicles")// Resolve vehicleId in the URL
				.build().toString();
		vehicle.addLink(rentalsUri, "rentals");
	}

	@GET
	@Secured
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getVehicles(@BeanParam VehicleFilterBean fBean) {
		boolean isYearValid = fBean.getYear() > 0;
		boolean isMakeValid = fBean.getMake() != null && !fBean.getMake().isEmpty();
		boolean isAvailableValid = fBean.getAvilable();

		List<Vehicle> filteredVehicles = vehicleService.getAllVehicles(); // Start with all vehicles

		if (isYearValid) {
			filteredVehicles = filteredVehicles.stream().filter(vehicle -> vehicle.getYear() == fBean.getYear())
					.collect(Collectors.toList());
		}

		if (isMakeValid) {
			filteredVehicles = filteredVehicles.stream()
					.filter(vehicle -> vehicle.getMake().equalsIgnoreCase(fBean.getMake()))
					.collect(Collectors.toList());
		}

		if (isAvailableValid) {
			filteredVehicles = filteredVehicles.stream().filter(vehicle -> vehicle.isAvailable() == fBean.getAvilable())
					.collect(Collectors.toList());
		}

		// filteredVehicles.forEach(vehicle -> addHateoasLinksToVehicle(vehicle,
		// vehicle.getId(), uriInfo));
		return Response.ok(filteredVehicles).build();
	}

	@GET
	@Path("/{vehicleId}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicle(@PathParam("vehicleId") int id) {
		Vehicle vehicle = VehicleService.getVehicleById(id);

		if (vehicle == null) {
			throw new NotFoundException("Vehicle with ID " + id + " not found");
//			return Response.status(Response.Status.NOT_FOUND)
//					.entity(new ErrorMessage("Vehicle with ID " + id + " not found", 404))
//					.type(MediaType.APPLICATION_JSON).build();
		}
		addHateoasLinksToVehicle(vehicle, id);
		return Response.ok(vehicle).build();
	}

	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addVehicle(Vehicle vehicle) {
		if (!securityContext.isUserInRole("admin")) {
			throw new WebApplicationException("Not authorized", 401);
		}

		addHateoasLinksToVehicle(vehicle, vehicle.getId());
		Vehicle newVehicle = vehicleService.addVehicle(vehicle);
		if (newVehicle == null) {
			throw new NotFoundException("Vehicle data is wrong not found");
		}
		return Response.status(Response.Status.CREATED).entity(newVehicle).build();

	}

	@PUT
	@Path("/{vehicleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateVehicle(@PathParam("vehicleId") int id, Vehicle vehicle) {
		if (!securityContext.isUserInRole("admin") && !securityContext.isUserInRole("moderator")) {
			throw new WebApplicationException("Not authorized", 401);
		}
		Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicle);
		if (updatedVehicle == null) {
			throw new NotFoundException("Vehicle data is wrong not found");
		}
		return Response.ok(updatedVehicle).build();
	}

	@DELETE
	@Path("/{vehicleId}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteVehicle(@PathParam("vehicleId") int id) {

		if (!securityContext.isUserInRole("admin")) {
			throw new WebApplicationException("Not authorized", 401);
		}
		boolean flag = vehicleService.deleteVehicle(id);
		if (flag == false) {
			return Response.status(Response.Status.NOT_FOUND).entity("Couldn't delete, vehicle not found").build();
		}
		return Response.ok("Deleted").build();

	}
}
