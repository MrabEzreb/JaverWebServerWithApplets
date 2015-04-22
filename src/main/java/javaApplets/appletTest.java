package javaApplets;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class appletTest extends Applet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1104812228514559016L;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(0,0,250,100);
		g.setColor(Color.blue);
		g.drawString("Look at me, I'm a Java Applet!",10,50);
	}
}
