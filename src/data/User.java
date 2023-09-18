/*
 * Group 6: Welcher, Rogers, Nguyen
 * User.java
 * This class creates fields, parameterized constructor, and get and set methods for User objects.
 */

package data;

import admin.AccessLevel;

public class User {

	private String username;
	private String password;
	private AccessLevel accessLevel;
	
	public User(String username, String password, AccessLevel accessLevel) {
		this.username = username;
		this.password = password;
		this.accessLevel = accessLevel;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public AccessLevel getAccessLevel() {
		return accessLevel;
	}
}
