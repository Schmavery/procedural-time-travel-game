package gui;

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
	
	public abstract ClickEvent click(int x, int y);
	public abstract void update(long deltaTime);
	public abstract void draw();

	// Implement ReadableRectangle
	public void getBounds(WritableRectangle arg0) {boundingBox.getBounds(arg0);}
	public void getSize(WritableDimension arg0) {boundingBox.getSize(arg0);}
	public void getLocation(WritablePoint arg0) {boundingBox.getLocation(arg0);}
	public int getHeight() {return boundingBox.getHeight();}
	public int getWidth() {return boundingBox.getWidth();}
	public int getX() {return boundingBox.getX();}
	public int getY() {return boundingBox.getY();}

}
