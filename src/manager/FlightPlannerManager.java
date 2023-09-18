/*
 * Group 6: Welcher, Rogers, Nguyen
 * FlightPlannerManager.java
 * This class initializes a graph, populates the graph with Airport objects and
 * their distance costs, then uses a specific implementation of the A* algorithm
 * suited for the needs of this flight planner system. When the destination airport
 * at lowest estimated cost is reached, the destination node is sent to a method which
 * backtracks the nodes to construct an appropriate flight path from starting airport to
 * destination airport. The path is sent back to the FlightPlannerUI class for
 * further processing.
 */

package manager;

import java.util.*;

import data.Airplane;
import data.Airport;
import data.AirportNode;
import toolkit.FlightPlannerToolkit;

public class FlightPlannerManager {
	
	// adjacency list representation of undirected, weighted graph
	private Map<Airport, Map<Airport, Double>> graph;
	
	// constructor initializes the graph as a HashMap
	public FlightPlannerManager() {
		graph = new HashMap<>();
	}
	
	// method adds all airports to the graph, then adds distance costs between airports if within airplane range
	// method then calls findShortestPath() to calculate the shortest path
	public List<Airport> getShortestPath(HashMap<String, Airport> icaoToAirport, Airport start, Airport destination, Airplane airplane) {
		for (Map.Entry<String, Airport> airport : icaoToAirport.entrySet()) {
			addAirport(airport.getValue());
		}

		for (Map.Entry<String, Airport> airportFrom : icaoToAirport.entrySet()) {
			for (Map.Entry<String, Airport> airportTo : icaoToAirport.entrySet()) {
				double cost = FlightPlannerToolkit.calculateCost(airportFrom.getValue(), airportTo.getValue());
				if (isValidRange(cost, airplane))
					addFlight(airportFrom.getValue(), airportTo.getValue());
			}
		}
		return findShortestPath(start, destination, airplane);
	}

	// method adds an airport to the graph if it doesn't exist
	// method adds a new HashMap for that airport to store distance costs to other airports
	private void addAirport(Airport airport) {
		if (!graph.containsKey(airport)) {
			graph.put(airport, new HashMap<>());
		}
	}

	// method adds the distance cost between the start and destination airports
	private void addFlight(Airport start, Airport destination) {
		double cost = FlightPlannerToolkit.calculateCost(start, destination);
		if (!graph.containsKey(start) || !graph.containsKey(destination))
			return;

		graph.get(start).put(destination, cost);
	}
	
	// method checks if distance cost is within airplane range
	private boolean isValidRange(double cost, Airplane airplane) {
		return cost <= airplane.getRange();
	}

	// method implements the modified A* algorithm to find the shortest path between start and destination airports
	private List<Airport> findShortestPath(Airport start, Airport destination, Airplane airplane) {
		
		PriorityQueue<AirportNode> possibleAirports = new PriorityQueue<>();
		Set<Airport> visited = new HashSet<>();

		possibleAirports.add(new AirportNode(start, null, 0, FlightPlannerToolkit.calculateCost(start, destination)));
		
		while (!possibleAirports.isEmpty()) {
			
			AirportNode current = possibleAirports.poll();
			
			if (current.getAirport().equals(destination)) {
				return recreatePath(current);
			}

			visited.add(current.getAirport());

	        if (graph.containsKey(current.getAirport())) {
	            for (Airport potentialAirport : graph.get(current.getAirport()).keySet()) {
	                if (!visited.contains(potentialAirport)) {
	                    double cost = graph.get(current.getAirport()).get(potentialAirport);
	                    if (isFuelTypeAvailable(potentialAirport, airplane) && isValidRunway(potentialAirport, airplane)) {
	                        double heuristic = FlightPlannerToolkit.calculateCost(potentialAirport, destination);
	                        possibleAirports.add(new AirportNode(potentialAirport, current, cost, heuristic));
	                    }
	                }
	            }
	        }
		}
		return null;
	}
	
	// method checks if the airplane's fuel type is available at the airport
	private boolean isFuelTypeAvailable(Airport airport, Airplane airplane) {
		return (airplane.getFuelType().equals("Jaa") && airport.isJaa() == true) || (airplane.getFuelType().equals("Avgas") && airport.isAvgas() == true);
	}
	
	// method checks if the airport's runway length can accomodate the airplane's minimum require runway length
	private boolean isValidRunway(Airport airport, Airplane airplane) {
		return airport.getMaxRunwayLength() >= airplane.getMinRunwayLength();
	}

	// method backtracks parent nodes from the destination airport to the start airport
	// method returns a list of Airport objects from start to destination airport
	private List<Airport> recreatePath(AirportNode node) {
		List<Airport> path = new ArrayList<>();
		while (node != null) {
			path.add(0, node.getAirport());
			node = node.getParent();
		}
		return path;
	}
}