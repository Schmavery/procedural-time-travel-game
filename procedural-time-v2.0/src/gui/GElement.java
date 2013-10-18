package gui;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

public abstract class GElement {
	
	protected String name;
	protected LinkedList<String> actions;
	protected Color color;
	protected Rectangle boundingBox;
	protected boolean visible;
	
	protected GElement(String name, String action, Color c, Rectangle box){
		this.name = name;
		if (action != null){
			this.actions = new LinkedList<String>();
			this.actions.add(action);
		}
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
	
	public abstract List<String> click(int x, int y);
	public abstract void draw();
}
