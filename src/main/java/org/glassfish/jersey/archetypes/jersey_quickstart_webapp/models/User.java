package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.paging.Link;

@XmlRootElement
public class User implements Principal {

	private String userName;
	private String name;
	private String email;
	private String phoneNumber;
	private String password;
	private List<Link> links = new ArrayList<>();
	private Set<String> roles = new HashSet<>();

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
		links.clear(); // Clear all existing links
	}

	public User(String password, String userName, String name, String email, String phoneNumber) {
		this.password = password;
		this.userName = userName;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;

	}

	public User() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public void addRole(String role) {
		this.roles.add(role);
	}

}
