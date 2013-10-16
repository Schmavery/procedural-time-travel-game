package gui;

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.Color;
/**
 * 
 * @author avery
 *
 * This is a generic button class designed to be put on a Panel object.
 * The action string is used with Reflection by the Panel to call a
 * method specific to this button in the Panel object.
 * 
 * Alternative potential design: call an "actionPerformed" method and pass
 * in a string/action object?
 * 
 * Future features: 
 *  - Borders - (Decorator design pattern?)
 */

public class Button {
	private String text;
	private String action;
	private Color color;
	int cornerRadius = 0;
	private Rectangle boundingBox;
	
	public Button(String text, Rectangle box){
		this.text = text;
		this.boundingBox = box;
	}
	
	public void setColor(Color c){
		this.color = c;
	}
	public void setText(String t){
		this.text = t;
	}
	public void setAction(String a){
		this.action = a;
	}
	
	public String getAction(){ return action; }
	public String getText(){ return text; }
	public Color getColor(){ return color; }
	public Rectangle getRect(){ return boundingBox; }
	
}
