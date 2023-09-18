/*
 * Group 6: Welcher, Rogers, Nguyen
 * AirportNode.java
 * This class creates fields, parameterized constructor, and get and set methods for AirportNode objects.
 * This class also contains a custom comparator for comparing total estimated cost for the flight planner.
 */

package data;

public class AirportNode implements Comparable<AirportNode> {
	private Airport airport;
	private AirportNode parent;
	private double cost;
	private double heuristic;

	public AirportNode(Airport airport, AirportNode parent, double cost, double heuristic) {
		this.airport = airport;
		this.parent = parent;
		this.cost = cost;
		this.heuristic = heuristic;
	}

	public Airport getAirport() {
		return airport;
	}

	public AirportNode getParent() {
		return parent;
	}

	public double getActualCost() {
		return cost;
	}

	public double getHeuristicCost() {
		return heuristic;
	}

	public double getTotalEstimatedCost() {
		return cost + heuristic;
	}

	@Override
	public int compareTo(AirportNode other) {
		return Double.compare(getTotalEstimatedCost(), other.getTotalEstimatedCost());
	}
}
