/*
 * Group 6: Welcher, Rogers, Nguyen
 * UIConstants.java
 * This class provides packaged constants for building UIs
 * in the gui package.
 */

package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

final class UIConstants {

	static final String LOGO = "res/img/logo.png";
	static final String PLANE = "res/img/plane.jpg";
	static final Font TITLEFONT = new Font("Verdana", Font.BOLD, 32);
	static final Color BGCOLOR = new Color(52, 153, 235);
	static final Font BODYFONT = new Font("Arial", Font.BOLD, 15);
	static final Font TEXTFONT = new Font("Arial", Font.PLAIN, 16);
	static final Border BORDER = BorderFactory.createMatteBorder(3,2,3,2,Color.BLACK);
	static final Font DISCLAIMER_FONT = new Font("Arial", Font.BOLD, 10);
	
	private UIConstants() {}
}
