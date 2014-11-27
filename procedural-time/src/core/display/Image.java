package core.display;

import gui.GUtil;
import gui.GUtil.SpriteSheetType;

import java.util.ArrayList;

import org.lwjgl.util.Point;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;

import core.Game;
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
		// We divide by this...
		// So it can't be 0...
		return 1;
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
		GUtil.drawSprite(getSpriteSheetType(), x-(anchorPt.getX()*Game.SCALE), y-(anchorPt.getY()*Game.SCALE), 
				Game.SCALE * getWidth(), Game.SCALE * getHeight(),
				getTexX(), getTexY(), getWidth(), getHeight(), ReadableColor.WHITE);
	}
	
}
