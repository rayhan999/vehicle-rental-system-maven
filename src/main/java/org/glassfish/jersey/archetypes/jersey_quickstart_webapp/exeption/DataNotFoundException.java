package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.exeption;

public class DataNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7804257417958026582L;

	/**
	 * 
	 */
	//private static final long serialVersionUID = -6672553621676928689L;
	
	public DataNotFoundException(String message){
		
		super(message);
		
	} 
	
	
}
