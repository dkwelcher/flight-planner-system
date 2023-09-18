/*
 * Group 6: Welcher, Rogers, Nguyen
 * Airport.java
 * This class creates fields, parameterized constructor, and get and set methods for Airport objects.
 */

package data;

public class Airport implements Comparable<Airport> {
	
	private String icao;
	private String name;
	private String city;
	private String state; // interchangeable with country
	private double latitude;
	private double longitude;
	private String radioType;
	private double radioFreq;
	private double maxRunwayLength; // in feet
	private boolean avgas;
	private boolean jaa;
	
	public Airport(String icao, String name, String city, String state, double latitude, double longitude,
				   String radioType, double radioFreq, double maxRunwayLength, boolean avgas, boolean jaa) {
		
		this.icao = icao;
		this.name = name;
		this.city = city;
		this.state = state;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radioType = radioType;
		this.radioFreq = radioFreq;
		this.maxRunwayLength = maxRunwayLength;
		this.avgas = avgas;
		this.jaa = jaa;
	}
	
	//get methods
	public String getIcao() {
		return icao;
	}
	public String getName() {
		return name;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public String getRadioType() {
		return radioType;
	}
	public double getRadioFreq() {
		return radioFreq;
	}
	public double getMaxRunwayLength() {
		return maxRunwayLength;
	}
	public boolean isAvgas() {
		return avgas;
	}
	public boolean isJaa() {
		return jaa;
	}
	
	//set methods
	public void setIcao(String icao) {
		this.icao = icao;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setRadioType(String radioType) {
		this.radioType = radioType;
	}
	public void setRadioFreq(double radioFreq) {
		this.radioFreq = radioFreq;
	}
	public void setMaxRunwayLength(int maxRunwayLength) {
		this.maxRunwayLength = maxRunwayLength;
	}
	public void setAvgas(boolean avgas) {
		this.avgas = avgas;
	}
	public void setJaa(boolean jaa) {
		this.jaa = jaa;
	}
	
	//methods to return string values
	public String getLatitudeString() {
		return String.valueOf(this.latitude);
	}
	public String getLongitudeString() {
		return String.valueOf(this.longitude);
	}
	public String getRadioFreqString() {
		return String.valueOf(this.radioFreq);
	}
	public String getMaxRunwayLengthString() {
		return String.valueOf(this.maxRunwayLength);
	}
	public String getAvgasString() {
		return this.avgas ? "Yes" : "No";
	}
	public String getJaaString() {
		return this.jaa ? "Yes" : "No";
	}

	@Override
	public int compareTo(Airport other) {
		return this.name.compareTo(other.name);
	}
}
