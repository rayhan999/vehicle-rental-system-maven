package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.Vehicle;

@Singleton
public class VehicleService {

	private int nextId; 
	private static List<Vehicle> vehicleList;

	public VehicleService() {
		vehicleList = Collections.synchronizedList(new ArrayList<>());
		nextId = 1; 
	}

	public List<Vehicle> getAllVehicles() {
		return new ArrayList<>(vehicleList); // Return a copy 
	}

	public static Vehicle getVehicleById(int id) {
		return vehicleList.stream().filter(v -> v.getId() == id).findFirst().orElse(null); 
	}

	public Vehicle addVehicle(Vehicle vehicle) {
		
		if (vehicle.getMake() == null || vehicle.getMake().isEmpty() || vehicle.getModel() == null
				|| vehicle.getModel().isEmpty()) {
			return null;
		}
		vehicle.setId(nextId++); 
		vehicleList.add(vehicle);
		return vehicle;
	}

	public Vehicle updateVehicle(int id, Vehicle updatedVehicle) {
		Vehicle currentVehicle = getVehicleById(id);
		if (currentVehicle == null)
		{
			return null;
		}
		if (updatedVehicle.getMake() == null || updatedVehicle.getMake().isEmpty() || updatedVehicle.getModel() == null
				|| updatedVehicle.getModel().isEmpty()) {
			return null;
		}

		currentVehicle.setMake(updatedVehicle.getMake());
		currentVehicle.setModel(updatedVehicle.getModel());
		currentVehicle.setYear(updatedVehicle.getYear());
		currentVehicle.setRentalPricePerDay(updatedVehicle.getRentalPricePerDay());
		currentVehicle.setAvailable(updatedVehicle.isAvailable());
		return currentVehicle;
	}

	public static double calculateTotalCost(int vehicleId, String rentalDate, String returnDate) {
		Vehicle vehicle = getVehicleById(vehicleId); 
		double pricePerDay = vehicle.getRentalPricePerDay();

		
		LocalDate start = LocalDate.parse(rentalDate);
		LocalDate end = LocalDate.parse(returnDate);

		long rentalDays = ChronoUnit.DAYS.between(start, end);
		if (rentalDays < 1) {
			return -1;
		}

		return rentalDays * pricePerDay;
	}

	public boolean deleteVehicle(int id) {
		boolean deleted = vehicleList.removeIf(vehicle -> vehicle.getId() == id);
		
		return deleted;
	}
}