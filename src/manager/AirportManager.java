/*
 * Group 6: Welcher, Rogers, Nguyen
 * AirportManager.java
 * This class implements display, add, modify, and delete methods
 * for user by the AirportUI class.
 */

package manager;

import java.util.HashMap;
import javax.swing.JOptionPane;

import data.Airport;

public class AirportManager {
	
	// method validates passed information and displays airport information
	public void display(HashMap<String, Airport> icaoToAirport, String input) {
		String icaoKey = input.toUpperCase();
		if (icaoToAirport.containsKey(icaoKey)) {
			Airport airport = icaoToAirport.get(icaoKey);
			String aavgasInfo = airport.isAvgas() ? "Yes" : "No";
			String jaaInfo = airport.isJaa() ? "Yes" : "No";
			String showInfo = "ICAO: " + airport.getIcao() + "\n"
							+ "Name: " + airport.getName() + "\n"
							+ "City: " + airport.getCity() + "\n"
							+ "State/Country: " + airport.getState() + "\n"
							+ "Latitude: " + airport.getLatitude() + "\n"
							+ "Longitude: " + airport.getLongitude() + "\n"
							+ "Radio Type: " + airport.getRadioType() + "\n"
							+ "Radio Frequency: " + airport.getRadioFreq() + "\n"
							+ "Max Runway Length (ft): " + airport.getMaxRunwayLength() + "\n"
							+ "AAVGAS Available: " + aavgasInfo + "\n"
							+ "JAa Available: " + jaaInfo;
			JOptionPane.showMessageDialog(null, showInfo, "Airport Information: " + airport.getName(), JOptionPane.PLAIN_MESSAGE);
		}
		else
			JOptionPane.showMessageDialog(null, "Airport not in database.", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	// method instantiates an airport object then adds it to the HashMap
	public void add(HashMap<String, Airport> icaoToAirport, String[] inputs) {
		Airport airport = new Airport(inputs[0], inputs[1], inputs[2], inputs[3], Double.valueOf(inputs[4]),
				Double.valueOf(inputs[5]), inputs[6], Double.valueOf(inputs[7]),
				Double.valueOf(inputs[8]), Boolean.valueOf(inputs[9]), Boolean.valueOf(inputs[10]));
		icaoToAirport.put(airport.getIcao(), airport);
	}
	
	// method instantiates an airport object then either replaces the value in HashMap
	// or deletes the previous entry and adds the new entry to the HashMap
	public void modify(HashMap<String, Airport> icaoToAirport, String[] inputs, String initialIcao) {
		Airport airport = new Airport(inputs[0], inputs[1], inputs[2], inputs[3], Double.valueOf(inputs[4]),
				Double.valueOf(inputs[5]), inputs[6], Double.valueOf(inputs[7]),
				Double.valueOf(inputs[8]), Boolean.valueOf(inputs[9]), Boolean.valueOf(inputs[10]));
		if(airport.getIcao().equals(initialIcao))
			icaoToAirport.replace(airport.getIcao(), airport);
		else {
			icaoToAirport.remove(initialIcao);
			icaoToAirport.put(airport.getIcao(), airport);
		}
	}
	
	// method prompts user for deletion, then deletes entry from HashMap
	public void delete(HashMap<String, Airport> icaoToAirport, String input) {
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ICAO: " + input + "?",
				"Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (confirm == JOptionPane.YES_OPTION)
			icaoToAirport.remove(input);
		else if (confirm == JOptionPane.NO_OPTION)
			return;
	}
}
