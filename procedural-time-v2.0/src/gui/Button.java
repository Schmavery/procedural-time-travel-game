package gui;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;
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

public class Button implements Element{
	private String text;
	private LinkedList<String> actions;
	private Color color;
	int cornerRadius = 0;
	private Rectangle boundingBox;
	
	public Button(String text, Rectangle box, String action){
		this.text = text;
		this.boundingBox = box;
		actions = new LinkedList<String>();
		actions.add(action);
	}
	
	public void setColor(Color c){
		this.color = c;
	}
	public void setText(String t){
		this.text = t;
	}
	public void addAction(String a){
		this.actions.add(a);
	}
	
	/**
	 * On a click event, returns all actions triggered by this button.
	 * (Generally there should only be one as order of actions is
	 *  not guaranteed)
	 * @return List of actions;
	 */
	public List<String> click(int x, int y){
		return actions;
	}
	
	public void draw(){
		
	}
	
	//public String getAction(){ return action; }
	public String getText(){ return text; }
	public Color getColor(){ return color; }
	public Rectangle getRect(){ return boundingBox; }
	
}
