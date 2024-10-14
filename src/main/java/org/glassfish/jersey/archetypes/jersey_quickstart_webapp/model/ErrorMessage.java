package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {

	private String errorMessage;
	private int errorCode;          //own error code  
	private String documentation;   //link to documentation regarding a error and resolving issues
	
	
	public ErrorMessage(){
		
		
		
	}
	
	public ErrorMessage(String errorMessage, int errorCode, String documentation) {
		super();
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.documentation = documentation;
	}




	public String getErrorMessage() {
		return errorMessage;
	}




	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}




	public int getErrorCode() {
		return errorCode;
	}




	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}




	public String getDocumentation() {
		return documentation;
	}




	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	
	
	
	
	
	
	
	
}
