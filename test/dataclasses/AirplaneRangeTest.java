package dataclasses;

import java.util.*;

import data.Airplane;
import toolkit.FileToolkit;

public class AirplaneRangeTest {

	public static void main(String[] args) {
		
		HashMap<String, Airplane> tailNumToAirplane = new HashMap<>();
		tailNumToAirplane = FileToolkit.readAirplanes();
		
		for (Map.Entry<String, Airplane> airplane : tailNumToAirplane.entrySet()) {
			System.out.println(airplane.getValue().getMake() + " " + airplane.getValue().getModel());
			System.out.println("Tank Size: " + airplane.getValue().getTankSize());
			System.out.println("Burn Rate: " + airplane.getValue().getBurnRate());
			System.out.println("Airspeed: " + airplane.getValue().getAirSpeed());
			System.out.println("Range: " + airplane.getValue().getRange());
			System.out.println("Min Runway Length: " + airplane.getValue().getMinRunwayLengthString());
			System.out.println();
		}
	}

}
