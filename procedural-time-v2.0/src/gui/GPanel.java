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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

public class GPanel extends GElement{
	ArrayList<GElement> children;

	public GPanel(String name, String action, Rectangle box, Color c){
		super(name, action, c, box);
		children = new ArrayList<>();
	}
	
	public List<String> click(int x, int y){
		LinkedList<String> actionStrings = new LinkedList<String>(actions);
		for (GElement child : children){
			actionStrings.addAll(child.click(x, y));
		}
		return actionStrings;
	}

	public void addChild(GElement e){
		children.add(e);
	}
	
	public GElement getChild(String name){
		for (GElement child : children){
			if (child.getName().equals(name)){
				return child;
			}
		}
		return null;
	}
	
	public void draw() {
		// Draw myself
		
		glDisable(GL_TEXTURE_2D);
		
		glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		glPushMatrix();
			glTranslatef(boundingBox.getX(), boundingBox.getY(), 0);
			glBegin(GL_QUADS);
			glVertex2f(0, 0);
			glVertex2f(boundingBox.getWidth(), 0);
			glVertex2f(boundingBox.getWidth(), boundingBox.getHeight());
			glVertex2f(0, boundingBox.getHeight());
			glEnd();
		
		// Draw my children
			for (GElement child : children){
				child.draw();
			}
		glPopMatrix();
	}

}
