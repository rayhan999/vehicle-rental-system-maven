package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.Rental;

@Singleton
public class RentalService {
//	
//    {
//    "id":25,
//	"vehicleId":2,
//	"userId":1,
//	"rentalDate":"12-09-2021",
//	"returnDate":"25-10-2021",
//	"totalCost" : 125.00
//    }

	private List<Rental> rentalList = new ArrayList<>();

	public List<Rental> getAllRentals() {

		List<Rental> result = new ArrayList<>(rentalList); // Create a new ArrayList and copy all elements
		return result;

	}

	public List<Rental> getRentalsByUserId(String userName) {

		List<Rental> result = new ArrayList<>();
		for (Rental rental : rentalList) {
			if (rental.getUserId().equals(userName)) {
				result.add(rental);
			}
		}
		return result;
	}

	public List<Rental> getRentalsByVehicleId(int vehicleId) {

		List<Rental> result = new ArrayList<>();
		for (Rental rental : rentalList) {
			if (rental.getVehicleId() == vehicleId) {
				result.add(rental);
			}
		}
		return result;
	}

	public Rental createRentalForVehicle(int vehicleId, Rental rental, String userName) {
		if (VehicleService.getVehicleById(vehicleId) == null || !VehicleService.getVehicleById(vehicleId).isAvailable()
				|| UserService.getUserById(userName) == null) {
			return null;
		}
		rental.setVehicleId(vehicleId); // Associate the rental with the vehicle
		rental.setUserId(userName);
		rental.setId(getNextId()); // Generate a new ID
		rental.setTotalCost(
				VehicleService.calculateTotalCost(vehicleId, rental.getRentalDate(), rental.getReturnDate()));
		rentalList.add(rental);
		return rental;
	}

	public Rental getRentalByIdForVehicle(int vehicleId, int rentalId) {
		for (Rental rental : rentalList) {
			if (rental.getVehicleId() == vehicleId && rental.getId() == rentalId) {
				return rental;
			}
		}
		return null;
	}

	public Rental getRentalByIdForUser(String userName, int rentalId) {
		for (Rental rental : rentalList) {
			if (rental.getUserId().equals(userName) && rental.getId() == rentalId) {
				return rental;
			}
		}
		return null;
	}

	public Rental updateRentalForVehicle(int vehicleId, int rentalId, Rental updatedRental) {
		for (int i = 0; i < rentalList.size(); i++) {
			Rental rental = rentalList.get(i);
			if (rental.getVehicleId() == vehicleId && rental.getId() == rentalId) {
				rentalList.set(i, updatedRental); // Replace the rental with the updated one
				return updatedRental;
			}
		}
		return null;
	}

	public Rental updateRentalForUser(String userName, int rentalId, Rental updatedRental) {
		for (int i = 0; i < rentalList.size(); i++) {
			Rental rental = rentalList.get(i);
			if (rental.getUserId().equals(userName) && rental.getId() == rentalId) {
				rentalList.set(i, updatedRental); // Replace the rental with the updated one
				return updatedRental;
			}
		}
		return null;
	}

	public boolean deleteRentalForVehicle(int vehicleId, int rentalId) {
		return rentalList.removeIf(rental -> rental.getVehicleId() == vehicleId && rental.getId() == rentalId);
	}

	public boolean deleteRentalForUser(String userName, int rentalId) {
		return rentalList.removeIf(rental -> rental.getUserId().equals(userName) && rental.getId() == rentalId);
	}

	private int getNextId() {

		return rentalList.size() + 1;
	}
}
