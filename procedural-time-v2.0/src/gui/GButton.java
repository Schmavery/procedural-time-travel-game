package gui;

import gui.GClickEvent.EventType;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;
/**
 * 
 * @author avery
 *
 * This is a generic button class designed to be put on a GPanel object.
 */

public class GButton extends GComponent{
	private String text;
	private Color textColor;
	private EventType type = EventType.BUTTON;
	
	public GButton(String name, String text, String action){
		super(name, action);
		this.text = text;
		this.textColor = new Color(0, 0, 0);
	}
	
	public GButton(String name, String text, String action, int posX, int posY){
		super(name, action, null);
		Rectangle rect = new Rectangle(posX, posY, text.length()*16 + 32, 48);
		setRect(rect);
		this.text = text;
		this.textColor = new Color(0, 0, 0);
	}
	
	public GButton(String name, String text, String action, Rectangle box){
		super(name, action, box);
		this.text = text;
	}
	
	public void setTextColor(Color c){
		this.textColor = c;
	}
	
	public void setText(String t){
		this.text = t;
	}
	
	public GClickEvent clickUp(int x, int y){
		super.clickUp(x, y);
		if (getRect().contains(x, y) && isClicked()){
			return new GClickEvent(action, this, type);
		}
		return null;
	}
	
	public void draw(){
		drawBorder();
		int xPos = getX() + (getWidth() - text.length()*16)/2;
		int yPos = getY() + (getHeight() - 16)/2;
		GUtil.drawText(xPos, yPos, textColor, text);
	}
	
	public void update(long deltaTime){
		return;
	}
	
	public String getText(){ return text; }
}
