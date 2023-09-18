/*
 * Group 6: Welcher, Rogers, Nguyen
 * FlightPathUI.java
 * This class creates a Flight Path window which displays information about the
 * flight path information passed in. The window shows the initial information about
 * the flight path including starting and destination airport information. The flight
 * path is divided into legs which show the starting and ending airports, the distance
 * traveled, hours in flight, and cardinal direction. The total distance and hours
 * traveled of the entire flight is given at the end.
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;

import data.Airplane;
import data.Airport;
import toolkit.FlightPlannerToolkit;

public class FlightPathUI {

	private JFrame pathFrame;
	private JButton closeButton;
	private final Font TITLE_FONT = new Font("Verdana", Font.BOLD, 28);
	private ArrayList<Airport> flightPath;
	private Airport startingAirport;
	private Airport destinationAirport;
	private Airplane airplane;
	
	// constructor creates UI and initializes parameters pass in
	public FlightPathUI(ArrayList<Airport> flightPath, Airport startingAirport, Airport destinationAirport, Airplane airplane) {
		this.flightPath = flightPath;
		this.startingAirport = startingAirport;
		this.destinationAirport = destinationAirport;
		this.airplane = airplane;
		
		final double widthScale = 0.3525;
		final double heightScale = 0.5;
		Dimension frameSize = getFrameDimension(widthScale, heightScale);
		
		pathFrame = new JFrame("Flight Plan");
		pathFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pathFrame.setSize(frameSize);
		pathFrame.setMinimumSize(new Dimension(900, 750));
		
		JPanel northPanel = createNorthPanel();
		JPanel centerPanel = createCenterPanel();
		JPanel southPanel = createSouthPanel();
		
		pathFrame.add(northPanel, BorderLayout.NORTH);
		pathFrame.add(centerPanel, BorderLayout.CENTER);
		pathFrame.add(southPanel, BorderLayout.SOUTH);
		
		pathFrame.setResizable(true);
		pathFrame.setLocationRelativeTo(null);
		pathFrame.setVisible(true);
	}
	
	// method returns a new Dimension based on user's screen size
	private Dimension getFrameDimension(double wScale, double hScale) {
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = (int)(screenDimension.width * wScale);
		int frameH = (int)(screenDimension.height * hScale);
		return new Dimension(frameW , frameH);
	}
	
	// method creates panel which displays banner
	private JPanel createNorthPanel() {
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(UIConstants.BGCOLOR);
		northPanel.setPreferredSize(new Dimension(25, 75));
		
		JLabel fpsLabel = new JLabel("Flight Plan");
		fpsLabel.setFont(TITLE_FONT);
		fpsLabel.setForeground(Color.WHITE);
		fpsLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel disclaimerLabel = new JLabel("DISCLAIMER: THIS SOFTWARE IS NOT TO BE USED FOR FLIGHT PLANNING OR NAVIGATIONAL PURPOSE");
		disclaimerLabel.setFont(UIConstants.DISCLAIMER_FONT);
		disclaimerLabel.setForeground(Color.WHITE);
		disclaimerLabel.setHorizontalAlignment(JLabel.CENTER);
		
		northPanel.add(fpsLabel, BorderLayout.CENTER);
		northPanel.add(disclaimerLabel, BorderLayout.SOUTH);
		return northPanel;
	}
	
	// method creates panel which displays flight path
	private JPanel createCenterPanel() {
		final Font TEXT_AREA_FONT = new Font("Verdana", Font.PLAIN, 18);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		JPanel infoPanel = createInfoPanel();
		
		JPanel textAreaPanel = new JPanel(new BorderLayout());
		JTextArea pathTextArea = getFlightLegCalculations();
		pathTextArea.setEditable(false);
		pathTextArea.setFont(TEXT_AREA_FONT);
		pathTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textAreaPanel.add(pathTextArea, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(textAreaPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		centerPanel.add(infoPanel, BorderLayout.NORTH);
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		return centerPanel;
	}
	
	// method creates panel which shows initial flight path information
	private JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel(new GridBagLayout());
		
		JLabel startAirportTextLabel = new JLabel("Starting Airport:");
		JLabel startAirportLabel = new JLabel(startingAirport.getName());
		
		JLabel startComTextLabel = new JLabel("COM:");
		JLabel startComLabel = new JLabel(startingAirport.getRadioType() + ": " + startingAirport.getRadioFreq());
		
		JLabel destAirportTextLabel = new JLabel("Destination Airport:");
		JLabel destAirportLabel = new JLabel(destinationAirport.getName());
		
		JLabel destComTextLabel = new JLabel("COM:");
		JLabel destComLabel = new JLabel(destinationAirport.getRadioType() + ": " + destinationAirport.getRadioFreq());
		
		JLabel airplaneTextLabel = new JLabel("Airplane:");
		JLabel airplaneLabel = new JLabel(airplane.getMake() + " " + airplane.getModel());
		
		JLabel[] left = {startAirportTextLabel, startComTextLabel, destAirportTextLabel,
				destComTextLabel, airplaneTextLabel
		};
		
		JLabel[] right = {startAirportLabel, startComLabel, destAirportLabel,
				destComLabel, airplaneLabel
		};
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 10, 2, 0);
		
		for (int i = 0; i < left.length; ++i) {
			gbc.gridx = 0;
			gbc.gridy = i;
			infoPanel.add(left[i], gbc);
			
			gbc.gridx = 1;
			infoPanel.add(right[i], gbc);
		}
		return infoPanel;
	}
	
	// method creates panel which contains buttons that handle events
	private JPanel createSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(1, 0, 10, 10));
		southPanel.setBorder(BorderFactory.createEmptyBorder(0, 400, 5, 400));
		FlightPathUIListener listener = new FlightPathUIListener();
		closeButton = new JButton("Close");
		closeButton.addActionListener(listener);
		southPanel.add(closeButton);
		return southPanel;
	}
	
	// method gathers information about flight path and displays the information in a text area
	private JTextArea getFlightLegCalculations() {
		
		JTextArea textArea = new JTextArea();
		DecimalFormat df = new DecimalFormat("#.##");
		final int DIVIDER = 105;
		ArrayList<Airport> fuelingStops = getFuelingStops();
		
		if (fuelingStops != null) {
			double totalDistance = 0;
			
			for (int i = 0; i < fuelingStops.size() - 1; ++i) {
				Airport current = fuelingStops.get(i);
				Airport next = fuelingStops.get(i + 1);
				double cost = FlightPlannerToolkit.calculateCost(current, next);
				totalDistance += cost;
				
				if ((i + 1) > 1) textArea.append("-".repeat(DIVIDER) + "\n");
				textArea.append("FLIGHT LEG: " + (i + 1) + "\n");
				textArea.append("FROM:  " + current.getName() + " (" + current.getCity() + ", " + current.getState() + ")\n");
				textArea.append("TO:      " + next.getName() + " (" + next.getCity() + ", " + next.getState() + ")\n");
				textArea.append("Distance (NM): " + df.format(cost) + "\n");
				textArea.append("Hours: " + df.format(FlightPlannerToolkit.calculateTime(cost, airplane.getAirSpeed())) + "\n");
				
				double headingRadians = FlightPlannerToolkit.getHeadingRadians(current, next);
				double heading = FlightPlannerToolkit.getPureHeading(headingRadians);
				String cardinalDirection = FlightPlannerToolkit.getCardinalDirection(current, next, FlightPlannerToolkit.getHeading(headingRadians));
				
				textArea.append("Direction: " + cardinalDirection + " (Heading: " + df.format(heading) + "°)" + "\n");
				textArea.append("-".repeat(DIVIDER) + "\n");
				
				if ((i + 2) != fuelingStops.size())
					textArea.append("Fueling Stop: " + next.getName() + "\n");
			}
			textArea.append("Total Distance (NM): " + df.format(totalDistance) + "\n");
			textArea.append("Total Hours: " + df.format(FlightPlannerToolkit.calculateTime(totalDistance, airplane.getAirSpeed())));
		}
		return textArea;
	}
	
	// post-processing method which processes a rare special case where the path includes
	// a non-fueling stop where the cost difference between a direct path and the path with
	// the included stop is 0 < cost < 1
	private ArrayList<Airport> getFuelingStops() {
		ArrayList<Airport> stops = new ArrayList<Airport>();
		
		stops.add(startingAirport);
		if (flightPath != null) {
			double currentFlightRange = airplane.getRange();
			for (int i = 0; i < flightPath.size() - 1; ++i) {
				Airport current = flightPath.get(i);
				Airport next = flightPath.get(i + 1);
				double cost = FlightPlannerToolkit.calculateCost(current, next);
				if (cost > currentFlightRange) {
					stops.add(current);
					currentFlightRange = airplane.getRange();
				}
				currentFlightRange -= cost;
			}
		}
		stops.add(destinationAirport);
		
		return stops;
	}
	
	// nested class which handles button events
	private class FlightPathUIListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == closeButton)
				pathFrame.dispose();
		}
	}
}
