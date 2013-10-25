package gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

public class GPanel extends GComponent{
	ArrayList<GComponent> children;
	int slideX, slideY, slideSpeedX, slideSpeedY;

	public GPanel(String name, String action, Rectangle box, Color c){
		super(name, action, c, box);
		children = new ArrayList<>();
	}
	
	public ClickEvent clickDown(int x, int y){
		if (boundingBox.contains(x, y)){
			for (GComponent child : children){
				ClickEvent tmp = child.clickDown(x - boundingBox.getX(), y - boundingBox.getY());
				if (tmp != null){
					return tmp;
				}
			}
		}
		return null;
	}
	
	public ClickEvent clickUp(int x, int y){
		if (boundingBox.contains(x, y)){
			for (GComponent child : children){
				ClickEvent tmp = child.clickUp(x - boundingBox.getX(), y - boundingBox.getY());
				if (tmp != null){
					return tmp;
				}
			}
		}
		return null;
	}

	public ClickEvent clickHold(int x, int y){
		if (boundingBox.contains(x, y)){
			for (GComponent child : children){
				ClickEvent tmp = child.clickHold(x - boundingBox.getX(), y - boundingBox.getY());
				if (tmp != null){
					return tmp;
				}
			}
		}
		return null;
	}

	public void draw() {
		// Draw myself
		drawBorder();
		//glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		glPushMatrix();
			glTranslatef(getX(), getY(), 0);
			
//			if (visible){
//				int innerW = getWidth() - 32;
//				int innerH = getHeight() - 32;
//				drawSprite(0,      0, 0, 4, 16, 16);		// Top Left
//				drawSprite(16,     0, 1, 4, innerW, 16);	// Top Mid
//				drawSprite(innerW + 16, 0, 2, 4, 16, 16);	// Top Right
//				drawSprite(0,  16, 0, 5, 16, innerH);			// Mid Left
//				drawSprite(16, 16, 1, 5, innerW, innerH);		// Mid Mid
//				drawSprite(innerW + 16, 16, 2, 5, 16, innerH);
//				drawSprite(0,           innerH + 16, 0, 6, 16, 16);		// Bottom Left
//				drawSprite(16,          innerH + 16, 1, 6, innerW, 16);	// Bottom Mid
//				drawSprite(innerW + 16, innerH + 16, 2, 6, 16, 16);		// Bottom Right
//			}
		
			// Draw my children
			for (GComponent child : children){
				child.draw();
			}
		glPopMatrix();
	}

	public void update(long deltaTime){
		for (GComponent child : children){
			child.update(deltaTime);
		}
	}

	public boolean addChild(GComponent e){
		for (GComponent child : children){
			if (child.overlaps(e)){
				System.out.println("Error: Overlapping children.");
				return false;
			}
		}
		Rectangle tmpRect = new Rectangle(e.getX() + getX(),
				e.getY() + getY(), 
				e.getWidth(), e.getHeight());
		
		if (boundingBox.contains(tmpRect)){
			children.add(e);
			return true;
		}else{
			System.out.println("Error: Child out of bounds.");
			return false;
		}
	}
	
	public GComponent getChild(String name){
		for (GComponent child : children){
			if (child.getName().equals(name)){
				return child;
			}
		}
		return null;
	}
	
}
