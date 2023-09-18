/*
 * Group 6: Welcher, Rogers, Nguyen
 * UITemplate.java
 * This superclass provides a UI template for any Manager-type UIs.
 * This superclass creates the outpanels, search bar, and buttons.
 */

package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class UITemplate {

	private JFrame mainFrame;
	
	// constructor creates the UI
	public UITemplate() {
		
		Dimension frameSize = getDimension();
		mainFrame = new JFrame();
		mainFrame.setSize(frameSize);
		mainFrame.setMinimumSize(new Dimension(1800, 1000));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.add(createHeader() , BorderLayout.NORTH);
		mainFrame.add(createSidePanel() , BorderLayout.EAST);
		mainFrame.add(createSidePanel() , BorderLayout.WEST);
		mainFrame.add(createFooter() , BorderLayout.SOUTH);
		mainFrame.setVisible(true);
	}
	
	// method allows subclasses to procure the frame
	public JFrame getFrame() {
		return mainFrame;
	}
	
	// method returns new Dimension based on user's screen size
	public Dimension getDimension() {
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = (int)(screenDimension.width * 0.7);
		int frameH = (int)(screenDimension.height * 0.7);
		return new Dimension(frameW , frameH);
	}
	
	private int panelW;
	private int panelH;
	private int sideW;
	private int sideH;
	private Dimension sideSize;
	private Dimension panelSize;
	
	// method creates the panel which holds additional panels
	public JPanel createHeader() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(UIConstants.BGCOLOR);
		panelW = mainFrame.getWidth();
		panelH = (int)(mainFrame.getHeight() * 0.15);
		panelSize = new Dimension(panelW , panelH);
		panel.setPreferredSize(panelSize);
		sideH = (int)(panelH * 0.75);
		sideW = (int)(panelW * 0.15);
		
		panel.add(createHeaderWest(sideW , sideH) , BorderLayout.WEST);
		panel.add(createHeaderCenter() , BorderLayout.CENTER);
		panel.add(createHeaderEast() , BorderLayout.EAST);
		panel.add(createHeaderSouth() , BorderLayout.SOUTH);
		
		return panel;
	}
	
	// method creates the panel which displays the logo
	public JPanel createHeaderWest(int sideW, int sideH) {
		JPanel panel = new JPanel();
		panel.setBackground(UIConstants.BGCOLOR);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.setPreferredSize(sideSize);
		panel.setBackground(UIConstants.BGCOLOR);
		JLabel logoLabel = new JLabel(new ImageIcon(getImage()));
		panel.add(logoLabel);
		return panel;
	}
	
	// method returns a readable image
	public Image getImage() {
		String image = UIConstants.LOGO;
		try {
			BufferedImage logo = ImageIO.read(new File(image));
			return logo.getScaledInstance(sideW, sideH, Image.SCALE_DEFAULT);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	// method creates the panel which is a background
 	public JPanel createHeaderCenter() {
		JPanel panel = new JPanel();
		panel.setBackground(UIConstants.BGCOLOR);
		return panel;
	}
	
	private JButton searchButton;
	private JTextField searchField;
	
	// method allows the superclass to procure the search button
	public JButton getSearchButton() {
		return searchButton;
	}
	
	// method allows the superclass to procure the search field
	public JTextField getSearchField() {
		return searchField;
	}
	
	// method creates the panel which displays the search textfield and button
	public JPanel createHeaderEast() {
		JPanel panel = new JPanel();
		panel.setBackground(UIConstants.BGCOLOR);
		searchField = new JTextField(10);
		searchButton = new JButton("Search");
		searchButton.setFont(UIConstants.BODYFONT);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.setPreferredSize(sideSize);
		panel.add(searchField);
		panel.add(searchButton);
		return panel;
	}
	
	// method creates the panel which is a background
	public JPanel createHeaderSouth() {

		JPanel panel = new JPanel();
		panel.setBackground(UIConstants.BGCOLOR);
		panel.setBorder(BorderFactory.createMatteBorder(0, sideW, 0, sideW, UIConstants.BGCOLOR));
		return panel;
	}
	
	// method creates the panel which is a background
	public JPanel createSidePanel() {
		
		sideH = (int) (mainFrame.getHeight() * 0.75);
		sideW = (int) (mainFrame.getWidth() * 0.15);
		sideSize = new Dimension(sideW , sideH);
		
		JPanel panel =  getSidePanel(sideW, sideH);
		
		panel.setBackground(UIConstants.BGCOLOR);
		panel.setPreferredSize(sideSize);
		
		return panel;
	}
	
	// method creates the side panels which display the image
	private JPanel getSidePanel(int sideW , int sideH) {
		JPanel panel = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
        	public void paintComponent(Graphics g) {
            	super.paintComponent(g);
            	BufferedImage planeImg;
				try {
					planeImg = ImageIO.read(new File(UIConstants.PLANE));
					Image beacon = planeImg.getScaledInstance(sideW, sideH, Image.SCALE_DEFAULT);
	            	g.drawImage(beacon, 0, 0, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		};
		return panel;
	}
	
	private JButton addButton;
	private JButton modifyButton;
	private JButton deleteButton;
	private JButton exitButton;
	
	// methods allow subclass to procure these buttons
	public JButton getAddButton() {
		return addButton;
	}
	
	public JButton getModifyButton() {
		return modifyButton;
	}
	
	public JButton getDeleteButton() {
		return deleteButton;
	}
	
	public JButton getExitButton() {
		return exitButton;
	}
	
	// method creates the panel which holds the buttons that handle events
	public JPanel createFooter() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panelW = mainFrame.getWidth();
		panelH = (int)(mainFrame.getHeight() * 0.1);
		panelSize = new Dimension(panelW , panelH);
		panel.setPreferredSize(panelSize);
		
		sideW = (int) (panelW * 0.15);
		sideH = (int) (panelH * 0.2);
		panel.setLayout(new GridLayout(1 , 0 , 15 , 15));
		panel.setBorder(BorderFactory.createMatteBorder(sideH, sideW, sideH, sideW, Color.WHITE));
		
		addButton = new JButton("Add");
		modifyButton = new JButton("Modify");
		deleteButton = new JButton("Delete");
		exitButton = new JButton("Exit");
		JButton[] footerButtons = {
			addButton,
			modifyButton,
			deleteButton,
			exitButton
		};
		
		for(JButton btn : footerButtons) {
			btn.setFont(UIConstants.BODYFONT);
			panel.add(btn);
		}
		
		return panel;
	}
	
 	private JFrame addFrame;
 	private JButton addSaveButton;
 	private JButton addClearButton;
 	private JButton addCancelButton;
 	
 	// methods allow the subclasses to procure these components
 	public JFrame getAddFrame() {
		return addFrame;
	}
 	
 	public JButton getAddSaveButton() {
		return addSaveButton;
	}
 	
 	public JButton getAddClearButton() {
		return addClearButton;
	}
 	
 	public JButton getAddCancelButton() {
		return addCancelButton;
	}
	
 	// method creates the add window and populates it with components
 	public void createAddFrame() {
 		
 		addFrame = new JFrame();
 		
 		panelH = (int)(mainFrame.getHeight() * 0.3);
 		panelW = (int)(mainFrame.getWidth() * 0.2);
 		panelSize = new Dimension(panelW , panelH);
 		
 		addFrame.setSize(panelSize);
 		addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 		addFrame.setResizable(false);
 		
 		JPanel footerPanel = new JPanel();
 		footerPanel.setLayout(new GridLayout(1 , 0 , 5 , 5));
 		addSaveButton = new JButton("Save");
 		addClearButton = new JButton("Clear");
 		addCancelButton = new JButton("Cancel");
 		JButton[] addButtons = {
 			addSaveButton,
 			addClearButton,
 			addCancelButton
 		};
 		for(JButton btn : addButtons) {
 			btn.setFont(UIConstants.BODYFONT);
 			footerPanel.add(btn);
 		}
 		addFrame.add(footerPanel , BorderLayout.SOUTH);
 		
 		addFrame.setVisible(true);
 	}
 	
 	private JFrame modifyFrame;
 	private JButton modifySaveButton;
 	private JButton modifyClearButton;
 	private JButton modifyCancelButton;
 	
 	// methods allow the subclasses to procure these components
 	public JFrame getModifyFrame() {
		return modifyFrame;
	}
 	
 	public JButton getModifySaveButton() {
		return modifySaveButton;
	}
 	
 	public JButton getModifyClearButton() {
		return modifyClearButton;
	}
 	
 	public JButton getModifyCancelButton() {
		return modifyCancelButton;
	}
 	
 	// method creates the modify frame and populates it with components
 	public void createModifyFrame(Object object) {
		modifyFrame = new JFrame();
		modifyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		modifyFrame.setResizable(false);

		panelH = (int) (mainFrame.getHeight() * 0.3);
		panelW = (int) (mainFrame.getWidth() * 0.2);
		panelSize = new Dimension(panelW, panelH);

		modifyFrame.setSize(panelSize);

		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new GridLayout(1, 0, 5, 5));
		modifySaveButton = new JButton("Save");
		modifyClearButton = new JButton("Clear");
		modifyCancelButton = new JButton("Cancel");
		JButton[] modifyButtons = { modifySaveButton, modifyClearButton, modifyCancelButton };
		for (JButton btn : modifyButtons) {
			btn.setFont(UIConstants.BODYFONT);
			footerPanel.add(btn);
		}
		modifyFrame.add(footerPanel, BorderLayout.SOUTH);
		modifyFrame.setVisible(true);
 	}
}
