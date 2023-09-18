/*
 * Group 6: Welcher, Rogers, Nguyen
 * MainMenu.java
 * This class creates a MainMenu window and stores the user's access level.
 * Each button is access restricted by the user's access level. Each button
 * takes the user to a subsystem if authorized.
 */

package admin;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import data.User;
import gui.AirplaneUI;
import gui.AirportUI;
import gui.FlightPlannerUI;
import gui.NavBeaconUI;

public class MainMenu {

	private JFrame menuFrame;
	private JButton flightPlannerButton;
	private JButton airportManagerButton;
	private JButton navBeaconManagerButton;
	private JButton airplaneManagerButton;
	private JButton registerButton;
	private JButton logoutButton;
	private User user;
	
	// constructor constructs the UI
	public MainMenu(User user) {
		
		this.user = user;
		
		Dimension frameSize = getFrameDimension();
		
		menuFrame = new JFrame("Main Menu");
		menuFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		menuFrame.setSize(frameSize);
		menuFrame.setMinimumSize(new Dimension(1000, 450));
		menuFrame.setResizable(false);
		menuFrame.setLayout(new BorderLayout());
		
		JPanel northPanel = createNorthPanel();
		JPanel centerPanel = createCenterPanel();
		JPanel southPanel = createSouthPanel();
		
		menuFrame.add(northPanel, BorderLayout.NORTH);
		menuFrame.add(centerPanel, BorderLayout.CENTER);
		menuFrame.add(southPanel, BorderLayout.SOUTH);
		
		menuFrame.setLocationRelativeTo(null);
		menuFrame.setVisible(true);
		menuFrame.getRootPane().setDefaultButton(flightPlannerButton);
	}
	
	// method creates a new Dimension based on user's screen size
	private Dimension getFrameDimension() {
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = (int)(screenDimension.width * 0.381);
		int frameH = (int)(screenDimension.height * 0.3);
		return new Dimension(frameW , frameH);
	}
	
	// method creates a panel which contains the banner
	private JPanel createNorthPanel() {
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(BannerConstants.BANNER_COLOR);
		northPanel.setPreferredSize(new Dimension(25, 75));
		
		JLabel fpsLabel = new JLabel("Flight Planner System");
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
	
	// method creates the panel which contains the logo
	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
		
		String logo = "res/img/logo.png";
		Image image = getImage(logo);
		Image resizedImage = image.getScaledInstance(250, 150, Image.SCALE_DEFAULT);
		JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
		centerPanel.add(imageLabel);
		return centerPanel;
	}
	
	// method creates the panel which contains the buttons that handle events
	private JPanel createSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(1, 0, 5, 5));
		southPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		MainMenuListener listener = new MainMenuListener();
		
		flightPlannerButton = new JButton("Flight Planner");
		flightPlannerButton.addActionListener(listener);
		airportManagerButton = new JButton("Airport Manager");
		airportManagerButton.addActionListener(listener);
		navBeaconManagerButton = new JButton("Nav Beacon Manager");
		navBeaconManagerButton.addActionListener(listener);
		airplaneManagerButton = new JButton("Airplane Manager");
		airplaneManagerButton.addActionListener(listener);
		registerButton = new JButton("Register");
		registerButton.addActionListener(listener);
		logoutButton = new JButton("Logout");
		logoutButton.addActionListener(listener);
		
		southPanel.add(flightPlannerButton);
		southPanel.add(airportManagerButton);
		southPanel.add(navBeaconManagerButton);
		southPanel.add(airplaneManagerButton);
		southPanel.add(registerButton);
		southPanel.add(logoutButton);
		return southPanel;
	}
	
	// method ensures image is readable
	private BufferedImage getImage(String imageFile) {
		try {
			return ImageIO.read(new File(imageFile));
		}
		catch(IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException: Can't read image", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	// nested class handles button events which allow / deny user access to other subsystems
	private class MainMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == flightPlannerButton)
				executeFlightPlannerButton();
			else if(e.getSource() == airportManagerButton)
				executeAirportManagerButton();
			else if(e.getSource() == navBeaconManagerButton)
				executeNavBeaconManagerButton();
			else if(e.getSource() == airplaneManagerButton)
				executeAirplaneManagerButton();
			else if(e.getSource() == registerButton)
				executeRegisterButton();
			else if(e.getSource() == logoutButton) {
				menuFrame.dispose();
				new Login();
			}
		}
		
		// method authorizes access and if successful, then calls an instance of the appropriate class
		private void executeFlightPlannerButton() {
			if (user.getAccessLevel() == AccessLevel.ADMIN || user.getAccessLevel() == AccessLevel.PLANNER)
				new FlightPlannerUI();
			else
				displayErrorMessage();
		}
		
		// method authorizes access and if successful, then calls an instance of the appropriate class
		private void executeAirportManagerButton() {
			if (getManagerClassAccess())
				new AirportUI();
			else
				displayErrorMessage();
		}
		
		// method authorizes access and if successful, then calls an instance of the appropriate class
		private void executeNavBeaconManagerButton() {
			if (getManagerClassAccess())
				new NavBeaconUI();
			else
				displayErrorMessage();
		}
		
		// method authorizes access and if successful, then calls an instance of the appropriate class
		private void executeAirplaneManagerButton() {
			if (getManagerClassAccess())
				new AirplaneUI();
			else
				displayErrorMessage();
		}
		
		// method authorizes access and if successful, then calls an instance of the appropriate class
		private void executeRegisterButton() {
			if (user.getAccessLevel() == AccessLevel.ADMIN || user.getAccessLevel() == AccessLevel.HR)
				new Registration();
			else
				displayErrorMessage();
		}
		
		// method displays error message if user denied access to a subsystem
		private void displayErrorMessage() {
			JOptionPane.showMessageDialog(null, "You are not authorized to access this system.", "Access denied", JOptionPane.ERROR_MESSAGE);
		}
		
		// method determines if user can access any of the manager subsystems
		private boolean getManagerClassAccess() {
			return (user.getAccessLevel() == AccessLevel.ADMIN || user.getAccessLevel() == AccessLevel.DATA_ENTRY);
		}
	}
}
