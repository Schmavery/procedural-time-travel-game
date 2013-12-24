package gui;

import org.lwjgl.util.Rectangle;

public abstract class GComponent implements IElement{
	
	protected String name;
	protected String action;
	private boolean clickDown;
	private boolean visible;
	private IBorder border;
	private Rectangle boundingBox;
	
	protected GComponent(String name){
		this.name = name;
		visible = true;
	}
	
	protected GComponent(String name, String action){
		this.name = name;
		this.action = action;
		visible = true;
	}
	
	protected GComponent(String name, String action, Rectangle box){
		this.name = name;
		this.action = action;
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
	
	public void setBorder(IBorder border){
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
	
	public boolean overlaps(IElement e){
		return (boundingBox.contains(e.getRect()) || 
				boundingBox.intersects(e.getRect()));
	}
	
	protected void drawBorder(){
		if (border != null){
			border.drawBorder(this);
		}
	}

	public GClickEvent clickDown(int x, int y){
		if (boundingBox.contains(x, y)){
			clickDown = true;
		}
		return null;
	}
	
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
	
	public abstract void update(long deltaTime);

	public abstract void draw();
	
	public void translateRect(int x, int y){
		if (boundingBox != null)
			boundingBox.translate(x, y);
	}
	
	
	public void setRect(Rectangle rect){
		boundingBox = rect;
	}
	
	public Rectangle getRect() {return boundingBox;}
	public int getHeight() {return boundingBox.getHeight();}
	public int getWidth() {return boundingBox.getWidth();}
	public int getX() {return boundingBox.getX();}
	public int getY() {return boundingBox.getY();}

}
