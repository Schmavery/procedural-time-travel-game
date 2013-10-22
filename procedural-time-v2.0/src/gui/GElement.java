package gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableRectangle;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.WritableDimension;
import org.lwjgl.util.WritablePoint;
import org.lwjgl.util.WritableRectangle;

public abstract class GElement implements ReadableRectangle{
	
	protected String name;
	protected String action;
	protected Color color;
	protected Rectangle boundingBox;
	protected boolean visible;
	
	protected GElement(String name, String action, Color c, Rectangle box){
		this.name = name;
		this.action = action;
		color = c;
		boundingBox = box;
		visible = true;
	}
	
	public void hide(){
		System.out.println("HIDING " + name);
		visible = false;
	}
	public void show(){
		System.out.println("SHOWING " + name);
		visible = true;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean overlaps(GElement e){
		return (boundingBox.contains(e.boundingBox) || 
				boundingBox.intersects(e.boundingBox));
	}
	
	protected static void drawSprite(float x, float y, int texX, int texY, float spriteW, float spriteH){
		glPushMatrix();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
			glTexCoord2f(texX/32f, texY/32f);				//short,short
			glVertex2f(0, 0);
			glTexCoord2f((texX+1)/32f, texY/32f);			//long, short
			glVertex2f(spriteW, 0);
			glTexCoord2f((texX+1)/32f, (texY+1)/32f);		//long,  long
			glVertex2f(spriteW, spriteH);
			glTexCoord2f(texX/32f, (texY+1)/32f);			//short, long
			glVertex2f(0, spriteH);
			glEnd();
		glPopMatrix();
	}
	
	protected static void drawText(String text){
		for (int i = 0; i < text.length(); i++){
			char drawCh = text.toUpperCase().charAt(i);
			int texX = (drawCh - ' ')%32;
			int texY = (drawCh - ' ')/32;
			drawSprite(i*16, 0, texX, texY, 16, 16);
		}
	}
	
	public abstract ClickEvent clickDown(int x, int y);
	public abstract ClickEvent clickUp(int x, int y);
	public abstract ClickEvent clickHold(int x, int y);
	public abstract void update(long deltaTime);
	public abstract void draw();
	
	
	public void setRect(Rectangle rect){
		this.boundingBox = rect;
	}

	// Implement ReadableRectangle
	public void getBounds(WritableRectangle arg0) {boundingBox.getBounds(arg0);}
	public void getSize(WritableDimension arg0) {boundingBox.getSize(arg0);}
	public void getLocation(WritablePoint arg0) {boundingBox.getLocation(arg0);}
	public int getHeight() {return boundingBox.getHeight();}
	public int getWidth() {return boundingBox.getWidth();}
	public int getX() {return boundingBox.getX();}
	public int getY() {return boundingBox.getY();}

}
