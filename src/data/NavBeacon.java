/*
 * Group 6: Welcher, Rogers, Nguyen
 * NavBeacon.java
 * This class creates fields, parameterized constructor, and get and set methods for NavBeacon objects.
 */

package data;

public class NavBeacon implements Comparable<NavBeacon> {
	
	//make attributes
	private String icao;
	private String name;
	private double latitude;
	private double longitude;
	private String type;
	
	//constructor
	public NavBeacon(String icao, String name, double latitude, double longitude, String type) {
		
		this.icao = icao;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
	}
	
	//get methods
	public String getIcao() {
		return icao;
	}
	public String getName() {
		return name;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public String getType() {
		return this.type;
	}
	
	//set methods
	public void setIcao(String icao) {
		this.icao = icao;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	//to string methods
	public String getLatitudeString() {
		return String.valueOf(this.latitude);
	}
	public String getLongitudeString() {
		return String.valueOf(this.longitude);
	}
	
	@Override
	public String toString() {
		return "ICAO: " + icao + "\n"
				+ "Name: " + name + "\n"
				+ "Latitude: " + latitude + "\n"
				+ "Longitude: " + longitude + "\n"
				+ "Type: " + type;
	}

	@Override
	public int compareTo(NavBeacon other) {
		return this.name.compareTo(other.name);
	}
}
