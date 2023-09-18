package fptoolkit;

import java.util.HashMap;

import data.Airport;
import toolkit.FileToolkit;
import toolkit.FlightPlannerToolkit;

public class HeuristicTest {

	public static void main(String[] args) {

		HashMap<String, Airport> icaoToAirport = FileToolkit.readAirports();
		
		Airport start = icaoToAirport.get("KJFK");
		Airport dest = icaoToAirport.get("KDSM");
		
		Airport stop1 = icaoToAirport.get("KEWR");
		Airport stop2 = icaoToAirport.get("KORD");
		
		double costStartToStop1 = FlightPlannerToolkit.calculateCost(start, stop1);
		double costStop1ToStop2 = FlightPlannerToolkit.calculateCost(stop1, stop2);
		double costStop2ToDest = FlightPlannerToolkit.calculateCost(stop2, dest);
		
		double costByNode = costStartToStop1 + costStop1ToStop2 + costStop2ToDest;
		
		double costStartToDest = FlightPlannerToolkit.calculateCost(start, dest);
		
		//double costStartToStop2 = toolkit.heuristic(start, stop2);
		
		//System.out.println("From JFK to Newark to OHare:" + (costStartToStop1 + costStop1ToStop2));
		//System.out.println("From JFK to OHare:" + costStartToStop2);
		
		
		//System.out.println();
		System.out.println("Cost through Nodes: " + costByNode);
		System.out.println("Cost straight path: " + costStartToDest);
	}
}
