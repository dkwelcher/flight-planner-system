/*
 * Group 6: Welcher, Rogers, Nguyen
 * FileToolkit.java
 * This class contains utility methods for reading from and writing to
 * a file.
 */

package toolkit;

import java.io.*;
import java.util.*;
import javax.swing.*;

import admin.AccessLevel;
import data.Airplane;
import data.Airport;
import data.NavBeacon;
import data.User;

public class FileToolkit {
	
	private static final String USER_PASS_FILE = "res/db/username-password.txt";
	private static final String AIRPORT_FILE = "res/db/airports.txt";
	private static final String NAV_BEACON_FILE = "res/db/navbeacons.txt";
	private static final String AIRPLANE_FILE = "res/db/airplanes.txt";

	// method reads from file to HashMap
	public static HashMap<String, User> readUserAndPass() {

		String file = USER_PASS_FILE;
		HashMap<String, User> usernameToUser = new HashMap<>();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				String[] arr = line.split(" ");
				String username = arr[0];
				String password = arr[1];
				AccessLevel access = AccessLevel.valueOf(arr[2]);
				User user = new User(username, password, access);
				if (!username.equals("") && user != null)
					usernameToUser.put(username, user);
			}
			reader.close();
			return usernameToUser;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "FileNotFoundException in readUserAndPass()", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException in readUserAndPass()", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	// method writes HashMap to file
	public static void writeUserAndPass(HashMap<String, User> usernameToUser) {

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(USER_PASS_FILE));
			for (Map.Entry<String, User> set : usernameToUser.entrySet()) { // iterate through HashMap
				writer.write(set.getKey() + " "); // write username plus delimiter
				writer.write(set.getValue().getPassword() + " "); // write password
				writer.write(set.getValue().getAccessLevel().name());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException in writeUserAndPass()", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// method reads from file to HashMap
	public static HashMap<String, Airport> readAirports() {

		String file = AIRPORT_FILE;
		HashMap<String, Airport> icaoToAirport = new HashMap<>();
		String line = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				String[] arr = line.split(",");
				String icao = arr[0].trim();
				String name = arr[1].trim();
				String city = arr[2].trim();
				String state = arr[3].trim();
				double latitude = Double.valueOf(arr[4].trim());
				double longitude = Double.valueOf(arr[5].trim());
				String radioType = arr[6].trim();
				double radioFreq = Double.valueOf(arr[7].trim());
				double runwayLength = Double.valueOf(arr[8].trim());
				boolean avgas = Boolean.valueOf(arr[9].trim());
				boolean jaa = Boolean.valueOf(arr[10].trim());
				Airport airport = new Airport(icao, name, city, state, latitude, longitude, radioType, radioFreq, runwayLength, avgas, jaa);
				if (!icao.equals("") && airport != null)
					icaoToAirport.put(icao, airport);
			}
			reader.close();
			return icaoToAirport;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "FileNotFoundException in readAirports()", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException in readAirports()", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	// method writes HashMap to file
	public static void writeAirports(HashMap<String, Airport> icaoToAirport) {

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(AIRPORT_FILE));
			final String DELIM = ",";
			for (Map.Entry<String, Airport> set : icaoToAirport.entrySet()) {
				writer.write(set.getValue().getIcao() + DELIM);
				writer.write(set.getValue().getName() + DELIM);
				writer.write(set.getValue().getCity() + DELIM);
				writer.write(set.getValue().getState() + DELIM);
				writer.write(set.getValue().getLatitude() + DELIM);
				writer.write(set.getValue().getLongitude() + DELIM);
				writer.write(set.getValue().getRadioType() + DELIM);
				writer.write(set.getValue().getRadioFreq() + DELIM);
				writer.write(set.getValue().getMaxRunwayLength() + DELIM);
				writer.write(set.getValue().isAvgas() + DELIM);
				writer.write(set.getValue().isJaa() + DELIM);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException in writeAirports()", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// method reads from file to HashMap
	public static HashMap<String , NavBeacon> readNavBeacons(){
		
		String file = NAV_BEACON_FILE;
		HashMap<String , NavBeacon> icaoToBeacon = new HashMap<>();
		String line = null;
			
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null) {
			String arr[] = line.split(",");
			String icao = arr[0].trim();
			String name = arr[1].trim();
			double latitude = Double.valueOf(arr[2].trim());
			double longitude = Double.valueOf(arr[3].trim());
			String type = arr[4].trim();
			NavBeacon beacon = new NavBeacon(icao , name , latitude , longitude , type);
			if(!icao.equals("") && beacon != null)
				icaoToBeacon.put(icao, beacon);
			}
			reader.close();
			return icaoToBeacon;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "FileNotFoundException in readNavBeacons()", "Error", JOptionPane.ERROR_MESSAGE);
		}catch(IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException in readNavBeacons()", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;	
	}
	
	// method writes HashMap to file
	public static void writeNavBeacons(HashMap<String, NavBeacon> icaoToBeacon) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(NAV_BEACON_FILE));
			final String DELIM = ",";
			for (Map.Entry<String, NavBeacon> set : icaoToBeacon.entrySet()) {
				writer.write(set.getValue().getIcao().toUpperCase() + DELIM);
				writer.write(set.getValue().getName() + DELIM);
				writer.write(set.getValue().getLatitude() + DELIM);
				writer.write(set.getValue().getLongitude() + DELIM);
				writer.write(set.getValue().getType().toUpperCase() + DELIM);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException in writeNavBeacons", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// method reads from file to HashMap
	public static HashMap<String , Airplane> readAirplanes(){
		
		String file = AIRPLANE_FILE;
		HashMap<String , Airplane> tailNumToAirplane = new HashMap<>();
		String line = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null) {
			String arr[] = line.split(",");
			String tailNumber = arr[0].trim();
			String make = arr[1].trim();
			String model = arr[2].trim();
			String type = arr[3].trim();
			Double tankSize = Double.valueOf(arr[4].trim());
			Double burnRate = Double.valueOf(arr[5].trim());
			Double airSpeed = Double.valueOf(arr[6].trim());
			Double minRunwayLength = Double.valueOf(arr[7].trim());
			String fuelType = arr[8].trim();
			Airplane plane = new Airplane(tailNumber , make , model , type , tankSize , burnRate , airSpeed, minRunwayLength, fuelType);
			if(!tailNumber.equals("") && plane != null)
				tailNumToAirplane.put(tailNumber , plane);
			}
			reader.close();
			return tailNumToAirplane;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "FileNotFoundException in readAirplanes()", "Error", JOptionPane.ERROR_MESSAGE);
		}catch(IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException in readAirplanes()", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	// method writes HashMap to file
	public static void writeAirplanes(HashMap<String, Airplane> tailNumToAirplane) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(AIRPLANE_FILE));
			final String DELIM = ",";
			for (Map.Entry<String, Airplane> set : tailNumToAirplane.entrySet()) {
				writer.write(set.getValue().getTailNumber() + DELIM);
				writer.write(set.getValue().getMake() + DELIM);
				writer.write(set.getValue().getModel() + DELIM);
				writer.write(set.getValue().getType() + DELIM);
				writer.write(set.getValue().getTankSizeString() + DELIM);
				writer.write(set.getValue().getBurnRateString() + DELIM);
				writer.write(set.getValue().getAirSpeedString() + DELIM);
				writer.write(set.getValue().getMinRunwayLengthString() + DELIM);
				writer.write(set.getValue().getFuelType() + DELIM);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException in writeAirplanes()", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
