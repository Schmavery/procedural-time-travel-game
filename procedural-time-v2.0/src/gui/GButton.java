package gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import gui.ClickEvent.EventType;

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

public class GButton extends GElement{
	private String text;
	private EventType type = EventType.BUTTON;
	
	public GButton(String name, String text, String action, int posX, int posY, Color c){
		super(name, action, c, null);
		System.out.println("Test?");
		this.text = text;
		Rectangle rect = new Rectangle(posX, posY, text.length()*16 + 32, 48);
		setRect(rect);
	}
	
	public GButton(String name, String text, String action, Rectangle box, Color c){
		super(name, action, c, box);
		this.text = text;
	}
	
	public void setColor(Color c){
		this.color = c;
	}
	public void setText(String t){
		this.text = t;
	}
	
	/**
	 * On a click event, returns a corresponding ClickEvent
	 * @return ClickEvent
	 */
	public ClickEvent click(int x, int y){
		if (boundingBox.contains(x, y)){
			return new ClickEvent(action, name, type);
		}
		return null;
	}
	
	public void draw(){
		//glDisable(GL_TEXTURE_2D);
		glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		glPushMatrix();
			glTranslatef(boundingBox.getX(), boundingBox.getY(), 0);
//			glBegin(GL_QUADS);
//			glVertex2f(0, 0);
//			glVertex2f(boundingBox.getWidth(), 0);
//			glVertex2f(boundingBox.getWidth(), boundingBox.getHeight());
//			glVertex2f(0, boundingBox.getHeight());
//			glEnd();
			int innerW = boundingBox.getWidth() - 32;
			int innerH = boundingBox.getHeight() - 32;
			drawSprite(0,      0, 0, 4, 16, 16);		// Top Left
			drawSprite(16,     0, 1, 4, innerW, 16);	// Top Mid
			drawSprite(innerW + 16, 0, 2, 4, 16, 16);	// Top Right
			drawSprite(0,  16, 0, 5, 16, innerH);			// Mid Left
			drawSprite(16, 16, 1, 5, innerW, innerH);		// Mid Mid
			drawSprite(innerW + 16, 16, 2, 5, 16, innerH);
			drawSprite(0,           innerH + 16, 0, 6, 16, 16);		// Bottom Left
			drawSprite(16,          innerH + 16, 1, 6, innerW, 16);	// Bottom Mid
			drawSprite(innerW + 16, innerH + 16, 2, 6, 16, 16);		// Bottom Right
			
			glColor3f(0f, 0f, 0f);
			// Print the text
			glTranslatef(16, 16, 0);
			for (int i = 0; i < text.length(); i++){
				char drawCh = text.toUpperCase().charAt(i);
				int texX = (drawCh - ' ')%32;
				int texY = (drawCh - ' ')/32;
				drawSprite(i*16, 0, texX, texY, 16, 16);
			}
			
		glPopMatrix();


//		glColor3f(0f, 0f, 0f);
//		// Print the text
//		for (int i = 0; i < text.length(); i++){
//			char drawCh = text.toUpperCase().charAt(i);
//			int texX = (drawCh - ' ')%32;
//			int texY = (drawCh - ' ')/32;
//			drawSprite(i*16, 0, texX, texY, 16, 16);
//		}
		
	}
	
	public void update(long deltaTime){
		
	}
	
	//public String getAction(){ return action; }
	public String getText(){ return text; }
	public Color getColor(){ return color; }
	public Rectangle getRect(){ return boundingBox; }
	
}
