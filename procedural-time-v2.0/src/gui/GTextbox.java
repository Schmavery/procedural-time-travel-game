package gui;

import gui.GUtil.Alignment;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

public class GTextbox extends GComponent{

	private String text;
	private int textLen;
	private Color textColor;
	private Alignment alignment = Alignment.LEFT;
	
	public GTextbox(String name, String text){
		super(name);
		this.text = text;
		this.textLen = text.length();
		this.textColor = new Color(0, 0, 0);
	}
	
	public GTextbox(String name, String text, int posX, int posY) {
		super(name, null, null);
		Rectangle rect = new Rectangle(posX, posY, 16*text.length(), 16);
		setRect(rect);
		this.text = text;
		this.textLen = text.length();
		this.textColor = new Color(0, 0, 0);
	}

	public void setText(String text){
		if (text.length() > textLen){
			this.text = text.substring(0, textLen);
			return;
		}
		this.text = text;
	}
	
	public void setTextColor(Color c){
		this.textColor = c;
	}
	
	public void setAlignment(Alignment alignment){
		this.alignment = alignment;
	}
	
	public GClickEvent clickDown(int x, int y) {
		return null;
	}

	public GClickEvent clickUp(int x, int y) {
		return null;
	}

	public GClickEvent clickHold(int x, int y) {
		return null;
	}

	public void update(long deltaTime) {
		return;
	}

	public void draw() {
		int xPos, yPos;
		switch (alignment){
			case LEFT:
				xPos = getX() + 16;
				yPos = getY() + (getHeight() - 16)/2;
				break;
			case RIGHT:
				xPos = getX() + (getWidth() - (text.length() + 1)*16);
				yPos = getY() + (getHeight() - 16)/2;
				break;
			case TOP:
				xPos = getX() + (getWidth() - text.length()*16)/2;
				yPos = getY() + 16;
				break;
			case BOTTOM:
				xPos = getX() + (getWidth() - text.length()*16)/2;
				yPos = getY() + (getHeight() - 2*16);
				break;
			case CENTER:
				xPos = getX() + (getWidth() - text.length()*16)/2;
				yPos = getY() + (getHeight() - 16)/2;
				break;
			default:
				xPos = getX();
				yPos = getY();
				break;
	}
		GUtil.drawText(xPos, yPos, textColor, text);
	}

}
