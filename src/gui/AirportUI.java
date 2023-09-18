/*
 * Group 6: Welcher, Rogers, Nguyen
 * AirportUI.java
 * This class creates an Airport Manager window which displays the sorted airports from file.
 * The window has a search bar which allows the user to search for airports and display their information.
 * The window also has buttons which allow the user to input new airports, modify existing airports, and
 * delete airports. Changes to the airport database are automatically written to file upon completion.
 * This class extends the UITemplate class and builds upon the template.
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.List;

import javax.swing.*;

import data.Airport;
import manager.AirportManager;
import toolkit.FileToolkit;
import toolkit.ValidationToolkit;

public class AirportUI extends UITemplate {
	
	private JFrame airportFrame = getFrame();
	private HashMap<String, Airport> temp;
	private HashMap<String, Airport> icaoToAirport;
	
	// constructor calls superclass constructor, creates UI, and reads from file to HashMap
	public AirportUI() {
		super();
		airportFrame.setTitle("Airport Manager");
		temp = FileToolkit.readAirports();
		icaoToAirport = sortByValue(temp);
		temp = null;
		airportFrame.add(createBodyPanel() , BorderLayout.CENTER);
		airportFrame.setVisible(true);
	}
	
	// method converts HashMap to List, sorts by ICAO, then returns a LinkedHashMap
	private HashMap<String, Airport> sortByValue(HashMap<String, Airport> airports) {

		List<Map.Entry<String, Airport>> list = new LinkedList<Map.Entry<String, Airport>>(airports.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Airport>>() {

			@Override
			public int compare(Entry<String, Airport> a1, Entry<String, Airport> a2) {
				return (a1.getValue().getIcao()).compareTo(a2.getValue().getIcao());
			}
		});

		HashMap<String, Airport> temp = new LinkedHashMap<String, Airport>();
		for (Map.Entry<String, Airport> ap : list) {
			temp.put(ap.getKey(), ap.getValue());
		}
		return temp;
	}
	
	@Override
	public JPanel createHeaderCenter() {
		JPanel panel = super.createHeaderCenter();
		JLabel titleLabel = new JLabel("Airport Manager");
		titleLabel.setFont(UIConstants.TITLEFONT);
		titleLabel.setForeground(Color.WHITE);
		panel.add(titleLabel);
		return panel;
	}
	
	private JButton searchButton;
	
	@Override
	public JPanel createHeaderEast() {
		JPanel panel = super.createHeaderEast();
		JLabel searchLabel = new JLabel("Search by ICAO:");
		searchLabel.setFont(UIConstants.BODYFONT);
		AirportUIListener listener = new AirportUIListener();
		searchButton = getSearchButton();
		searchButton.addActionListener(listener);
		panel.add(searchLabel, FlowLayout.LEFT);
		panel.add(searchButton);
		return panel;
	}
	
	@Override
	public JPanel createHeaderSouth() {
		JLabel[] headers = {
	 	 		new JLabel("ICAO :"),
	 	 		new JLabel("Name :"),
	 	 		new JLabel("City :"),
	 	 		new JLabel("State/Country :"),
	 	 		//new JLabel("Latitude :"),
	 	 		//new JLabel("Longitude :"),
	 	 		//new JLabel("Radio Type :"),
	 	 		//new JLabel("Radio Frequency :"),
	 	 		//new JLabel("Max Runway Length: "),
	 	 		//new JLabel("AVGAS :"),
	 	 		//new JLabel("JA-A :")
		};
		
		JPanel panel = super.createHeaderSouth();
		panel.setLayout(new GridLayout(0 , headers.length , 5 , 5));
		for(int i = 0 ; i < headers.length ; i++) {
			headers[i].setHorizontalAlignment(JLabel.CENTER);
			headers[i].setFont(UIConstants.BODYFONT);
			headers[i].setBorder(UIConstants.BORDER);
			panel.add(headers[i]);
		}
		return panel;
	}
	
	// method creates the panel which displays all airports and their information
	private JScrollPane createBodyPanel() {
		int counter = 0;
		JPanel panel = new JPanel();
 		panel.setLayout(new GridLayout(0 , 4 , 0 , 0));
		for(Map.Entry<String, Airport> airport : icaoToAirport.entrySet()) {
			JTextArea[] airportInfo = {
				new JTextArea(airport.getValue().getIcao()),
				new JTextArea(airport.getValue().getName(), 1, 25),
				new JTextArea(airport.getValue().getCity()),
				new JTextArea(airport.getValue().getState())
			};
			JPanel[] airportCells = {
				new JPanel(),
				new JPanel(),
				new JPanel(),
				new JPanel()
			};
			for(int i = 0; i < airportCells.length ; i++) {
				airportInfo[i].setLineWrap(true);
				airportInfo[i].setBackground(null);
				airportInfo[i].setFont(UIConstants.BODYFONT);
				airportCells[i].add(airportInfo[i]);
				if(counter % 2 != 0)
					airportCells[i].setBackground(UIConstants.BGCOLOR);
				panel.add(airportCells[i]);
			}
			counter++;
		}
		JScrollPane scroller = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
						  					   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scroller;		
	}
	
	private JButton addButton;
	private JButton modifyButton;
	private JButton deleteButton;
	private JButton exitButton;
	
	@Override
	public JPanel createFooter() {
		JPanel panel = super.createFooter();
		AirportUIListener listener = new AirportUIListener();
		addButton = getAddButton();
		addButton.addActionListener(listener);
		modifyButton = getModifyButton();
		modifyButton.addActionListener(listener);
		deleteButton = getDeleteButton();
		deleteButton.addActionListener(listener);
		exitButton = getExitButton();
		exitButton.addActionListener(listener);
		panel.add(addButton);
		panel.add(modifyButton);
		panel.add(deleteButton);
		panel.add(exitButton);
		return panel;
	}
	
 	private JRadioButton avgasTrueButton;
 	private JRadioButton avgasFalseButton;
 	private ButtonGroup avgasButtonGroup;
 	private JRadioButton jaaTrueButton;
 	private JRadioButton jaaFalseButton;
 	private ButtonGroup jaaButtonGroup;
 	private JTextField[] inputs = {
 			new JTextField(10),
 			new JTextField(10),
 			new JTextField(10),
 			new JTextField(10),
 			new JTextField(10),
 			new JTextField(10),
 			new JTextField(10),
 			new JTextField(10),
 			new JTextField(10)
 	};
 	private JButton addSaveButton;
 	private JButton addClearButton;
 	private JButton addCancelButton;
 	
	@Override
	public void createAddFrame() {
		clearInput();
		super.createAddFrame();
		JFrame addFrame = getAddFrame();
		addFrame.setSize(new Dimension(350, 350));
		addFrame.setTitle("Add Airport");
		JPanel panel = new JPanel();
 		panel.setLayout(new GridLayout(0 , 2));
 		panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 10));
 		
		avgasTrueButton = new JRadioButton("Yes");
		avgasFalseButton = new JRadioButton("No");
		avgasButtonGroup = new ButtonGroup();
		avgasButtonGroup.add(avgasTrueButton);
		avgasButtonGroup.add(avgasFalseButton);

		jaaTrueButton = new JRadioButton("Yes");
		jaaFalseButton = new JRadioButton("No");
		jaaButtonGroup = new ButtonGroup();
		jaaButtonGroup.add(jaaTrueButton);
		jaaButtonGroup.add(jaaFalseButton);
		
		JLabel[] headers = {
	 	 		new JLabel("ICAO :"),
	 	 		new JLabel("Name :"),
	 	 		new JLabel("City :"),
	 	 		new JLabel("State/Country :"),
	 	 		new JLabel("Latitude :"),
	 	 		new JLabel("Longitude :"),
	 	 		new JLabel("Radio Type :"),
	 	 		new JLabel("Radio Frequency :"),
	 	 		new JLabel("Max Runway Length: "),
	 	 		new JLabel("AVGAS :"),
	 	 		new JLabel("JA-A :")
		};

		for (int i = 0; i < headers.length; i++) {
			headers[i].setFont(UIConstants.TEXTFONT);
			headers[i].setBorder(BorderFactory.createEmptyBorder());
			JPanel labelCell = new JPanel(new BorderLayout());
			JPanel inputCell = new JPanel();
			if (i < 9) {
				labelCell.add(headers[i], BorderLayout.WEST);
				inputCell.add(inputs[i]);
			} else if (i == 9) {
				labelCell.add(headers[i], BorderLayout.WEST);
				inputCell.add(avgasTrueButton);
				inputCell.add(avgasFalseButton);
			} else if (i == 10) {
				labelCell.add(headers[i], BorderLayout.WEST);
				inputCell.add(jaaTrueButton);
				inputCell.add(jaaFalseButton);
			}
			panel.add(labelCell);
			panel.add(inputCell);
		}
		AirportUIListener listener = new AirportUIListener();
		addSaveButton = getAddSaveButton();
		addSaveButton.addActionListener(listener);
		addClearButton = getAddClearButton();
		addClearButton.addActionListener(listener);
		addCancelButton = getAddCancelButton();
		addCancelButton.addActionListener(listener);

		addFrame.add(panel, BorderLayout.CENTER);
		addFrame.setLocationRelativeTo(null);
		addFrame.setVisible(true);
	}
	
	private JButton modifySaveButton;
	private JButton modifyClearButton;
	private JButton modifyCancelButton;
	private String initialIcao;
	
	@Override
	public void createModifyFrame(Object object) {
		clearInput();
		Airport airport = (Airport) object;
		initialIcao = airport.getIcao();
		super.createModifyFrame(airport);
		JFrame modifyFrame = getModifyFrame();
		modifyFrame.setSize(new Dimension(350, 350));
		modifyFrame.setTitle("Modify Airport");
		JPanel panel = new JPanel();
 		panel.setLayout(new GridLayout(0 , 2));
 		panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 10));
 		
 		avgasTrueButton = new JRadioButton("Yes");
		avgasFalseButton = new JRadioButton("No");
		avgasButtonGroup = new ButtonGroup();
		avgasButtonGroup.add(avgasTrueButton);
		avgasButtonGroup.add(avgasFalseButton);
		
		jaaTrueButton = new JRadioButton("Yes");
		jaaFalseButton = new JRadioButton("No");
		jaaButtonGroup = new ButtonGroup();
		jaaButtonGroup.add(jaaTrueButton);
		jaaButtonGroup.add(jaaFalseButton);
		
		JLabel[] headers = {
	 	 		new JLabel("ICAO :"),
	 	 		new JLabel("Name :"),
	 	 		new JLabel("City :"),
	 	 		new JLabel("State/Country :"),
	 	 		new JLabel("Latitude :"),
	 	 		new JLabel("Longitude :"),
	 	 		new JLabel("Radio Type :"),
	 	 		new JLabel("Radio Frequency :"),
	 	 		new JLabel("Max Runway Length: "),
	 	 		new JLabel("AVGAS :"),
	 	 		new JLabel("JA-A :")
		};
 		
 		String[] modifyAirportText = {
 				airport.getIcao(),
 				airport.getName(),
 				airport.getCity(),
 				airport.getState(),
 				airport.getLatitudeString(),
 				airport.getLongitudeString(),
 				airport.getRadioType(),
 				airport.getRadioFreqString(),
 				airport.getMaxRunwayLengthString()
 		 	};

			for (int i = 0; i < headers.length; i++) {
				headers[i].setFont(UIConstants.TEXTFONT);
				headers[i].setBorder(BorderFactory.createEmptyBorder());
				JPanel labelCell = new JPanel(new BorderLayout());
				JPanel inputCell = new JPanel();
				if (i < 9) {
					inputs[i].setText(modifyAirportText[i]);
					labelCell.add(headers[i], BorderLayout.WEST);
					inputCell.add(inputs[i]);
				} else if (i == 9) {
					if (airport.isAvgas())
						avgasTrueButton.setSelected(true);
					if (!airport.isAvgas())
						avgasFalseButton.setSelected(true);
					labelCell.add(headers[i], BorderLayout.WEST);
					inputCell.add(avgasTrueButton);
					inputCell.add(avgasFalseButton);
				} else if (i == 10) {
					if (airport.isJaa())
						jaaTrueButton.setSelected(true);
					if (!airport.isJaa())
						jaaFalseButton.setSelected(true);
					labelCell.add(headers[i], BorderLayout.WEST);
					inputCell.add(jaaTrueButton);
					inputCell.add(jaaFalseButton);
				}
				panel.add(labelCell);
				panel.add(inputCell);
			}
			AirportUIListener listener = new AirportUIListener();
			modifySaveButton = getModifySaveButton();
			modifySaveButton.addActionListener(listener);
			modifyClearButton = getModifyClearButton();
			modifyClearButton.addActionListener(listener);
			modifyCancelButton = getModifyCancelButton();
			modifyCancelButton.addActionListener(listener);
			
			modifyFrame.add(panel, BorderLayout.CENTER);
			modifyFrame.setLocationRelativeTo(null);
			modifyFrame.setVisible(true);
	}
	
	// method clears all textfields and radio buttons
	private void clearInput() {
 		for(JTextField input : inputs)
 			input.setText("");
 		if (avgasButtonGroup != null && jaaButtonGroup != null) {
 			avgasButtonGroup.clearSelection();
			jaaButtonGroup.clearSelection();
 		}
 		inputs[0].requestFocus();
 	}
	
	// nested class handles button events
	private class AirportUIListener implements ActionListener {

		AirportManager manager = new AirportManager();
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object button = e.getSource();
			if(button == searchButton)
				executeSearchButton();
			else if(button == addButton)
				createAddFrame();
			else if(button == modifyButton)
				executeModifyButton();
			else if(button == deleteButton)
				executeDeleteButton();
			else if(button == exitButton)
				airportFrame.dispose();
			else if(button == addSaveButton)
				executeAddSaveButton();
			else if(button == addClearButton)
				clearInput();
			else if(button == addCancelButton)
				executeAddCancelButton();
			else if(button == modifySaveButton)
				executeModifySaveButton();
			else if(button == modifyClearButton)
				clearInput();
			else if(button == modifyCancelButton)
				executeModifyCancelButton();
		}
		
		// method extracts information from textfield then calls display from manager class
		private void executeSearchButton() {
			String input = getSearchField().getText();
			manager.display(icaoToAirport, input);
		}
		
		// method prompts user, validates input, then creates a modify window
		private void executeModifyButton() {
			String input = JOptionPane.showInputDialog(null, "Enter airport to modify:",
					"Modify Airport", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(ValidationToolkit.validateAirportIcao(icaoToAirport, input.toUpperCase()))
				createModifyFrame(icaoToAirport.get(input.toUpperCase()));
			else
				modifyButton.doClick();
		}
		
		// method prompts user, validates input, then deletes airport from HashMap
		private void executeDeleteButton() {
			String input = JOptionPane.showInputDialog(null, "Enter airport to delete:",
					"Delete Airport", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(ValidationToolkit.validateAirportIcao(icaoToAirport, input.toUpperCase())) {
				manager.delete(icaoToAirport, input.toUpperCase());
				FileToolkit.writeAirports(icaoToAirport);
				resetWindow();
			}
			else
				deleteButton.doClick();
		}
		
		// method extracts information from textfields, validates the info, then
		// writes HashMap to file if successful
		private void executeAddSaveButton() {
			String[] addInfo = getTextFromFields();
			if(ValidationToolkit.validateAddAirport(addInfo)) {
				if (icaoToAirport.containsKey(addInfo[0])) {
					JOptionPane.showMessageDialog(null, "ICAO already exists in database.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				manager.add(icaoToAirport, addInfo);
				FileToolkit.writeAirports(icaoToAirport);
				JFrame addFrame = getAddFrame();
				addFrame.dispose();
				JOptionPane.showMessageDialog(null, "Successfully added: " + addInfo[1] + "!", "Airport saved",
						JOptionPane.PLAIN_MESSAGE);
				resetWindow();
			}
		}
		
		// method disposes of add window
		private void executeAddCancelButton() {
			JFrame frame = getAddFrame();
			frame.dispose();
		}
		
		// method extracts information from textfields, validates the info, then
		// writes HashMap to file if successful
		private void executeModifySaveButton() {
			String[] modifyInfo = getTextFromFields();
			if(ValidationToolkit.validateAddAirport(modifyInfo)) {
				if(ValidationToolkit.validateModifyAirport(icaoToAirport, modifyInfo, initialIcao)) {
					manager.modify(icaoToAirport, modifyInfo, initialIcao);
					FileToolkit.writeAirports(icaoToAirport);
					JFrame modifyFrame = getModifyFrame();
					modifyFrame.dispose();
					JOptionPane.showMessageDialog(null, "Successfully modified: " + modifyInfo[1] + "!", "Airport saved",
							JOptionPane.PLAIN_MESSAGE);
					resetWindow();
				}
			}
		}
		
		// method disposes of modify window
		private void executeModifyCancelButton() {
			JFrame frame = getModifyFrame();
			frame.dispose();
		}
		
		// method disposes window then calls new instance of AirportUI
		private void resetWindow() {
			JFrame frame = getFrame();
			frame.dispose();
			new AirportUI();
		}
		
		// method extracts information from textfields and radio buttons from add / modify window
	 	private String[] getTextFromFields() {
	 		String avgas = "";
				if(avgasTrueButton.isSelected())
					avgas = "true";
				else if(avgasFalseButton.isSelected()) 
					avgas = "false";
				String jaa = "";
				if(jaaTrueButton.isSelected())
					jaa = "true";
				else if(jaaFalseButton.isSelected())
					jaa = "false";
				String[] info = {
					inputs[0].getText().toUpperCase(),
					inputs[1].getText(),
					inputs[2].getText(),
					inputs[3].getText(),
					inputs[4].getText(),
					inputs[5].getText(),
					inputs[6].getText().toUpperCase(),
					inputs[7].getText(),
					inputs[8].getText(),
					avgas,
					jaa
				};
				return info;
	 	}
	}
}