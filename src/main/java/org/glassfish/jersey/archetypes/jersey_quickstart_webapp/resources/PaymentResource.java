package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.resources;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.model.ErrorMessage;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.Payment;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services.PaymentService;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.List;

@Path("/vehicles/{vehicleId}/rentals/{rentalId}/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class PaymentResource {

	private PaymentService paymentService = new PaymentService();

	@Context
	private UriInfo uriInfo;

	private void addHateoasLinksToPayment(Payment payment, int vehicleId, int rentalId) {
		payment.clearLinks();
		// Link to the rental
		String rentalUrl = uriInfo.getBaseUriBuilder().path(RentalResource.class)
				.path(RentalResource.class, "getRental").resolveTemplate("vehicleId", vehicleId)
				.resolveTemplate("rentalId", rentalId).build().toString();
		payment.addLink(rentalUrl, "rental");

		// Link to the vehicle associated with this rental
		String vehicleUrl = uriInfo.getBaseUriBuilder().path(VehicleResource.class).path(String.valueOf(vehicleId))
				.build().toString();
		payment.addLink(vehicleUrl, "vehicle");

		// Self-link (link to this specific payment)
		String selfUrl = uriInfo.getBaseUriBuilder().path(PaymentResource.class)
				.path(PaymentResource.class, "getPayment").resolveTemplate("vehicleId", vehicleId)
				.resolveTemplate("rentalId", rentalId).resolveTemplate("paymentId", payment.getId()).build().toString();
		payment.addLink(selfUrl, "self");
	}

	@GET
	@Path("/vehicles/{vehicleId}/rentals/{rentalId}/payments")
	public Response getPaymentsByVehicle(@PathParam("vehicleId") int vehicleId, @PathParam("rentalId") int rentalId) {
		List<Payment> payments = paymentService.getPaymentsByRentalId(rentalId);
		// payments.forEach(payment -> addHateoasLinksToPayment(payment, vehicleId,
		// rentalId));
		return Response.ok(payments).build();
	}

	// GET all payments for a rental
	@GET
	public Response getPayments(@PathParam("vehicleId") int vehicleId, @PathParam("rentalId") int rentalId) {
		List<Payment> payments = paymentService.getPaymentsByRentalId(rentalId);
		// payments.forEach(payment -> addHateoasLinksToPayment(payment, vehicleId,
		// rentalId));
		return Response.ok(payments).build();
	}

	// GET a specific payment for a rental
	@GET
	@Path("/{paymentId}")
	public Response getPayment(@PathParam("rentalId") int rentalId, @PathParam("paymentId") int paymentId,
			@PathParam("vehicleId") int vehicleId) {

		Payment payment = paymentService.getPaymentById(rentalId, paymentId);
		if (payment == null) {
			throw new NotFoundException("Payment with ID " + paymentId + " not found");
		}
		// addHateoasLinksToPayment(payment, vehicleId, rentalId);
		return Response.ok(payment).build();

	}

	// POST a new payment for a rental
	@POST
	public Response createPayment(@PathParam("rentalId") int rentalId, @PathParam("vehicleId") int vehicleId,
			Payment payment) {

		Payment newPayment = paymentService.createPayment(rentalId, payment);
		if (newPayment == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessage("couldn't create", 400, null))
					.type(MediaType.APPLICATION_JSON).build();
		}
		addHateoasLinksToPayment(payment, vehicleId, rentalId);
		return Response.status(Response.Status.CREATED).entity(newPayment).build();

	}

	// PUT - update an existing payment
	@PUT
	@Path("/{paymentId}")
	public Response updatePayment(@PathParam("rentalId") int rentalId, @PathParam("paymentId") int paymentId,
			Payment payment) {

		Payment updatedPayment = paymentService.updatePayment(rentalId, paymentId, payment);
		if (updatedPayment == null) {
			throw new NotFoundException("Payment with ID " + paymentId + " not found");
		}
		return Response.ok(updatedPayment).build();

	}

	// DELETE - delete a specific payment
	@DELETE
	@Path("/{paymentId}")
	public Response deletePayment(@PathParam("rentalId") int rentalId, @PathParam("paymentId") int paymentId) {

		boolean deleted = paymentService.deletePayment(rentalId, paymentId);
		if (!deleted) {
			throw new NotFoundException("Payment with ID " + paymentId + " not found");
		}
		return Response.status(Response.Status.NO_CONTENT).build();
	}
}
