package gui;

import java.util.ArrayList;
import java.util.List;

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
				GClickEvent tmp = child.clickDown(x, y);
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
				GClickEvent tmp = child.clickUp(x, y);
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
				GClickEvent tmp = child.clickHold(x, y);
				if (tmp != null){
					return tmp;
				}
			}
		}
		return null;
	}

	public void draw() {
		if (!isVisible())
			return;
		
		drawBorder();
		for (IElement child : children){
			child.draw();
		}
	}

	public void update(long deltaTime){
		for (IElement child : children){
			child.update(deltaTime);
		}
	}

	@Override
	public boolean addChild(IElement e){
		Rectangle tmpRect;
		if (e.getRect() == null){
			tmpRect = null;
		} else {
			tmpRect = new Rectangle(e.getX() + getX(),
				e.getY() + getY(), 
				e.getWidth(), e.getHeight());
		}
		if (layout == null){
//			for (IElement child : children){
//				if (child.overlaps(e)){
//					System.out.println("Error: Overlapping children.");
//					return false;
//				}
//			}
			
			if (getRect().contains(tmpRect)){
				children.add(e);
				e.translateRect(getX(), getY());
			}else{
				System.out.println("Error: Child out of bounds.");
				return false;
			}
				
		} else {
			children.add(e);
			e.translateRect(getX(), getY());
			layout();
		}
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
	
	@Override
	public List<IElement> getChildren(){
		return children;
	}
	
	@Override
	public IElement getChild(int index){
		if (index < children.size()){
			return children.get(index);
		}
		return null;
	}
	
	@Override
	public int getChildCount(){
		return children.size();
	}
	
	@Override
	public void translateRect(int x, int y){
		super.translateRect(x, y);
		for (IElement e : children){
			e.translateRect(x, y);
		}
	}

	public void setLayout(ILayoutManager layout) {
		this.layout = layout;
	}
	
	public void layout() {
		layout.layoutContainer(this);
	}

}
