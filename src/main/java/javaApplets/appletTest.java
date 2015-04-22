package javaApplets;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class appletTest extends Applet {

	@Override
	public void paint(Graphics g) {
		g.drawRect(0,0,250,100);
		g.setColor(Color.blue);
		g.drawString("Look at me, I'm a Java Applet!",10,50);
	}
}
