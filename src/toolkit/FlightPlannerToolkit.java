/*
 * Group 6: Welcher, Rogers, Nguyen
 * FlightPlannerToolkit.java
 * This class contains utility methods for calculating costs
 * from one airport to another, calculating heading and cardinal
 * direction from one airport to another, and calculating time
 * spent in flight.
 */

package toolkit;

import data.Airport;

public class FlightPlannerToolkit {

	public FlightPlannerToolkit() {}

	// method calculates distance from starting airport to destination airport
	// using Haversine formula
	public static double calculateCost(Airport start, Airport destination) {
		double lat1 = start.getLatitude();
		double lon1 = start.getLongitude();
		double lat2 = destination.getLatitude();
		double lon2 = destination.getLongitude();
	    double radius = 6371; // radius of Earth in kilometers
	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLon/2) * Math.sin(dLon/2);
	    double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distance = radius * b; // distance in kilometers
	    return distance * 0.539957; // convert to nautical miles
	}
	
	// overloaded method which calculates cardinal direction based on starting
	// airport and destination airport
	public static String getCardinalDirection(Airport start, Airport destination) {
		int heading = getHeading(start, destination);

		switch (heading) {
		case 0:
			return "North";
		case 45:
			return "Northeast";
		case 90:
			return "East";
		case 135:
			return "Southeast";
		case 180:
			return "South";
		case 225:
			return "Southwest";
		case 270:
			return "West";
		case 315:
			return "Northwest";
		default:
			return "Invalid cardinal direction";
		}
	}
	
	// overloaded method which calculates cardinal direction based on starting
	// airport, destination airport, and given heading
	public static String getCardinalDirection(Airport start, Airport destination, int heading) {

		switch (heading) {
		case 0:
			return "North";
		case 45:
			return "Northeast";
		case 90:
			return "East";
		case 135:
			return "Southeast";
		case 180:
			return "South";
		case 225:
			return "Southwest";
		case 270:
			return "West";
		case 315:
			return "Northwest";
		default:
			return "Invalid cardinal direction";
		}
	}
	
	// method calculates initial heading radians from given starting
	// airport and destination airport
	public static double getHeadingRadians(Airport start, Airport destination) {
		double startLatRad = Math.toRadians(start.getLatitude());
		double startLongRad = Math.toRadians(start.getLongitude());
		double destinationLatRad = Math.toRadians(destination.getLatitude());
		double destinationLongRad = Math.toRadians(destination.getLongitude());

		double deltaLongRad = destinationLongRad - startLongRad;

		double y = Math.sin(deltaLongRad) * Math.cos(destinationLatRad);
		double x = Math.cos(startLatRad) * Math.sin(destinationLatRad)
				- Math.sin(startLatRad) * Math.cos(destinationLatRad) * Math.cos(deltaLongRad);

		return Math.atan2(y, x);
	}
	
	// overloaded method calculates heading based on starting airport
	// and destination airport
	public static int getHeading(Airport start, Airport destination) {
		double headingRadians = getHeadingRadians(start, destination);
		double heading = Math.toDegrees(headingRadians);
		heading = (heading + 360) % 360;
		
		return getNormalizedHeading(heading);
	}
	
	// overloaded method calculates heading based on given
	// initial heading radians
	public static int getHeading(double headingRadians) {
		double heading = Math.toDegrees(headingRadians);
		heading = (heading + 360) % 360;
		
		return getNormalizedHeading(heading);
	}
	
	// method calculates heading with no normalization based on
	// given initial heading radians
	public static double getPureHeading(double headingRadians) {
		double heading = Math.toDegrees(headingRadians);
		return (heading + 360) % 360;
	}
	
	// method converts heading to a normalized heading based
	// on switch cases in getCardinalDirection()
	public static int getNormalizedHeading(double heading) {
		return ((int) Math.round(heading / 45.0) * 45) % 360;
	}
	
	// method calculates time in hours based on given
	// distance and air speed
	public static double calculateTime(double distance, double airspeed) {
		return distance / airspeed;
	}
}
