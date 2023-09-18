/*
 * Group 6: Welcher, Rogers, Nguyen
 * Login.java
 * This class creates a Login window with textfields that allow the user to enter
 * username and password. The user may click on the login button which will validate input
 * and allow / deny access to the system. The user's access level is passed to the main menu.
 */

package admin;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

import data.User;
import toolkit.FileToolkit;
import toolkit.ValidationToolkit;

public class Login {

	private JFrame loginFrame;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton exitButton;
	private HashMap<String, User> usernameToUser;
	
	// constructor reads from file to HashMap and constructs UI
	public Login() {
		
		usernameToUser = FileToolkit.readUserAndPass();
		
		Dimension frameSize = getFrameDimension();
		
		loginFrame = new JFrame("Login");
		loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loginFrame.setSize(frameSize);
		loginFrame.setMinimumSize(new Dimension(600, 300));
		loginFrame.setResizable(false);
		loginFrame.setLayout(new BorderLayout());
		
		JPanel northPanel = createNorthPanel();
		JPanel centerPanel = createCenterPanel();
		JPanel southPanel = createSouthPanel();
		
		loginFrame.add(northPanel, BorderLayout.NORTH);
		loginFrame.add(centerPanel, BorderLayout.CENTER);
		loginFrame.add(southPanel, BorderLayout.SOUTH);
		
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setVisible(true);
		loginFrame.getRootPane().setDefaultButton(loginButton);
	}
	
	// method creates a new Dimension based on user's screen size
	private Dimension getFrameDimension() {
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = (int)(screenDimension.width * 0.235);
		int frameH = (int)(screenDimension.height * 0.21);
		return new Dimension(frameW , frameH);
	}
	
	// method creates panel which contains the banner
	private JPanel createNorthPanel() {
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(BannerConstants.BANNER_COLOR);
		northPanel.setPreferredSize(new Dimension(25, 75));
		
		JLabel fpsLabel = new JLabel("Flight Planner System Login");
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
	
	// method creates the panel which contains the labels and textfields for user entry
	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 150));
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setHorizontalAlignment(JLabel.RIGHT);
		usernameField = new JTextField();
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setHorizontalAlignment(JLabel.RIGHT);
		passwordField = new JPasswordField();
		
		centerPanel.add(usernameLabel);
		centerPanel.add(usernameField);
		centerPanel.add(passwordLabel);
		centerPanel.add(passwordField);
		
		return centerPanel;
	}
	
	// method creates the panel which contains buttons that handle events
	private JPanel createSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(1, 0, 10, 10));
		southPanel.setBorder(BorderFactory.createEmptyBorder(5, 200, 5, 200));
		
		LoginListener listener = new LoginListener();
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(listener);
		exitButton = new JButton("Exit");
		exitButton.addActionListener(listener);
		
		southPanel.add(loginButton);
		southPanel.add(exitButton);
		return southPanel;
	}
	
	// method calls an anonymous instance of the class
	public static void main(String[] args) {
		new Login();
	}
	
	// nested class handles button events which allow / deny user access to the system and which allow user to exit program
	private class LoginListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == loginButton)
				executeLoginButton();
			else if(e.getSource() == exitButton)
				loginFrame.dispose();
		}
		
		// method extracts information from textfield, passes out the info for input validation, and
		// if successful, then calls the MainMenu class, passing in the user's access level
		private void executeLoginButton() {
			String username = usernameField.getText();
			String password = (new String(passwordField.getPassword()));
			if(ValidationToolkit.getLoginValidation(usernameToUser, username, password)) {
				User user = usernameToUser.get(username);
				loginFrame.dispose();
				new MainMenu(user);
			}
		}
	}
}
