package gui;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableRectangle;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.WritableDimension;
import org.lwjgl.util.WritablePoint;
import org.lwjgl.util.WritableRectangle;

public abstract class GComponent implements ReadableRectangle{
	
	protected String name;
	protected String action;
	protected boolean clickDown;
	//protected Color color;
	protected Rectangle boundingBox;
	protected boolean visible;
	protected GBorder border;
	
	protected GComponent(String name, String action, Color c, Rectangle box){
		this.name = name;
		this.action = action;
		//color = c;
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
	
	public void setBorder(GBorder border){
		this.border = border;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public boolean isClicked(){
		return clickDown;
	}
	
	public boolean overlaps(GComponent e){
		return (boundingBox.contains(e.boundingBox) || 
				boundingBox.intersects(e.boundingBox));
	}
	
	protected void drawBorder(){
		if (border != null){
			border.drawBorder(this);
		}
	}

	/**
	 * On a click event, returns a corresponding GClickEvent
	 * @return GClickEvent
	 */
	public GClickEvent clickDown(int x, int y){
		if (boundingBox.contains(x, y)){
			clickDown = true;
		}
		return null;
	}
	
	/**
	 * On a click event, returns a corresponding GClickEvent
	 * @return GClickEvent
	 */
	public GClickEvent clickUp(int x, int y){
		if (boundingBox.contains(x, y) && clickDown){
			clickDown = false;
		}
		return null;
	}
	
	public GClickEvent clickHold(int x, int y){
		if (!boundingBox.contains(x, y)){
			clickDown = false;
		}
		return null;
	}
	
//	public abstract GClickEvent clickDown(int x, int y);
//	public abstract GClickEvent clickUp(int x, int y);
//	public abstract GClickEvent clickHold(int x, int y);
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
