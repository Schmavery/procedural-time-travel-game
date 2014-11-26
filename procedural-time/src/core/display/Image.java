package core.display;

import gui.GUtil.SpriteSheetType;

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
	
	public Image(SpriteSheetType ssType) {
		super(ssType);
		hooks = new ArrayList<>();
	}
	
	@Override
	public long getAnimTime(){
		return 0;
	}
	
	@Override
	public int getPause(){
		// We divide by this...
		// So it can't be 0...
		return 1;
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
	
	public void clearPoly(){
		collisionPoly = new Poly();
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
	public void drawModel(float x, float y, int index) {
		// TODO Auto-generated method stub
		// Ignore index
		
		
	}
	
}
