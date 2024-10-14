package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.Payment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class PaymentService {

//    {
//   "id":123,
//"rentalId":2,
//"paymentDate":"01-01-2021",
//"amount":2500.00,
//"paymentMethod":"cash"
//    }

	private List<Payment> paymentList = new ArrayList<>();
	private int currentId = 1;

	public List<Payment> getPaymentsByRentalId(int rentalId) {
		
		List<Payment> filteredPayments = new ArrayList<>();


		paymentList.stream().filter(payment -> payment.getRentalId() == rentalId).forEach(filteredPayments::add); 

		return filteredPayments; // Return the new list
	}

	public Payment getPaymentById(int rentalId, int paymentId) {
		return paymentList.stream().filter(payment -> payment.getRentalId() == rentalId && payment.getId() == paymentId)
				.findFirst().orElse(null);
	}

	public Payment createPayment(int rentalId, Payment payment) {
		payment.setRentalId(rentalId);
		payment.setId(currentId++);
		paymentList.add(payment);
		return payment;
	}

	public Payment updatePayment(int rentalId, int paymentId, Payment updatedPayment) {
		for (int i = 0; i < paymentList.size(); i++) {
			Payment payment = paymentList.get(i);
			if (payment.getRentalId() == rentalId && payment.getId() == paymentId) {
				payment.setPaymentDate(updatedPayment.getPaymentDate());
				payment.setAmount(updatedPayment.getAmount());
				payment.setPaymentMethod(updatedPayment.getPaymentMethod());
				return payment;
			}
		}
		return null;
	}

	public boolean deletePayment(int rentalId, int paymentId) {
		return paymentList.removeIf(payment -> payment.getRentalId() == rentalId && payment.getId() == paymentId);
	}
}
