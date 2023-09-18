/*
 * Group 6: Welcher, Rogers, Nguyen
 * Airplane.java
 * This class creates fields, parameterized constructor, and get and set methods for Airplane objects.
 */

package data;

public class Airplane implements Comparable<Airplane> {
	
	private String tailNumber;
	private String make;
	private String model;
	private String type;
	private Double tankSize;	//size of fuel tank in liters
	private Double burnRate;	//liters/hr
	private Double airSpeed;	//knots
	private Double range;		//nautical miles
	private Double minRunwayLength; // in feet
	private String fuelType;
	
	//constructor
	public Airplane(String tailNumber , String make , String model , String type , double tankSize , double burnRate , double airSpeed,
			double minRunwayLength, String fuelType) {
		this.tailNumber = tailNumber;
		this.make = make;
		this.model = model;
		this.type = type;
		this.tankSize = tankSize;
		this.burnRate = burnRate;
		this.airSpeed = airSpeed;
		this.range = calculateRange();
		this.minRunwayLength = minRunwayLength;
		this.fuelType = fuelType;
	}
	
	private double calculateRange() {
		return tankSize / burnRate * airSpeed;
	}
	
	//get methods
	public String getTailNumber() {
		return tailNumber;
	}
	public String getMake() {
		return make;
	}
	public String getModel() {
		return model;
	}
	public String getType() {
		return type;
	}
	public Double getTankSize() {
		return tankSize;
	}
	public Double getBurnRate() {
		return burnRate;
	}
	public Double getAirSpeed() {
		return airSpeed;
	}
	public Double getRange() {
		return range;
	}
	public String getFuelType() {
		return fuelType;
	}
	public Double getMinRunwayLength() {
		return minRunwayLength;
	}
	
	//set methods
	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setTankSize(Double tankSize) {
		this.tankSize = tankSize;
	}
	public void setBurnRate(Double burnRate) {
		this.burnRate = burnRate;
	}
	public void setAirSpeed(Double airSpeed) {
		this.airSpeed = airSpeed;
	}
	public void setRange(Double range) {
		this.range = range;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public void setMinRunwayLength(Double minRunwayLength) {
		this.minRunwayLength = minRunwayLength;
	}
	
	//to string methods
	public String getTankSizeString() {
		return String.valueOf(this.tankSize);
	}
	public String getBurnRateString() {
		return String.valueOf(this.burnRate);
	}
	public String getAirSpeedString() {
		return String.valueOf(this.airSpeed);
	}
	public String getRangeString() {
		return String.valueOf(this.range);
	}
	public String getMinRunwayLengthString() {
		return String.valueOf(this.minRunwayLength);
	}
	
	@Override
	public String toString() {
		return "Tail number: " + tailNumber + "\n" + 
				"Make: " + make + "\n" + 
				"Model: " + model + "\n" + 
				"Type: " + type + "\n" + 
				"Tank size: " + tankSize + " liters\n" + 
				"Burn rate: " + burnRate + " liters / hr.\n" + 
				"Air speed: " + airSpeed + " Knots";
	}

	@Override
	public int compareTo(Airplane other) {
		return this.tailNumber.compareTo(other.tailNumber);
	}
}
