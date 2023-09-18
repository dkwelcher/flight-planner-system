/*
 * Group 6: Welcher, Rogers, Nguyen
 * NavBeaconUI.java
 * This class creates an NavBeacon Manager window which displays the sorted nav beacons from file.
 * The window has a search bar which allows the user to search for nav beacons and display their information.
 * The window also has buttons which allow the user to input new nav beacons, modify existing nav beacons, and
 * delete nav beacons. Changes to the nav beacons database are automatically written to file upon completion.
 * This class extends the UITemplate class and builds upon the template.
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.*;

import data.NavBeacon;
import manager.NavBeaconManager;
import toolkit.FileToolkit;
import toolkit.ValidationToolkit;

public class NavBeaconUI extends UITemplate {

	private JFrame beaconFrame = getFrame();
	private HashMap<String, NavBeacon> temp;
	private HashMap<String, NavBeacon> icaoToBeacon;
	
	// constructor calls superclass constructor, creates UI, and reads from file to HashMap
	public NavBeaconUI() {
		super();
		beaconFrame.setTitle("Airport Manager");
		temp = FileToolkit.readNavBeacons();
		icaoToBeacon = sortByValue(temp);
		temp = null;
		beaconFrame.add(createBodyPanel() , BorderLayout.CENTER);
		beaconFrame.setVisible(true);
	}
	
	// method converts HashMap to LinkedList, sorts by ICAO, then returns a LinkedHashMap
	private HashMap<String, NavBeacon> sortByValue(HashMap<String, NavBeacon> beacons) {

		List<Map.Entry<String, NavBeacon>> list = new LinkedList<Map.Entry<String, NavBeacon>>(beacons.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, NavBeacon>>() {

			@Override
			public int compare(Entry<String, NavBeacon> a1, Entry<String, NavBeacon> a2) {
				return (a1.getValue().getIcao()).compareTo(a2.getValue().getIcao());
			}
		});

		HashMap<String, NavBeacon> temp = new LinkedHashMap<String, NavBeacon>();
		for (Map.Entry<String, NavBeacon> ap : list) {
			temp.put(ap.getKey(), ap.getValue());
		}
		return temp;
	}
	
	@Override
	public JPanel createHeaderCenter() {
		JPanel panel = super.createHeaderCenter();
		JLabel titleLabel = new JLabel("NavBeacon Manager");
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
		NavBeaconUIListener listener = new NavBeaconUIListener();
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
		 		new JLabel("Latitude :"),
		 		new JLabel("Longitude :"),
		 		new JLabel("Type :")
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
	
	// method creates panel which displays all nav beacons and their information
	private JScrollPane createBodyPanel() {
		int counter = 0;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0 , 5 , 0 , 0));
		for(Map.Entry<String, NavBeacon> beacon : icaoToBeacon.entrySet()) {
			JTextArea[] beaconInfo = {
				new JTextArea(beacon.getValue().getIcao()),
				new JTextArea(beacon.getValue().getName()),
				new JTextArea(beacon.getValue().getLatitudeString()),
				new JTextArea(beacon.getValue().getLongitudeString()),
				new JTextArea(beacon.getValue().getType()),
			};
			JPanel[] beaconCells = {
					new JPanel(),
					new JPanel(),
					new JPanel(),
					new JPanel(),
					new JPanel()
				};
			for(int i = 0; i < beaconCells.length ; i++) {
				beaconInfo[i].setLineWrap(true);
				beaconInfo[i].setBackground(null);
				beaconInfo[i].setFont(UIConstants.BODYFONT);
				beaconCells[i].add(beaconInfo[i]);
				if(counter % 2 != 0)
					beaconCells[i].setBackground(UIConstants.BGCOLOR);
				panel.add(beaconCells[i]);
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
		NavBeaconUIListener listener = new NavBeaconUIListener();
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
		addFrame.setSize(new Dimension(400, 250));
		addFrame.setTitle("Add NavBeacon");
 		JPanel panel = new JPanel();
 		panel.setLayout(new GridLayout(0 , 2));
 		panel.setBorder(BorderFactory.createEmptyBorder(5, 50, 10, 50));
 		
		JLabel[] headers = {
		 		new JLabel("ICAO :"),
		 		new JLabel("Name :"),
		 		new JLabel("Latitude :"),
		 		new JLabel("Longitude :"),
		 		new JLabel("Type :")
		 };
 		
		for(int i = 0 ; i < headers.length ; i++) {
				headers[i].setFont(UIConstants.TEXTFONT);
				headers[i].setBorder(BorderFactory.createEmptyBorder());
				JPanel labelCell = new JPanel(new BorderLayout());
				JPanel inputCell = new JPanel();
				labelCell.add(headers[i], BorderLayout.WEST);
				inputCell.add(inputs[i]);
				panel.add(labelCell);
				panel.add(inputCell);
		}
		NavBeaconUIListener listener = new NavBeaconUIListener();
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
		NavBeacon navBeacon = (NavBeacon) object;
		initialIcao = navBeacon.getIcao();
		super.createModifyFrame(navBeacon);
		JFrame modifyFrame = getModifyFrame();
		modifyFrame.setSize(new Dimension(400, 250));
		modifyFrame.setTitle("Modify NavBeacon");
 		JPanel panel = new JPanel();
 		panel.setLayout(new GridLayout(0 , 2));
 		panel.setBorder(BorderFactory.createEmptyBorder(5, 50, 10, 50));
 		
		JLabel[] headers = {
		 		new JLabel("ICAO :"),
		 		new JLabel("Name :"),
		 		new JLabel("Latitude :"),
		 		new JLabel("Longitude :"),
		 		new JLabel("Type :")
		 };
 		
 		String[] modifyNavBeaconText = {
 				navBeacon.getIcao(),
 				navBeacon.getName(),
 				navBeacon.getLatitudeString(),
 				navBeacon.getLongitudeString(),
 				navBeacon.getType()
		 	};
			
			for(int i = 0 ; i < headers.length ; i++) {
				headers[i].setFont(UIConstants.TEXTFONT);
				headers[i].setBorder(BorderFactory.createEmptyBorder());
				inputs[i].setText(modifyNavBeaconText[i]);
				JPanel labelCell = new JPanel(new BorderLayout());
				JPanel inputCell = new JPanel();
				labelCell.add(headers[i], BorderLayout.WEST);
				inputCell.add(inputs[i]);
				panel.add(labelCell);
				panel.add(inputCell);
			}
			NavBeaconUIListener listener = new NavBeaconUIListener();
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
	
	// method clears all textfields
 	private void clearInput() {
 		for(JTextField input : inputs)
 			input.setText("");
 		inputs[0].requestFocus();
 	}
	
 	// nested class handles button events
	private class NavBeaconUIListener implements ActionListener {

		NavBeaconManager manager = new NavBeaconManager();
		
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
				beaconFrame.dispose();
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
		
		// method extracts information from textfield and calls display from manager class
		private void executeSearchButton() {
			String input = getSearchField().getText();
			manager.display(icaoToBeacon, input);
		}
		
		// method prompts user, validates input, then creates a modify window if successful
		private void executeModifyButton() {
			String input = JOptionPane.showInputDialog(null, "Enter NavBeacon to modify:",
					"Modify NavBeacon", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(ValidationToolkit.validateBeaconIcao(icaoToBeacon, input.toUpperCase()))
				createModifyFrame(icaoToBeacon.get(input.toUpperCase()));
			else
				modifyButton.doClick();
		}
		
		// method prompts user, validates input, then deletes nav beacon from HashMap
		private void executeDeleteButton() {
			String input = JOptionPane.showInputDialog(null, "Enter NavBeacon to delete:",
					"Delete NavBeacon", JOptionPane.PLAIN_MESSAGE);
			if(input == null) return;
			if(ValidationToolkit.validateBeaconIcao(icaoToBeacon, input.toUpperCase())) {
				manager.delete(icaoToBeacon, input.toUpperCase());
				FileToolkit.writeNavBeacons(icaoToBeacon);
				resetWindow();
			}
			else
				deleteButton.doClick();
		}
		
		// methods extracts information from textfields, validates the info, then
		// writes HashMap to file if successful
		private void executeAddSaveButton() {
			String[] addInfo = getTextFromFields();
			if(ValidationToolkit.validateAddBeacon(addInfo)) {
				if (icaoToBeacon.containsKey(addInfo[0])) {
					JOptionPane.showMessageDialog(null, "ICAO already exists in database.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				manager.add(icaoToBeacon, addInfo);
				FileToolkit.writeNavBeacons(icaoToBeacon);
				JFrame addFrame = getAddFrame();
				addFrame.dispose();
				JOptionPane.showMessageDialog(null, "Successfully added: " + addInfo[1] + "!", "NavBeacon saved",
						JOptionPane.PLAIN_MESSAGE);
				resetWindow();
			}
		}
		
		// methods disposes of add window
		private void executeAddCancelButton() {
			JFrame frame = getAddFrame();
			frame.dispose();
		}
		
		// method extracts information from textfields, validates the info, then writes
		// HashMap to file if successful
		private void executeModifySaveButton() {
			String[] modifyInfo = getTextFromFields();
			if(ValidationToolkit.validateAddBeacon(modifyInfo)) {
				if(ValidationToolkit.validateModifyBeacon(icaoToBeacon, modifyInfo, initialIcao)) {
					manager.modify(icaoToBeacon, modifyInfo, initialIcao);
					FileToolkit.writeNavBeacons(icaoToBeacon);
					JFrame modifyFrame = getModifyFrame();
					modifyFrame.dispose();
					JOptionPane.showMessageDialog(null, "Successfully modified: " + modifyInfo[1] + "!", "NavBeacon saved",
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
		
		// method dispose of window then calls a new instance of NavBeaconUI
		private void resetWindow() {
			JFrame frame = getFrame();
			frame.dispose();
			new NavBeaconUI();
		}
		
		// method clears all textfields
	 	private String[] getTextFromFields() {
				String[] info = {
					inputs[0].getText().toUpperCase(),
					inputs[1].getText(),
					inputs[2].getText(),
					inputs[3].getText(),
					inputs[4].getText().toUpperCase()
				};
				return info;
	 	}
	}
}
