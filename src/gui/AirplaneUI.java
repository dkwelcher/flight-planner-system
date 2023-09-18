/*
 * Group 6: Welcher, Rogers, Nguyen
 * AirplaneUI.java
 * This class creates an Airplane Manager window which displays the sorted airplanes from file.
 * The window has a search bar which allows the user to search for airplanes and display their information.
 * The window also has buttons which allow the user to input new airplanes, modify existing airplanes, and
 * delete airplanes. Changes to the airplanes database are automatically written to file upon completion.
 * This class extends the UITemplate class and builds upon the template.
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.*;

import data.Airplane;
import manager.AirplaneManager;
import toolkit.FileToolkit;
import toolkit.ValidationToolkit;

public class AirplaneUI extends UITemplate {

	private JFrame airplaneFrame = getFrame();
	private HashMap<String, Airplane> temp;
	private HashMap<String, Airplane> tailNumToAirplane;
	
	// constructor calls superclass constructor, creates UI, and read from file to HashMap
	public AirplaneUI() {
		super();
		airplaneFrame.setTitle("Airplane Manager");
		temp = FileToolkit.readAirplanes();
		tailNumToAirplane = sortByValue(temp);
		temp = null;
		airplaneFrame.add(createBodyPanel() , BorderLayout.CENTER);
		airplaneFrame.setVisible(true);
	}
	
	// method converts HashMap to LinkedList, sorts by tailNumber, then returns a LinkedHashMap
	private HashMap<String, Airplane> sortByValue(HashMap<String, Airplane> airplanes) {

		List<Map.Entry<String, Airplane>> list = new LinkedList<Map.Entry<String, Airplane>>(airplanes.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Airplane>>() {

			@Override
			public int compare(Entry<String, Airplane> a1, Entry<String, Airplane> a2) {
				return (a1.getValue().getTailNumber()).compareTo(a2.getValue().getTailNumber());
			}
		});

		HashMap<String, Airplane> temp = new LinkedHashMap<String, Airplane>();
		for (Map.Entry<String, Airplane> ap : list) {
			temp.put(ap.getKey(), ap.getValue());
		}
		return temp;
	}
	
	@Override
	public JPanel createHeaderCenter() {
		JPanel panel = super.createHeaderCenter();
		JLabel titleLabel = new JLabel("Airplane Manager");
		titleLabel.setFont(UIConstants.TITLEFONT);
		titleLabel.setForeground(Color.WHITE);
		panel.add(titleLabel);
		return panel;
	}
	
	private JButton searchButton;
	
	@Override
	public JPanel createHeaderEast() {
		JPanel panel = super.createHeaderEast();
		JLabel searchLabel = new JLabel("Search by Tail ID:");
		searchLabel.setFont(UIConstants.BODYFONT);
		AirplaneUIListener listener = new AirplaneUIListener();
		searchButton = getSearchButton();
		searchButton.addActionListener(listener);
		panel.add(searchLabel, FlowLayout.LEFT);
		panel.add(searchButton);
		return panel;
	}
	
	@Override
	public JPanel createHeaderSouth() {
		JLabel[] headers = {
		 		new JLabel("Tail ID :"),
		 		new JLabel("Make :"),
		 		new JLabel("Model :"),
		 		new JLabel("Type :"),
		 		new JLabel("Tank Size(l) :"),
		 		new JLabel("Burn Rate(l/hr) :"),
		 		new JLabel("Air Speed(Kn) :"),
		 		new JLabel("Fuel Type:")
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
	
	// method creates panel which displays all airplanes and their information
	public JScrollPane createBodyPanel() {
		int counter = 0;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0 , 8 , 0 , 0));
		for(Map.Entry<String, Airplane> airplane : tailNumToAirplane.entrySet()) {
			JTextArea[] planeInfo = {
				new JTextArea(airplane.getValue().getTailNumber()),
				new JTextArea(airplane.getValue().getMake()),
				new JTextArea(airplane.getValue().getModel()),
				new JTextArea(airplane.getValue().getType()),
				new JTextArea(airplane.getValue().getTankSizeString()),
				new JTextArea(airplane.getValue().getBurnRateString()),
				new JTextArea(airplane.getValue().getAirSpeedString()),
				new JTextArea(airplane.getValue().getFuelType())
			};
			JPanel[] planeCells = {
				new JPanel(),
				new JPanel(),
				new JPanel(),
				new JPanel(),
				new JPanel(),
				new JPanel(),
				new JPanel(),
				new JPanel()
			};
			for(int i = 0; i < planeCells.length ; i++) {
				planeInfo[i].setLineWrap(true);
				planeInfo[i].setBackground(null);
				planeInfo[i].setFont(UIConstants.BODYFONT);
				planeCells[i].add(planeInfo[i]);
				if(counter % 2 != 0)
					planeCells[i].setBackground(UIConstants.BGCOLOR);
				panel.add(planeCells[i]);
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
		AirplaneUIListener listener = new AirplaneUIListener();
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
	
	private JTextField[] inputs = {
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
	private JRadioButton fuelTypeJaa;
	private JRadioButton fuelTypeAvgas;
	private ButtonGroup fuelTypeButtonGroup;
 	
	
	@Override
	public void createAddFrame() {
		clearInput();
		super.createAddFrame();
		JFrame addFrame = getAddFrame();
		addFrame.setTitle("Add Airplane");
 		JPanel panel = new JPanel();
 		panel.setLayout(new GridLayout(0, 2));
 		panel.setBorder(BorderFactory.createEmptyBorder(5, 30, 10, 30));
 		
 		fuelTypeJaa = new JRadioButton("Jaa");
 		fuelTypeAvgas = new JRadioButton("Avgas");
 		fuelTypeButtonGroup = new ButtonGroup();
 		fuelTypeButtonGroup.add(fuelTypeJaa);
 		fuelTypeButtonGroup.add(fuelTypeAvgas);
 		
		JLabel[] headers = {
		 		new JLabel("Tail ID :"),
		 		new JLabel("Make :"),
		 		new JLabel("Model :"),
		 		new JLabel("Type :"),
		 		new JLabel("Tank Size(l) :"),
		 		new JLabel("Burn Rate(l/hr) :"),
		 		new JLabel("Air Speed(Kn) :"),
		 		new JLabel("Min Runway Length:"),
		 		new JLabel("Fuel Type:")
		 };
 		
 		for(int i = 0 ; i < headers.length; i++) {
				headers[i].setFont(UIConstants.TEXTFONT);
				headers[i].setBorder(BorderFactory.createEmptyBorder());
				JPanel labelCell = new JPanel(new BorderLayout());
				JPanel inputCell = new JPanel();
				if (i < 8) {
					labelCell.add(headers[i], BorderLayout.WEST);
					inputCell.add(inputs[i]);
				}
				else if ( i == 8) {
					labelCell.add(headers[i], BorderLayout.WEST);
					inputCell.add(fuelTypeJaa);
					inputCell.add(fuelTypeAvgas);
				}
				panel.add(labelCell);
				panel.add(inputCell);
			}
 		
 		AirplaneUIListener listener = new AirplaneUIListener();
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
	private String initialTailNum;
	
	@Override
	public void createModifyFrame(Object object) {
		clearInput();
		Airplane airplane = (Airplane) object;
		initialTailNum = airplane.getTailNumber();
		super.createModifyFrame(airplane);
		JFrame modifyFrame = getModifyFrame();
		modifyFrame.setTitle("Modify Airplane");
 		JPanel panel = new JPanel();
 		panel.setLayout(new GridLayout(0 , 2));
 		panel.setBorder(BorderFactory.createEmptyBorder(5, 30, 10, 30));
 		
 		fuelTypeJaa = new JRadioButton("Jaa");
 		fuelTypeAvgas = new JRadioButton("Avgas");
 		fuelTypeButtonGroup = new ButtonGroup();
 		fuelTypeButtonGroup.add(fuelTypeJaa);
 		fuelTypeButtonGroup.add(fuelTypeAvgas);
 		
		JLabel[] headers = {
		 		new JLabel("Tail ID :"),
		 		new JLabel("Make :"),
		 		new JLabel("Model :"),
		 		new JLabel("Type :"),
		 		new JLabel("Tank Size(l) :"),
		 		new JLabel("Burn Rate(l/hr) :"),
		 		new JLabel("Air Speed(Kn) :"),
		 		new JLabel("Min Runway Length:"),
		 		new JLabel("Fuel Type:")
		 };
 		
 		String[] modifyAirplaneText = {
 				airplane.getTailNumber(),
 				airplane.getMake(),
 				airplane.getModel(),
 				airplane.getType(),
 				airplane.getTankSizeString(),
 				airplane.getBurnRateString(),
 				airplane.getAirSpeedString(),
 				airplane.getMinRunwayLengthString()
		 	};
			
			for(int i = 0 ; i < headers.length; i++) {
				headers[i].setFont(UIConstants.TEXTFONT);
				headers[i].setBorder(BorderFactory.createEmptyBorder());
				JPanel labelCell = new JPanel(new BorderLayout());
				JPanel inputCell = new JPanel();
				if (i < 8) {
					inputs[i].setText(modifyAirplaneText[i]);
					labelCell.add(headers[i], BorderLayout.WEST);
					inputCell.add(inputs[i]);
				}
				else if (i == 8) {
					if (airplane.getFuelType().equals("Jaa"))
						fuelTypeJaa.setSelected(true);
					if (airplane.getFuelType().equals("Avgas"))
						fuelTypeAvgas.setSelected(true);
					labelCell.add(headers[i], BorderLayout.WEST);
					inputCell.add(fuelTypeJaa);
					inputCell.add(fuelTypeAvgas);
				}
				panel.add(labelCell);
				panel.add(inputCell);
			}
			
			
			AirplaneUIListener listener = new AirplaneUIListener();
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
 		if (fuelTypeButtonGroup != null)
 			fuelTypeButtonGroup.clearSelection();
 		inputs[0].requestFocus();
 	}
	
 	// nested class which handles button events
	private class AirplaneUIListener implements ActionListener {

		AirplaneManager manager = new AirplaneManager();
		
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
				airplaneFrame.dispose();
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
		
		// method extracts search textfield and calls display from manager class
		private void executeSearchButton() {
			String input = getSearchField().getText();
			manager.display(tailNumToAirplane, input);
		}
		
		// method prompts user, validates input, then creates a modify window
		private void executeModifyButton() {
			String input = JOptionPane.showInputDialog(null, "Enter Tail ID to modify:",
					"Modify Airplane", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(ValidationToolkit.validateAirplaneNumber(tailNumToAirplane, input))
				createModifyFrame(tailNumToAirplane.get(input));
			else
				modifyButton.doClick();
		}
		
		// method prompts user, validates input, then deletes the airplane from HashMap
		private void executeDeleteButton() {
			String input = JOptionPane.showInputDialog(null, "Enter Tail ID to delete:",
					"Delete Airplane", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(ValidationToolkit.validateAirplaneNumber(tailNumToAirplane, input)) {
				manager.delete(tailNumToAirplane, input);
				FileToolkit.writeAirplanes(tailNumToAirplane);
				resetWindow();
			}
			else
				deleteButton.doClick();
		}
		
		// method creates an add window, validates user input, then if
		// successful, writes the HashMap to file
		private void executeAddSaveButton() {
			String[] addInfo = getTextFromFields();
			if(ValidationToolkit.validateAddAirplane(addInfo)) {
				if (tailNumToAirplane.containsKey(addInfo[0])) {
					JOptionPane.showMessageDialog(null, "Tail ID already exists in database.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				manager.add(tailNumToAirplane, addInfo);
				FileToolkit.writeAirplanes(tailNumToAirplane);
				JFrame addFrame = getAddFrame();
				addFrame.dispose();
				JOptionPane.showMessageDialog(null, "Successfully added: " + addInfo[1] + "/" + addInfo[2] + "!", "Airplane saved",
						JOptionPane.PLAIN_MESSAGE);
				resetWindow();
			}
		}
		
		// method disposes of add window
		private void executeAddCancelButton() {
			JFrame frame = getAddFrame();
			frame.dispose();
		}
		
		// method creates a modify window, validates user input, then if
		// successful, writes the HashMap to file
		private void executeModifySaveButton() {
			String[] modifyInfo = getTextFromFields();
			if(ValidationToolkit.validateAddAirplane(modifyInfo)) {
				if(ValidationToolkit.validateModifyAirplane(tailNumToAirplane, modifyInfo, initialTailNum)) {
					manager.modify(tailNumToAirplane, modifyInfo, initialTailNum);
					FileToolkit.writeAirplanes(tailNumToAirplane);
					JFrame modifyFrame = getModifyFrame();
					modifyFrame.dispose();
					JOptionPane.showMessageDialog(null, "Successfully modified: " + modifyInfo[1] + "/" + modifyInfo[2] + "!", "Airplane saved",
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
		
		// method disposes of AirplaneUI window
		private void resetWindow() {
			JFrame frame = getFrame();
			frame.dispose();
			new AirplaneUI();
		}
		
		// method extracts information from all textfields and radio buttons in add / modify window
	 	private String[] getTextFromFields() {
	 		String fuelType = "";
				if(fuelTypeJaa.isSelected())
					fuelType = "Jaa";
				else if(fuelTypeAvgas.isSelected()) 
					fuelType = "Avgas";
				String[] info = {
					inputs[0].getText(),
					inputs[1].getText(),
					inputs[2].getText(),
					inputs[3].getText(),
					inputs[4].getText(),
					inputs[5].getText(),
					inputs[6].getText(),
					inputs[7].getText(),
					fuelType
				};
				return info;
	 	}
	}
}
