package gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import gui.GClickEvent.EventType;

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
 */

public class GButton extends GComponent{
	private String text;
	private Color textColor;
	private boolean clickDown;
	private EventType type = EventType.BUTTON;
	
	public GButton(String name, String text, String action, int posX, int posY, Color c){
		super(name, action, c, null);
		Rectangle rect = new Rectangle(posX, posY, text.length()*16 + 32, 48);
		setRect(rect);
		this.text = text;
		this.clickDown = false;
		this.textColor = new Color(0, 0, 0);
	}
	
	public GButton(String name, String text, String action, Rectangle box, Color c){
		super(name, action, c, box);
		this.text = text;
	}
	
	public void setTextColor(Color c){
		this.textColor = c;
	}
	
	public void setText(String t){
		this.text = t;
	}
	
//	/**
//	 * On a click event, returns a corresponding GClickEvent
//	 * @return GClickEvent
//	 */
//	public GClickEvent clickDown(int x, int y){
//		if (boundingBox.contains(x, y)){
//			clickDown = true;
//		}
//		return null;
//	}
//	
//	/**
//	 * On a click event, returns a corresponding GClickEvent
//	 * @return GClickEvent
//	 */
//	public GClickEvent clickUp(int x, int y){
//		if (boundingBox.contains(x, y) && clickDown){
//			clickDown = false;
//			return new GClickEvent(action, this, type);
//		}
//		return null;
//	}
//	
//	public GClickEvent clickHold(int x, int y){
//		if (!boundingBox.contains(x, y)){
//			clickDown = false;
//		}
//		return null;
//	}
	public GClickEvent clickUp(int x, int y){
		super.clickUp(x, y);
		if (boundingBox.contains(x, y) && isClicked()){
			return new GClickEvent(action, this, type);
		}
		return null;
	}
	
	public void draw(){
		//glDisable(GL_TEXTURE_2D);
//		if (clickDown){
//			glColor3f((color.getRed()+100)/255f, (color.getGreen()+100)/255f, (color.getBlue()+100)/255f);
//		} else {
//			glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
//		}
		drawBorder();
		glPushMatrix();
			glTranslatef(getX(), getY(), 0);
//			int innerW = getWidth() - 32;
//			int innerH = getHeight() - 32;
//			drawSprite(0,      0, 0, 4, 16, 16);		// Top Left
//			drawSprite(16,     0, 1, 4, innerW, 16);	// Top Mid
//			drawSprite(innerW + 16, 0, 2, 4, 16, 16);	// Top Right
//			drawSprite(0,  16, 0, 5, 16, innerH);			// Mid Left
//			drawSprite(16, 16, 1, 5, innerW, innerH);		// Mid Mid
//			drawSprite(innerW + 16, 16, 2, 5, 16, innerH);
//			drawSprite(0,           innerH + 16, 0, 6, 16, 16);		// Bottom Left
//			drawSprite(16,          innerH + 16, 1, 6, innerW, 16);	// Bottom Mid
//			drawSprite(innerW + 16, innerH + 16, 2, 6, 16, 16);		// Bottom Right
			
			glColor3f(textColor.getRed()/255f, textColor.getGreen()/255f, textColor.getBlue()/255f);
			// Print the text
			glTranslatef(16, 16, 0);
			GUtil.drawText(text);
			
		glPopMatrix();


	}
	
	public void update(long deltaTime){
		return;
	}
	
	public String getText(){ return text; }
	public Rectangle getRect(){ return boundingBox; }
	
}
