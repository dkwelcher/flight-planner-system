/*
 * Group 6: Welcher, Rogers, Nguyen
 * AirplaneManager.java
 * This class implements display, add, modify, and delete methods
 * for user by the AirplaneUI class.
 */

package manager;

import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.JOptionPane;

import data.Airplane;

public class AirplaneManager {

	// method validates passed information and displays airplane information
	public void display(HashMap<String, Airplane> tailNumberToAirplane, String input) {
		DecimalFormat df = new DecimalFormat("#.##");
		String tailNum = input.toUpperCase();
		if (tailNumberToAirplane.containsKey(tailNum)) {
			Airplane airplane = tailNumberToAirplane.get(tailNum);
			String showInfo = "Tail ID: " + airplane.getTailNumber() + "\n"
							+ "Make: " + airplane.getMake() + "\n"
							+ "Model: " + airplane.getModel() + "\n"
							+ "Type: " + airplane.getType() + "\n"
							+ "Fuel Type: " + airplane.getFuelType().toUpperCase() + "\n"
							+ "Tank Size: " + df.format(airplane.getTankSize()) + " liters" + "\n"
							+ "Burn Rate: " + df.format(airplane.getBurnRate()) + " liters/hour" + "\n"
							+ "Air Speed: " + df.format(airplane.getAirSpeed()) + " knots" + "\n"
							+ "Range: " + df.format(airplane.getRange()) + " nautical miles";
			JOptionPane.showMessageDialog(null, showInfo, "Airplane Information: " + airplane.getMake() + " " + airplane.getModel(), JOptionPane.PLAIN_MESSAGE);
		}
		else
			JOptionPane.showMessageDialog(null, "Airplane not in database.", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	// method instantiates an airplane object then adds it to the HashMap
	public void add(HashMap<String, Airplane> tailNumberToAirplane, String[] inputs) {
		Airplane airplane = new Airplane(inputs[0], inputs[1], inputs[2], inputs[3],
				Double.valueOf(inputs[4]), Double.valueOf(inputs[5]), Double.valueOf(inputs[6]),
				Double.valueOf(inputs[7]), inputs[8]);
		tailNumberToAirplane.put(airplane.getTailNumber(), airplane);
	}
	
	// method instantiates an airplane object then either replaces the value in HashMap
	// or deletes the previous entry and adds the new entry to the HashMap
	public void modify(HashMap<String, Airplane> tailNumberToAirplane, String[] inputs, String initialTailNum) {
		Airplane airplane = new Airplane(inputs[0], inputs[1], inputs[2], inputs[3],
				Double.valueOf(inputs[4]), Double.valueOf(inputs[5]), Double.valueOf(inputs[6]),
				Double.valueOf(inputs[7]), inputs[8]);
		if(airplane.getTailNumber().equals(initialTailNum))
			tailNumberToAirplane.replace(airplane.getTailNumber(), airplane);
		else {
			tailNumberToAirplane.remove(initialTailNum);
			tailNumberToAirplane.put(airplane.getTailNumber(), airplane);
		}
	}
	
	// method prompts user for deletion, then deletes entry from HashMap
	public void delete(HashMap<String, Airplane> tailNumberToAirplane, String input) {
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Tail ID: " + input + "?",
				"Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (confirm == JOptionPane.YES_OPTION)
			tailNumberToAirplane.remove(input);
		else if (confirm == JOptionPane.NO_OPTION)
			return;
	}
}
