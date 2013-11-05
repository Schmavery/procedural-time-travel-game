package gui;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

public class GPanel extends GComponent implements IContainer{
	private ArrayList<IElement> children;
	private ILayoutManager layout;

	public GPanel(String name, Color c){
		super(name);
		children = new ArrayList<IElement>();
		setBorder(GBorderFactory.createBasicBorder(c));
	}
	
	public GPanel(String name){
		super(name);
		children = new ArrayList<IElement>();
	}
	
	public GPanel(String name, String action, Rectangle box){
		super(name, action, box);
		children = new ArrayList<IElement>();
	}
	
	public GClickEvent clickDown(int x, int y){
		if (getRect().contains(x, y)){
			for (IElement child : children){
				GClickEvent tmp = child.clickDown(x - getRect().getX(), y - getRect().getY());
				if (tmp != null){
					return tmp;
				}
			}
		}
		return null;
	}
	
	public GClickEvent clickUp(int x, int y){
		if (getRect().contains(x, y)){
			for (IElement child : children){
				GClickEvent tmp = child.clickUp(x - getRect().getX(), y - getRect().getY());
				if (tmp != null){
					return tmp;
				}
			}
		}
		return null;
	}

	public GClickEvent clickHold(int x, int y){
		if (getRect().contains(x, y)){
			for (IElement child : children){
				GClickEvent tmp = child.clickHold(x - getRect().getX(), y - getRect().getY());
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
		glPushMatrix();
			glTranslatef(getX(), getY(), 0);
			// Draw my children
			for (IElement child : children){
				child.draw();
			}
		glPopMatrix();
	}

	public void update(long deltaTime){
		for (IElement child : children){
			child.update(deltaTime);
		}
	}

	@Override
	public boolean addChild(IElement e){
		if (layout == null){
			for (IElement child : children){
				if (child.overlaps(e)){
					System.out.println("Error: Overlapping children.");
					return false;
				}
			}
			Rectangle tmpRect = new Rectangle(e.getX() + getX(),
					e.getY() + getY(), 
					e.getWidth(), e.getHeight());
			
			if (getRect().contains(tmpRect)){
				children.add(e);
				return true;
			}else{
				System.out.println("Error: Child out of bounds.");
				return false;
			}
				
		}
		children.add(e);
		layout();
		return true;
	}
	
	@Override
	public IElement getChild(String name){
		for (IElement child : children){
			if (child.getName().equals(name)){
				return child;
			}
		}
		return null;
	}
	
	public IElement getChild(int index){
		if (index < children.size()){
			return children.get(index);
		}
		return null;
	}
	
	public int getChildCount(){
		return children.size();
	}

	public void setLayout(ILayoutManager layout) {
		this.layout = layout;
	}
	
	public void layout() {
		layout.layoutContainer(this);
	}

}
