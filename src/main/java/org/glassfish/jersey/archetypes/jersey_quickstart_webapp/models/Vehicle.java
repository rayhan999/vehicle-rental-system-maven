package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.paging.Link;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Vehicle {

	private int id;
	private String make;
	private String model;
	private int year;
	private double rentalPricePerDay;
	private boolean available;
	private List<Link> links = new ArrayList<>();

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLink(String url, String rel) {

		links.add(new Link(url, rel));
	}

	public void clearLinks() {
		links.clear();
	}

	// Default constructor
	public Vehicle() {
	}

	public Vehicle(int id, String make, String model, int year, double rentalPricePerDay, boolean available) {
		this.id = id;
		this.make = make;
		this.model = model;
		this.year = year;
		this.rentalPricePerDay = rentalPricePerDay;
		this.available = available;
	}

	// Getters and setters...
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getRentalPricePerDay() {
		return rentalPricePerDay;
	}

	public void setRentalPricePerDay(double rentalPricePerDay) {
		this.rentalPricePerDay = rentalPricePerDay;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
