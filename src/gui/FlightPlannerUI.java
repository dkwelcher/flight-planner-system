/*
 * Group 6: Welcher, Rogers, Nguyen
 * FlightPlannerUI.java
 * This class creates a Flight Planner window which allows the user to select
 * starting and destination airports and airplane to be used in calculating
 * the flight path. When selecting the user may enter an exact key match or 
 * enter a substring of airport name, city, or state/country or airplane tail
 * ID, make, or model. A selection screen is generated with the given substring,
 * and the user may select from the list of entries. Clicking on the Plan button
 * sends the information to be processed by the algorithm in FlightPlannerManager.java.
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;

import data.Airplane;
import data.Airport;
import manager.FlightPlannerManager;
import toolkit.FileToolkit;

public class FlightPlannerUI {
	
	private JFrame fpMainFrame;
	private JFrame selectFrame;
	private JButton startAirportSearchButton;
	private JButton destAirportSearchButton;
	private JButton airplaneSearchButton;
	private JButton closeButton;
	private JButton planButton;
	private HashMap<String, Airport> icaoToAirport;
	private HashMap<String, Airplane> tailNumToAirplane;
	private final Font TITLE_FONT = new Font("Verdana", Font.BOLD, 18);
	
	private Airport startingAirport;
	private Airport destinationAirport;
	private Airplane airplane;

	// constructor creates the UI and reads from files to HashMaps
	public FlightPlannerUI() {
		
		final double widthScale = 0.235;
		final double heightScale = 0.21;
		Dimension frameSize = getFrameDimension(widthScale, heightScale);
		
		fpMainFrame = new JFrame("Flight Planner");
		fpMainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fpMainFrame.setSize(frameSize);
		fpMainFrame.setMinimumSize(new Dimension(650, 325));
		
		JPanel northPanel = createNorthPanel();
		JPanel centerPanel = createCenterPanel();
		JPanel southPanel = createSouthPanel();
		
		fpMainFrame.add(northPanel, BorderLayout.NORTH);
		fpMainFrame.add(centerPanel, BorderLayout.CENTER);
		fpMainFrame.add(southPanel, BorderLayout.SOUTH);
		
		fpMainFrame.setResizable(false);
		fpMainFrame.setLocationRelativeTo(null);
		fpMainFrame.setVisible(true);
		
		icaoToAirport = FileToolkit.readAirports();
		tailNumToAirplane = FileToolkit.readAirplanes();
	}
	
	// method returns a new Dimension based on user's screen size
	private Dimension getFrameDimension(double wScale, double hScale) {
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = (int)(screenDimension.width * wScale);
		int frameH = (int)(screenDimension.height * hScale);
		return new Dimension(frameW , frameH);
	}
	
	// method creates the panel which displays the banner
	private JPanel createNorthPanel() {
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(UIConstants.BGCOLOR);
		northPanel.setPreferredSize(new Dimension(25, 75));
		
		JLabel fpsLabel = new JLabel("Flight Planner");
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
	
	private JLabel startOutputLabel;
	private JLabel destOutputLabel;
	private JLabel airplaneOutputLabel;
	
	// methods creates the panel which displays labels and buttons that handle events
	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel(new GridLayout(3, 3, 5, 5));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		FlightPlannerUIListener listener = new FlightPlannerUIListener();
		
		JLabel startTextLabel = new JLabel("Starting Airport:");
		startOutputLabel = new JLabel("");
		startAirportSearchButton = new JButton("Search");
		startAirportSearchButton.addActionListener(listener);
		
		JLabel destTextLabel = new JLabel("Destination Airport:");
		destOutputLabel = new JLabel("");
		destAirportSearchButton = new JButton("Search");
		destAirportSearchButton.addActionListener(listener);
		
		JLabel airplaneTextLabel = new JLabel("Airplane:");
		airplaneOutputLabel = new JLabel("");
		airplaneSearchButton = new JButton("Search");
		airplaneSearchButton.addActionListener(listener);
		
		startAirportSearchButton.requestFocus();
		
		centerPanel.add(startTextLabel);
		centerPanel.add(startOutputLabel);
		centerPanel.add(startAirportSearchButton);
		centerPanel.add(destTextLabel);
		centerPanel.add(destOutputLabel);
		centerPanel.add(destAirportSearchButton);
		centerPanel.add(airplaneTextLabel);
		centerPanel.add(airplaneOutputLabel);
		centerPanel.add(airplaneSearchButton);
		return centerPanel;
	}
	
	// method creates the panel which holds buttons that handle events
	private JPanel createSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(1, 0, 10, 10));
		southPanel.setBorder(BorderFactory.createEmptyBorder(30, 200, 5, 200));
		
		FlightPlannerUIListener listener = new FlightPlannerUIListener();
		planButton = new JButton("Plan");
		planButton.addActionListener(listener);
		closeButton = new JButton("Close");
		closeButton.addActionListener(listener);
		
		southPanel.add(planButton);
		southPanel.add(closeButton);
		return southPanel;
	}
	
	private JLabel[] labels;
	private JButton[] buttons;
	private JButton cancelButton;
	private boolean isStart = false;
	private boolean isAirplane = false;
	
	// method creates the window which allows user to select airports or airplanes
	private JFrame createSelectFrame(JPanel northSelectPanel, JPanel centerSelectPanel, JPanel southSelectPanel) {
		Dimension frameSize = getSelectFrameDimension();
		
		selectFrame = new JFrame();
		selectFrame.setSize(frameSize);
		selectFrame.setMinimumSize(new Dimension(600, 300));
		selectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JScrollPane selectScroller = new JScrollPane(centerSelectPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		selectFrame.add(northSelectPanel, BorderLayout.NORTH);
		selectFrame.add(selectScroller, BorderLayout.CENTER);
		selectFrame.add(southSelectPanel, BorderLayout.SOUTH);
		
		return selectFrame;
	}
	
	// method creates window which allows user to select from a list of airports
	private void createAirportSelectFrame(List<Airport> matches, int comparison) {
	    FlightPlannerUIListener listener = new FlightPlannerUIListener();

	    JPanel northSelectPanel = createNorthSelectPanel();
	    JLabel titleLabel = new JLabel();
	    titleLabel.setFont(TITLE_FONT);
	    titleLabel.setForeground(Color.WHITE);
	    titleLabel.setHorizontalAlignment(JLabel.CENTER);
	    if (comparison == 0)
	        titleLabel.setText("Select Starting Airport");
	    else if (comparison == 1)
	        titleLabel.setText("Select Destination Airport");
	    northSelectPanel.add(titleLabel, BorderLayout.CENTER);

	    JPanel centerSelectPanel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    labels = new JLabel[matches.size()];
	    buttons = new JButton[matches.size()];

	    for (int i = 0; i < matches.size(); ++i) {
	        gbc.gridx = 0;
	        gbc.gridy = i;
	        labels[i] = new JLabel(matches.get(i).getName());
	        centerSelectPanel.add(labels[i], gbc);

	        gbc.gridx = 1;
	        gbc.gridy = i;
	        
	        gbc.insets = new Insets(0, 10, 5, 0);
	        
	        buttons[i] = new JButton("Select");
	        buttons[i].addActionListener(listener);
	        centerSelectPanel.add(buttons[i], gbc);
	        
	        gbc.insets = new Insets(0, 0, 0, 0);
	    }

	    JPanel southSelectPanel = createSouthSelectPanel();
	    cancelButton.addActionListener(listener);
	    southSelectPanel.add(cancelButton, BorderLayout.CENTER);

	    JFrame selectFrame = createSelectFrame(northSelectPanel, centerSelectPanel, southSelectPanel);
	    selectFrame.setTitle("Airport Select");

	    selectFrame.setResizable(false);
	    selectFrame.setLocationRelativeTo(null);
	    selectFrame.setVisible(true);
	    if (comparison == 0) {
	        isStart = true;
	        isAirplane = false;
	    } else if (comparison == 1) {
	        isStart = false;
	        isAirplane = false;
	    }
	}

	// method creates window which allows user to select from a list of airplanes
	private void createAirplaneSelectFrame(List<Airplane> matches) {
	    FlightPlannerUIListener listener = new FlightPlannerUIListener();

	    JPanel northSelectPanel = createNorthSelectPanel();
	    JLabel titleLabel = new JLabel("Select Airplane");
	    titleLabel.setFont(TITLE_FONT);
	    titleLabel.setForeground(Color.WHITE);
	    titleLabel.setHorizontalAlignment(JLabel.CENTER);
	    northSelectPanel.add(titleLabel, BorderLayout.CENTER);

	    JPanel centerSelectPanel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    labels = new JLabel[matches.size()];
	    buttons = new JButton[matches.size()];

	    for (int i = 0; i < matches.size(); ++i) {
	        Airplane airplane = matches.get(i);
	        labels[i] = new JLabel(airplane.getMake() + " " + airplane.getModel());

	        gbc.gridx = 0;
	        gbc.gridy = i;
	        centerSelectPanel.add(labels[i], gbc);

	        buttons[i] = new JButton("Select");
	        buttons[i].addActionListener(listener);

	        gbc.gridx = 1;
	        gbc.gridy = i;

	        gbc.insets = new Insets(0, 10, 5, 0);

	        centerSelectPanel.add(buttons[i], gbc);

	        gbc.insets = new Insets(0, 0, 0, 0);
	    }

	    JPanel southSelectPanel = createSouthSelectPanel();
	    cancelButton.addActionListener(listener);
	    southSelectPanel.add(cancelButton, BorderLayout.CENTER);

	    JFrame selectFrame = createSelectFrame(northSelectPanel, centerSelectPanel, southSelectPanel);
	    selectFrame.setTitle("Airplane Select");

	    selectFrame.setResizable(false);
	    selectFrame.setLocationRelativeTo(null);
	    selectFrame.setVisible(true);
	    isAirplane = true;
	}

	// method returns new Dimension for select window
	private Dimension getSelectFrameDimension() {
		final double widthScale = 0.2;
		final double heightScale = 0.21;
		return getFrameDimension(widthScale, heightScale);
	}
	
	// method creates the panel which houses the airports or airplanes and buttons
	private JPanel createNorthSelectPanel() {
		JPanel northSelectPanel = new JPanel(new BorderLayout());
		northSelectPanel.setBackground(UIConstants.BGCOLOR);
		northSelectPanel.setPreferredSize(new Dimension(25, 50));
		return northSelectPanel;
	}
	
	// method creates the panel which houses the cancel button
	private JPanel createSouthSelectPanel() {
		JPanel southSelectPanel = new JPanel(new BorderLayout());
		southSelectPanel.setBorder(BorderFactory.createEmptyBorder(5, 200, 5, 200));
		cancelButton = new JButton("Cancel");
		return southSelectPanel;
	}
	
	// nested class handles button events
	private class FlightPlannerUIListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object button = e.getSource();
			
			if(button == startAirportSearchButton)
				executeStartAirportSearchButton();
			else if(button == destAirportSearchButton)
				executeDestAirportSearchButton();
			else if(button == airplaneSearchButton)
				executeAirplaneSearchButton();
			else if(button == planButton)
				executePlanButton();
			else if(button == closeButton)
				fpMainFrame.dispose();
			else if(button == cancelButton)
				selectFrame.dispose();
			
			checkSearchButtons(button);
		}
		
		// method determines which button user pressed and processed information
		private void checkSearchButtons(Object button) {
			if (buttons != null) {
				setSelection(button);
			}
		}
		
		// method determines which button is pressed and processes the information accordingly
		private void setSelection(Object button) {
			for (int i = 0; i < buttons.length; ++i) {
				if (button == buttons[i]) {
					if (isAirplane) {
						setAirplane(i);
					}
					else if (isStart) {
						setStartingAirport(i);
					}
					else if (!isStart){
						setDestinationAirport(i);
					}
					else
						return;
				}
			}
		}
		
		// method displays the airplane selected and assigns airplane variable
		private void setAirplane(int i) {
			String tailNum = getTailNumKey(i);
			if (tailNum == null) return;
			airplane = tailNumToAirplane.get(tailNum);
			airplaneOutputLabel.setText(airplane.getMake() + " " + airplane.getModel());
			selectFrame.dispose();
		}
		
		// method displays the starting airport selected and assigns startingAirport variable
		private void setStartingAirport(int i) {
			String icao = getIcaoKey(i);
			if (icao == null) return;
			startingAirport = icaoToAirport.get(icao);
			startOutputLabel.setText(startingAirport.getName());
			selectFrame.dispose();
		}
		
		// method displays the destination airport selected and assigns destinationAirport variable
		private void setDestinationAirport(int i) {
			String icao = getIcaoKey(i);
			if (icao == null) return;
			destinationAirport = icaoToAirport.get(icao);
			destOutputLabel.setText(destinationAirport.getName());
			selectFrame.dispose();
		}
		
		// method finds selected airport in HashMap
		private String getIcaoKey(int i) {
			for (Map.Entry<String, Airport> airport : icaoToAirport.entrySet()) {
				if (airport.getValue().getName().equals(labels[i].getText()))
					return airport.getKey();
			}
			return null;
		}
		
		// method finds selected airplane in HashMap
		private String getTailNumKey(int i) {
			for (Map.Entry<String, Airplane> airplane : tailNumToAirplane.entrySet()) {
				String original = labels[i].getText();
				int index = original.indexOf(" ");
				String make = original.substring(0, index);
				String model = original.substring(index + 1);
				if (airplane.getValue().getMake().toUpperCase().equals(make.toUpperCase()) &&
					airplane.getValue().getModel().toUpperCase().equals(model.toUpperCase()))
					return airplane.getKey();
			}
			return null;
		}
		
		// methods prompts user, validates and processes the information, adds all matches to a list,
		// then creates an starting airport selection window
		private void executeStartAirportSearchButton() {
			String input = JOptionPane.showInputDialog(null, "Enter ICAO or airport name, city, or state / country:",
					"Search Starting Airport", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(icaoToAirport.containsKey(input.toUpperCase())) {
				startingAirport = icaoToAirport.get(input.toUpperCase());
				startOutputLabel.setText(startingAirport.getName());
			}
			else {
				List<Airport> matches = new ArrayList<Airport>();
				for (Airport airport : icaoToAirport.values()) {
					if(airport.getName().toUpperCase().contains(input.toUpperCase()))
						matches.add(airport);
					else if(airport.getCity().toUpperCase().contains(input.toUpperCase()))
						matches.add(airport);
					else if(airport.getState().toUpperCase().contains(input.toUpperCase()))
						matches.add(airport);
				}
				if(matches.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No airport matches found.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Collections.sort(matches);
				createAirportSelectFrame(matches, 0);
			}
		}
		
		// methods prompts user, validates and processes the information, adds all matches to a list,
		// then creates an destination airport selection window
		private void executeDestAirportSearchButton() {
			String input = JOptionPane.showInputDialog(null, "Enter ICAO or airport name, city, or state / country:",
					"Search Destination Airport", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(icaoToAirport.containsKey(input.toUpperCase())) {
				destinationAirport = icaoToAirport.get(input.toUpperCase());
				destOutputLabel.setText(destinationAirport.getName());
			}
			else {
				List<Airport> matches = new ArrayList<Airport>();
				for (Airport airport : icaoToAirport.values()) {
					if(airport.getName().toUpperCase().contains(input.toUpperCase()))
						matches.add(airport);
					else if(airport.getCity().toUpperCase().contains(input.toUpperCase()))
						matches.add(airport);
					else if(airport.getState().toUpperCase().contains(input.toUpperCase()))
						matches.add(airport);
				}
				if(matches.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No airport matches found.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Collections.sort(matches);
				createAirportSelectFrame(matches, 1);
			}
		}
		
		// methods prompts user, validates and processes the information, adds all matches to a list,
		// then creates an airplane selection window
		private void executeAirplaneSearchButton() {
			String input = JOptionPane.showInputDialog(null, "Enter Tail ID or airplane make or model:",
					"Search Airplane", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(tailNumToAirplane.containsKey(input)) {
				airplane = tailNumToAirplane.get(input);
				airplaneOutputLabel.setText(airplane.getMake() + " " + airplane.getModel());
			}
			else {
				HashSet<Airplane> matches = new HashSet<Airplane>();
				for (Airplane airplane : tailNumToAirplane.values()) {
					if(airplane.getMake().toUpperCase().contains(input.toUpperCase()))
						matches.add(airplane);
					else if(airplane.getModel().toUpperCase().contains(input.toUpperCase()))
						matches.add(airplane);
				}
				if(matches.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No airplane matches found.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				List<Airplane> list = new ArrayList<>(matches);
				Collections.sort(list);
				createAirplaneSelectFrame(list);
			}
		}
		
		// method validates all necessary information for creating flight path and if successful,
		// then sends the information to the FlightPlannerManager to be processed and calls the
		// FlightPathUI if a flight path exists
		private void executePlanButton() {
			
			if (startingAirport != null && destinationAirport != null && airplane != null) {
				if (startingAirport.getIcao().equals(destinationAirport.getIcao())) {
					JOptionPane.showMessageDialog(null, "Starting Airport and Destination Airport cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (airplane.getMinRunwayLength() > destinationAirport.getMaxRunwayLength()) {
					JOptionPane.showMessageDialog(null, "Destination airport's runway length does not accommodate selected airplane.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				FlightPlannerManager flightPlanner = new FlightPlannerManager();
				List<Airport> shortestPath = flightPlanner.getShortestPath(icaoToAirport, startingAirport, destinationAirport, airplane);
				
				
				if (shortestPath != null) {
					ArrayList<Airport> flightPath = (ArrayList<Airport>) shortestPath;
					new FlightPathUI(flightPath, startingAirport, destinationAirport, airplane);
				}
				else {
					DecimalFormat df = new DecimalFormat("#.##");
					JOptionPane.showMessageDialog(null,
							"Flight Plan couldn't be established.\nMax flight range for given airplane is " + df.format(airplane.getRange()) +
							" NM.\nFuel type for given airplane is " + airplane.getFuelType(),
							"Flight Planner Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
				JOptionPane.showMessageDialog(null, "Please make a selection for starting airport, destination airport, and airplane.",
						"Plan Error", JOptionPane.ERROR_MESSAGE);
		}
	}	
}