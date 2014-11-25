package core.display;

import java.util.ArrayList;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

import core.util.Poly;

public class Image extends Sprite{

	private int id;
	private Point anchorPt;
	private ArrayList<SpriteHook> hooks;
	private Poly collisionPoly;
	private Rectangle bounds;
	
	public Image() {
		hooks = new ArrayList<>();
	}
	
	public int getTexX(){
		return bounds.getX();
	}
	
	public int getTexY(){
		return bounds.getY();
	}
	
	public int getHeight(){
		return bounds.getHeight();
	}
	
	public int getWidth(){
		return bounds.getWidth();
	}
	
	public void setBounds(Rectangle r){
		this.bounds = r;
	}
	
	public Poly getPoly(){
		return collisionPoly;
	}
	
	public Point getAnchor(){
		return anchorPt;
	}

	public void setAnchor(Point a){
		anchorPt = a;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void addHook(SpriteHook h){
		hooks.add(h);
	}
	
	@Override
	public void draw(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
}
