/*
 * Group 6: Welcher, Rogers, Nguyen
 * Registration.java
 * This class creates a Registration window and allows the user to create a
 * new user in the system with a specified access level. The system will generate
 * a username based on the information provided. If the username generated is not
 * unique, then the system will continue appending identifiers until it is unique.
 */

package admin;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

import data.User;
import toolkit.FileToolkit;
import toolkit.ValidationToolkit;

public class Registration {

	private JFrame registrationFrame;
	private JComboBox<String> accessLevelBox;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JButton registerButton;
	private JButton clearButton;
	private JButton exitButton;
	private HashMap<String, User> usernameToUser;
	
	// constructor creates UI and reads from file to HashMap
	public Registration() {
		
		usernameToUser = FileToolkit.readUserAndPass();
		
		Dimension frameSize = getFrameDimension();
		
		registrationFrame = new JFrame("Registration");
		registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		registrationFrame.setSize(frameSize);
		registrationFrame.setMinimumSize(new Dimension(600, 350));
		registrationFrame.setResizable(false);
		registrationFrame.setLayout(new BorderLayout());
		
		JPanel northPanel = createNorthPanel();
		JPanel centerPanel = createCenterPanel();
		JPanel southPanel = createSouthPanel();
		
		registrationFrame.add(northPanel, BorderLayout.NORTH);
		registrationFrame.add(centerPanel, BorderLayout.CENTER);
		registrationFrame.add(southPanel, BorderLayout.SOUTH);
		
		registrationFrame.setLocationRelativeTo(null);
		registrationFrame.setVisible(true);
		registrationFrame.getRootPane().setDefaultButton(registerButton);
	}
	
	// method creates new Dimension based on user's screen size
	private Dimension getFrameDimension() {
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = (int)(screenDimension.width * 0.235);
		int frameH = (int)(screenDimension.height * 0.25);
		return new Dimension(frameW , frameH);
	}
	
	// method creates the panel which contains the banner
	private JPanel createNorthPanel() {
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(BannerConstants.BANNER_COLOR);
		northPanel.setPreferredSize(new Dimension(25, 75));
		
		JLabel fpsLabel = new JLabel("Registration");
		fpsLabel.setFont(BannerConstants.TITLE_FONT);
		fpsLabel.setForeground(Color.WHITE);
		fpsLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel disclaimerLabel = new JLabel("DISCLAIMER: THIS SOFTWARE IS NOT TO BE USED FOR FLIGHT PLANNING OR NAVIGATIONAL PURPOSE");
		disclaimerLabel.setFont(BannerConstants.DISCLAIMER_FONT);
		disclaimerLabel.setForeground(Color.WHITE);
		disclaimerLabel.setHorizontalAlignment(JLabel.CENTER);
		
		northPanel.add(fpsLabel, BorderLayout.CENTER);
		northPanel.add(disclaimerLabel, BorderLayout.SOUTH);
		return northPanel;
	}
	
	// method creates the panel which displays a combo box and textfields for user input
	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel(new GridLayout(0, 2, 10, 10));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 150));
		
		JLabel accessLevelLabel = new JLabel("Access Level:");
		accessLevelLabel.setHorizontalAlignment(JLabel.RIGHT);
		accessLevelBox = new JComboBox<String>();
		accessLevelBox.addItem(AccessLevel.ADMIN.toString());
		accessLevelBox.addItem(AccessLevel.HR.toString());
		accessLevelBox.addItem(AccessLevel.DATA_ENTRY.toString());
		accessLevelBox.addItem(AccessLevel.PLANNER.toString());
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setHorizontalAlignment(JLabel.RIGHT);
		firstNameField = new JTextField();
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setHorizontalAlignment(JLabel.RIGHT);
		lastNameField = new JTextField();
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setHorizontalAlignment(JLabel.RIGHT);
		passwordField = new JPasswordField();
		
		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
		confirmPasswordLabel.setHorizontalAlignment(JLabel.RIGHT);
		confirmPasswordField = new JPasswordField();
		
		centerPanel.add(accessLevelLabel);
		centerPanel.add(accessLevelBox);
		centerPanel.add(firstNameLabel);
		centerPanel.add(firstNameField);
		centerPanel.add(lastNameLabel);
		centerPanel.add(lastNameField);
		centerPanel.add(passwordLabel);
		centerPanel.add(passwordField);
		centerPanel.add(confirmPasswordLabel);
		centerPanel.add(confirmPasswordField);
		return centerPanel;
	}
	
	// method creates buttons that handle events
	private JPanel createSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(1, 0, 5, 5));
		southPanel.setBorder(BorderFactory.createEmptyBorder(5, 150, 5, 150));
		
		RegistrationListener listener = new RegistrationListener();
		
		registerButton = new JButton("Register");
		registerButton.addActionListener(listener);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(listener);
		exitButton = new JButton("Exit");
		exitButton.addActionListener(listener);
		
		southPanel.add(registerButton);
		southPanel.add(clearButton);
		southPanel.add(exitButton);
		return southPanel;
	}
	
	// nested class handles button events
	private class RegistrationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == registerButton)
				executeRegisterButton();
			else if(e.getSource() == clearButton)
				clearTextFields();
			else if(e.getSource() == exitButton)
				registrationFrame.dispose();
		}
		
		// method extracts information from textfield, validates user input, and
		// if successful, then calls registerUser() for further processing
		private void executeRegisterButton() {
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String password = new String(passwordField.getPassword());
			String confirmPassword = new String(confirmPasswordField.getPassword());
			
			if(ValidationToolkit.getRegistrationValidation(firstName, lastName, password, confirmPassword))
				registerUser();
		}
		
		// method instantiates new user and writes them to file, then disposes window
		private void registerUser() {
			String username = getUsername();
			String password = new String(confirmPasswordField.getPassword());
			AccessLevel access = AccessLevel.valueOf((String) accessLevelBox.getSelectedItem());
			User user = new User(username, password, access);
			usernameToUser.put(username, user);
			FileToolkit.writeUserAndPass(usernameToUser);
			registrationFrame.dispose();
			JOptionPane.showMessageDialog(null, username + " has been registered!", "Registration successful", JOptionPane.PLAIN_MESSAGE);
		}
		
		// method extracts username from textfields and sends to generateUsername()
		private String getUsername() {
			String firstInitial = getInitial(firstNameField);
			String lastName = formatLastName(lastNameField);
			return generateUsername(firstInitial + lastName);
		}
		
		// method extracts first letter from textfield
		private String getInitial(JTextField textfield) {
			return textfield.getText().trim().toLowerCase().substring(0, 1);
		}
		
		// method formats textfield to lower case
		private String formatLastName(JTextField textfield) {
			return textfield.getText().trim().toLowerCase();
		}
		
		// recursive method which continues generating and appending identifiers
		// to ensure generated username is unique
		private String generateUsername(String username) {
			if (!usernameToUser.containsKey(username))
				return username;
			else {
				String identifier = generateIdentifier();
				username += identifier;
				return generateUsername(username);
			}
		}
		
		// method generates an identifier between 0 and 9
		private String generateIdentifier() {
			StringBuilder sb = new StringBuilder();
			int identifier = (int) (Math.random() * 9) + 1;
			sb.append(identifier);
			return sb.toString();
		}
		
		// method clears all textfields
		private void clearTextFields() {
			firstNameField.setText("");
			lastNameField.setText("");
			passwordField.setText("");
			confirmPasswordField.setText("");
			firstNameField.requestFocus();
		}
	}
}
