package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.paging;

import javax.ws.rs.QueryParam;

public class VehicleFilterBean {

	
	 private @QueryParam("year") int year;
	 private @QueryParam("make") String make;
	 private @QueryParam("available") boolean available;
	 
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public boolean getAvilable() {
		return available;
	}
	public void setAvilable(boolean avilable) {
		this.available = avilable;
	}
	 
	 
	 
	
}
