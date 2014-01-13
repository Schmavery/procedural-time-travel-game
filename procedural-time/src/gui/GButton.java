package gui;

import gui.GClickEvent.EventType;
import gui.GUtil.Alignment;

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
	private Alignment alignment = Alignment.CENTER;
	
	public GButton(String name, String text, String action, Color c){
		super(name, action);
		this.text = text;
		this.textColor = new Color(0, 0, 0);
		setBorder(GBorderFactory.createButtonBorder(c));
	}
	
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
	
	public void setAlignment(Alignment alignment){
		this.alignment = alignment;
	}
	
	@Override
	public GClickEvent clickUp(int x, int y){
		GClickEvent ce = null;
		if (getRect().contains(x, y) && isClicked()){
			ce = new GClickEvent(action, this, type);
		}
		super.clickUp(x, y);
		return ce;
	}
	
	@Override
	public void draw(){
		if (!isVisible())
			return;
		
		drawBorder();
		int xPos, yPos;
		switch (alignment){
			case LEFT:
				xPos = getX() + 16;
				yPos = getY() + (getHeight() - 16)/2;
				break;
			case RIGHT:
				xPos = getX() + (getWidth() - GUtil.textLength(text) - 16);
				yPos = getY() + (getHeight() - 16)/2;
				break;
			case TOP:
				xPos = getX() + (getWidth() - GUtil.textLength(text))/2;
				yPos = getY() + 16;
				break;
			case BOTTOM:
				xPos = getX() + (getWidth() - GUtil.textLength(text))/2;
				yPos = getY() + (getHeight() - 2*16);
				break;
			case CENTER:
				xPos = getX() + (getWidth() - GUtil.textLength(text))/2;
				yPos = getY() + (getHeight() - 16)/2;
				break;
			default:
				xPos = getX();
				yPos = getY();
				break;
		}
		GUtil.drawText(xPos, yPos, textColor, text);
	}
	
	public void update(long deltaTime){
		return;
	}
	
	public String getText(){ return text; }
}
