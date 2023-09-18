/*
 * Group 6: Welcher, Rogers, Nguyen
 * ValidationToolkit.java
 * This class contains utility methods for input validation.
 */

package toolkit;

import java.util.*;
import javax.swing.*;

import data.Airplane;
import data.Airport;
import data.NavBeacon;
import data.User;

public class ValidationToolkit {

	// method validates input for login and displays error messages when appropriate
	public static boolean getLoginValidation(HashMap<String, User> usernameToUser, String username, String password) {

		User user = null;
		if (usernameToUser.containsKey(username)) {
			user = usernameToUser.get(username);
			if (user.getPassword().equals(password))
				return true;
			else
				JOptionPane.showMessageDialog(null, "Password does not match.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else
			JOptionPane.showMessageDialog(null, "Username does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
		return false;
	}

	// method validates input for registration and displays error message when appropriate
	public static boolean getRegistrationValidation(String firstName, String lastName, String password, String confirmPassword) {

		StringBuilder failedCheckpoints = new StringBuilder();
		String firstNameFailed = "First name must only contain letters.";
		String lastNameFailed = "Last name must only contain letters and hyphens.";
		String passwordFailed = "Password must be at least six characters "
				+ "and contain at least one digit and one letter. " + "No spaces.";
		String confirmPasswordFailed = "Passwords do not match";

		boolean fieldFailed = false;

		boolean isFirstNameValid = validateLettersOnly(firstName);
		boolean isLastNameValid = validateLettersOnly(lastName);
		boolean isPasswordValid = validatePassword(confirmPassword);
		boolean isPasswordConfirmed = password.equals(confirmPassword);

		boolean[] registrationBooleans = { isFirstNameValid, isLastNameValid, isPasswordValid, isPasswordConfirmed };

		String[] registrationValidationStrings = { firstNameFailed, lastNameFailed, passwordFailed, confirmPasswordFailed };

		for (int i = 0; i < registrationBooleans.length; i++) {
			if (!registrationBooleans[i]) {
				failedCheckpoints.append(registrationValidationStrings[i] + "\n");
				fieldFailed = true;
			}
		}

		if (fieldFailed)
			JOptionPane.showMessageDialog(null, failedCheckpoints, "Error", JOptionPane.ERROR_MESSAGE);
		return isFirstNameValid && isLastNameValid && isPasswordValid && isPasswordConfirmed;
	}

	// method validates that input contains only letters
	public static boolean validateLettersOnly(String text) {
		if (text.equals(""))
			return false;
		text = text.replaceAll("\\s", "");
		text = text.replaceAll("-", "");
		for (int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			if (!Character.isLetter(c)) {
				return false; // non-letter detected: fail
			}
		}
		return true; // only letters: pass
	}

	// method validates that input meets or exceeds a count based on comparison passed
	private static boolean validateCount(String text, int limit, String comparison) {
		int count = 0;
		for (int i = 0; i < text.length(); ++i) {
			if (text.charAt(i) != ' ')
				++count;
		}
		if (comparison.equals(">="))
			return count >= limit;
		else if (comparison.equals("=="))
			return count == limit;
		else
			return false;
	}

	// method validates that password meets requirements
	private static boolean validatePassword(String password) {
		final int LIMIT = 6;
		final String COMPARISON = ">=";
		if (!validateCount(password, LIMIT, COMPARISON))
			return false;
		else if (!validateNoSpaces(password))
			return false;
		else if (!validateAnyDigit(password))
			return false;
		else if (!validateAnyLetter(password))
			return false;
		else
			return true;
	}

	// method validates that input contains no spaces
	private static boolean validateNoSpaces(String text) {
		for (int i = 0; i < text.length(); ++i) {
			if (text.charAt(i) == ' ')
				return false; // space detected
		}
		return true; // no spaces detected
	}

	// method validates that input has at least one digit
	private static boolean validateAnyDigit(String text) {
		for (int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			if (Character.isDigit(c)) {
				return true; // digit detected
			}
		}
		return false; // no digit detected
	}

	// method validates that input has at least one letter
	private static boolean validateAnyLetter(String text) {
		for (int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			if (Character.isLetter(c)) {
				return true; // letter detected
			}
		}
		return false; // no letter detected
	}

	// method validates that input exists in HashMap
	public static boolean validateAirportIcao(HashMap<String, Airport> icaoToAirport, String input) {
		if (input == null)
			return false;
		if (!input.equals("")) {
			input = input.toUpperCase();
			if (icaoToAirport.containsKey(input))
				return true;
			else {
				JOptionPane.showMessageDialog(null, input + " doesn't exist in database.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null, "No ICAO code entered.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	// method validates that input exists in HashMap
	public static boolean validateBeaconIcao(HashMap<String, NavBeacon> icaoToBeacon, String input) {
		if (input == null)
			return false;
		if (!input.equals("")) {
			input = input.toUpperCase();
			if (icaoToBeacon.containsKey(input))
				return true;
			else {
				JOptionPane.showMessageDialog(null, input + " doesn't exist in database.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null, "No ICAO code entered.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	// method validates that input exists in HashMap
	public static boolean validateAirplaneNumber(HashMap<String, Airplane> tailNumToBeacon, String input) {
		if (input == null)
			return false;
		if (!input.equals("")) {
			if (tailNumToBeacon.containsKey(input))
				return true;
			else {
				JOptionPane.showMessageDialog(null, input + " doesn't exist in database.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null, "No Tail # code entered.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	// method validates that all inputs meet requirements and displays error message when appropriate
	public static boolean validateAddAirport(String[] inputs) {
		Boolean[] isValid = new Boolean[inputs.length];
		Double[] lowerLimits = { 3.0, 1.0, 1.0, 1.0, -90.0, -180.0, 1.0, 118.0, 1200.0 };
		Double[] upperLimits = { 4.0, 75.0, 50.0, 50.0, 90.0, 180.0, 20.0, 136.975, 20000.0 };
		String[] failures = { "ICAO field must be between 3 to 4 letters.",
							  "Name field must be between 1 to 75 letters.",
							  "City field must be between 1 to 50 letters.",
							  "State/Country field must be between 1 to 50 letters.",
							  "Latitude field must be between -90 to 90 degrees.",
							  "Longitude field must be between -180 to 180 degrees.",
							  "Radio Type field must be between 1 to 20 letters.",
							  "Radio Frequency field must be between 118.000 to 136.975 MHz.",
							  "Runway Length field must be between 1200 to 20000 feet.",
							  "AVGAS selection must be chosen.",
						      "JAa selection must be chosen."
							  };
		Boolean valid = true;
		StringBuilder failedCheckpoints = new StringBuilder();

		for (int i = 0; i < inputs.length; i++) {
			if (i == 0 || i == 6)
				isValid[i] = isLetterOnlyTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);
			else if (i == 1 || i == 2 || i == 3)
				isValid[i] = isLetterOnlyTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);
			else if (i == 4 || i == 5 || i == 7 || i == 8)
				isValid[i] = isDigitOnlyTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);
			else if (i > 8)
				isValid[i] = isButtonSelected(inputs[i]);

			if (!isValid[i]) {
				valid = false;
				failedCheckpoints.append(failures[i] + "\n");
			}
		}
		if (!valid)
			JOptionPane.showMessageDialog(null, failedCheckpoints, "Error", JOptionPane.ERROR_MESSAGE);

		return valid;
	}
	
	// method validates that input matches the initial ICAO passed in
	public static boolean validateModifyAirport(HashMap<String, Airport> icaoToAirport, String[] inputs, String initialIcao) {
	    if (inputs[0].equals(initialIcao)) {
	        return true;
	    }
	    else if (icaoToAirport.containsKey(inputs[0])) {
	        JOptionPane.showMessageDialog(null, "ICAO already exists in database.", "Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    else
	        return true;
	}

	// method validates that all inputs meet requirements and displays error message when appropriate
	public static boolean validateAddBeacon(String[] inputs) {
		Boolean[] isValid = new Boolean[inputs.length];
		Double[] lowerLimits = { 3.0, 1.0, -90.0, -180.0, 1.0 };
		Double[] upperLimits = { 4.0, 50.0, 90.0, 180.0, 50.0 };
		String[] failures = { "ICAO field must be between 3 to 4 letters.",
							  "Name field must be between 1 to 50 letters.",
							  "Latitude field must be between -90 to 90 degrees.",
							  "Longitude field must be between -180 to 180 degrees.",
							  "Type field must be between 1 to 50 letters." };
		boolean valid = true;
		StringBuilder failedCheckpoints = new StringBuilder();

		for (int i = 0; i < failures.length; i++) {
			if (i == 0 || i == 4)
				isValid[i] = isLetterOnlyTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);
			else if (i == 1)
				isValid[i] = isTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);
			else if (i == 2 || i == 3)
				isValid[i] = isDigitOnlyTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);

			if (!isValid[i]) {
				failedCheckpoints.append(failures[i] + "\n");
				valid = false;
			}
		}
		if (!valid)
			JOptionPane.showMessageDialog(null, failedCheckpoints, "Error", JOptionPane.ERROR_MESSAGE);

		return valid;
	}
	
	// method validates that input matches the inital ICAO passed in
	public static boolean validateModifyBeacon(HashMap<String, NavBeacon> icaoToBeacon, String[] inputs, String initialIcao) {
		if (inputs[0].equals(initialIcao))
			return true;
		else if (icaoToBeacon.containsKey(inputs[0])) {
			JOptionPane.showMessageDialog(null, "ICAO already exists in database.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
			return true;
	}

	// method validates that all inputs meet requirements and displays error message when appropriate
	public static boolean validateAddAirplane(String[] inputs) {
		Boolean[] isValid = new Boolean[inputs.length];
		Double[] lowerLimits = { 2.0, 1.0, 1.0, 1.0, 200.0, 1.0, 1.0, 800.0 };
		Double[] upperLimits = { 6.0, 50.0, 50.0, 50.0, 350000.0, 20000.0, 2000.0, 15000.0 };
		String[] failures = { "Tail # field must be between 2 to 6 characters.",
				"Make field must be between 1 to 50 letters.",
				"Model field must be between 1 to 50 characters.",
				"Type field must be between 1 to 50 letters.",
				"Tank size field must be between 200 and 350000 liters.",
				"Burn rate field must be between 1 and 20000 in liters/hour.",
				"Air speed field must be between 1 and 2000 in knots.",
				"Min Runway Range field must be between 800 and 15000 feet.",
				"Fuel Type must be selected."};
		boolean valid = true;
		StringBuilder failedCheckpoints = new StringBuilder();

		for (int i = 0; i < failures.length; i++) {
			if (i == 0 || i == 2)
				isValid[i] = isTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);
			else if (i == 1 || i == 3)
				isValid[i] = isLetterOnlyTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);
			else if (i >= 4 && i <= 7)
				isValid[i] = isDigitOnlyTextfieldValid(inputs[i], lowerLimits[i], upperLimits[i]);
			else if (i == 8)
				isValid[i] = isButtonSelected(inputs[i]);

			if (!isValid[i]) {
				valid = false;
				failedCheckpoints.append(failures[i] + "\n");
			}
		}

		if (!valid)
			JOptionPane.showMessageDialog(null, failedCheckpoints, "Error", JOptionPane.ERROR_MESSAGE);

		return valid;
	}
	
	// method validates the input matches initial tail number passed in
	public static boolean validateModifyAirplane(HashMap<String, Airplane> tailNumToAirplane, String[] inputs, String initialTailNum) {
		if (inputs[0].equals(initialTailNum))
			return true;
		else if (tailNumToAirplane.containsKey(inputs[0])) {
			JOptionPane.showMessageDialog(null, "Tail # already exists in database.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
			return true;
	}

	// method validates letter-only entries by checking if a string is blank and if
	// a string contains only letters
	private static boolean isLetterOnlyTextfieldValid(String input, double lowerLimit, double upperLimit) {
		if (input.equals(""))
			return false;
		int count = countCharacterValidation(input);
		if (!isOnlyLetters(input))
			return false;
		else if (count < lowerLimit || count > upperLimit)
			return false;
		else
			return true;
	}

	// method validates a string that may contain letters and digits
	private static boolean isTextfieldValid(String input, double lowerLimit, double upperLimit) {
		if (input.equals(""))
			return false;
		int count = countCharacterValidation(input);
		if (count < lowerLimit || count > upperLimit)
			return false;
		else
			return true;
	}

	// method validates digit-only entries by checking if a string is blank and if a
	// string contains only digit
	private static boolean isDigitOnlyTextfieldValid(String input, double lowerLimit, double upperLimit) {
		if (input.equals(""))
			return false;
		if (!isOnlyDigits(input))
			return false;
		double digit = Double.parseDouble(input);
		if (digit < lowerLimit || digit > upperLimit)
			return false;
		else
			return true;
	}

	// method returns count of characters in String
	private static int countCharacterValidation(String input) {
		int count = 0;
		for (int i = 0; i < input.length(); ++i) {
			if (input.charAt(i) != ' ')
				++count;
		}
		return count;
	}

	// methods checks if String only contains digits
	private static boolean isOnlyDigits(String input) {
		input = input.replaceAll("\\.", "");
		input = input.replaceAll("\\-", "");
		for (int i = 0; i < input.length(); ++i) {
			char c = input.charAt(i);
			if (!Character.isDigit(c)) {
				return false; // non-digit detected: fail
			}
		}
		return true; // only digits: pass
	}

	// method checks if String contains only letters
	private static boolean isOnlyLetters(String input) {
		input = input.replaceAll("\\s", "");
		for (int i = 0; i < input.length(); ++i) {
			char c = input.charAt(i);
			if (!Character.isLetter(c)) {
				return false; // non-letter detected: fail
			}
		}
		return true; // only letters: pass
	}

	// method checks if radio button is selected in button group
	private static boolean isButtonSelected(String input) {
		return !input.equals("");
	}
}
