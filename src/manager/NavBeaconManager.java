/*
 * Group 6: Welcher, Rogers, Nguyen
 * NavBeaconManager.java
 * This class implements display, add, modify, and delete methods
 * for user by the NavBeaconUI class.
 */

package manager;

import java.util.HashMap;
import javax.swing.JOptionPane;

import data.NavBeacon;

public class NavBeaconManager {

	// method validates passed information and displays nav beacon information
	public void display(HashMap<String, NavBeacon> icaoToBeacon, String input) {
		String icaoKey = input.toUpperCase();
		if (icaoToBeacon.containsKey(icaoKey)) {
			NavBeacon beacon = icaoToBeacon.get(icaoKey);
			String showInfo = "ICAO: " + beacon.getIcao() + "\n"
							+ "Name: " + beacon.getName() + "\n"
							+ "Latitude: " + beacon.getLatitude() + "\n"
							+ "Longitude: " + beacon.getLongitude() + "\n"
							+ "Type: " + beacon.getType();
			JOptionPane.showMessageDialog(null, showInfo, "NavBeacon Information: " + beacon.getName(), JOptionPane.PLAIN_MESSAGE);
		}
		else
			JOptionPane.showMessageDialog(null, "NavBeacon not in database.", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	// method instantiates an nav beacon object then adds it to the HashMap
	public void add(HashMap<String, NavBeacon> icaoToBeacon, String[] inputs) {
		NavBeacon beacon = new NavBeacon(inputs[0], inputs[1], Double.valueOf(inputs[2]),
				Double.valueOf(inputs[3]), inputs[4]);
		icaoToBeacon.put(beacon.getIcao(), beacon);
	}
	
	// method instantiates an nav beacon object then either replaces the value in HashMap
	// or deletes the previous entry and adds the new entry to the HashMap
	public void modify(HashMap<String, NavBeacon> icaoToBeacon, String[] inputs, String initialIcao) {
		NavBeacon beacon = new NavBeacon(inputs[0], inputs[1], Double.valueOf(inputs[2]),
				Double.valueOf(inputs[3]), inputs[4]);
		if(beacon.getIcao().equals(initialIcao))
			icaoToBeacon.replace(beacon.getIcao(), beacon);
		else {
			icaoToBeacon.remove(initialIcao);
			icaoToBeacon.put(beacon.getIcao(), beacon);
		}
	}
	
	// method prompts user for deletion, then deletes entry from HashMap
	public void delete(HashMap<String, NavBeacon> icaoToBeacon, String input) {
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ICAO: " + input + "?",
				"Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (confirm == JOptionPane.YES_OPTION)
			icaoToBeacon.remove(input);
		else if (confirm == JOptionPane.NO_OPTION)
			return;
	}
}
