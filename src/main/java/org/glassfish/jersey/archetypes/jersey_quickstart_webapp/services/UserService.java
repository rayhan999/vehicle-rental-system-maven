package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.models.User;
import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.utils.JwtUtils;

import com.google.inject.Inject;

@Singleton
public class UserService {

	public JwtUtils jwtUtil =new JwtUtils();
	private static final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

	public static List<User> getAllUsers() {
		return new ArrayList<>(users.values());
	}

	public static User getUserById(String username) {
		User user = users.get(username);
		if (user == null) {
			throw new WebApplicationException("User not found", Status.NOT_FOUND);
		}
		return user;
	}


	public  String authenticateUser(String username, String password) {
		User user = users.get(username);
		System.out.println("username is  : "+ username);
		if (user != null && user.getPassword().equals(password)) {
			return jwtUtil.generateToken(username); 
		}
		throw new WebApplicationException("Invalid credentials", Status.UNAUTHORIZED);
	}

	public User addUser(User user) {
		if (users.containsKey(user.getUserName())) {
			throw new WebApplicationException("Username already exists", Status.CONFLICT);
		}
		user.addRole("user");
		users.put(user.getUserName(), user);
		return user;
	}

	public User addUserRole(String username, String role) {
		User user = getUserById(username);
		user.addRole(role);
		return user;
	}

	public User updateUser(String userName, User updatedUser) {
		if (!users.containsKey(userName)) {
			throw new WebApplicationException("User not found", Status.NOT_FOUND);
		}

		// Ensure the username in the path matches the updated user
		if (!userName.equals(updatedUser.getUserName())) {
			throw new WebApplicationException("Username mismatch", Status.BAD_REQUEST);
		}

		// Update the user
		users.put(userName, updatedUser);
		return updatedUser;
	}

	public boolean deleteUser(String userName) {
		if (!users.containsKey(userName)) {
			throw new WebApplicationException("User not found", Status.NOT_FOUND);
		}
		User userCheck = users.remove(userName);
		if (userCheck != null) {
			return true;
		} else {
			return false;
		}

	}
}