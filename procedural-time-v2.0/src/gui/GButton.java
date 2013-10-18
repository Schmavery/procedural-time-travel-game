package gui;

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

public class GButton extends GElement{
	private String text;
	
	public GButton(String name, String text, Rectangle box, String action, Color c){
		super(name, action, c, box);
		this.text = text;
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
//		glDisable(GL_TEXTURE_2D);
//		glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
//		glPushMatrix();
//			glTranslatef(boundingBox.getX(), boundingBox.getY(), 0);
//			glBegin(GL_QUADS);
//			glVertex2f(0, 0);
//			glVertex2f(boundingBox.getWidth(), 0);
//			glVertex2f(boundingBox.getWidth(), boundingBox.getHeight());
//			glVertex2f(0, boundingBox.getHeight());
//			glEnd();
//		glPopMatrix();
//		
//		glEnable(GL_TEXTURE_2D);
//		glColor3f(0f, 0f, 0f);
		// Print the text
	}
	
	//public String getAction(){ return action; }
	public String getText(){ return text; }
	public Color getColor(){ return color; }
	public Rectangle getRect(){ return boundingBox; }
	
}
