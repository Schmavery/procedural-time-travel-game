package core.display;

import gui.GUtil;
import gui.GUtil.SpriteSheet;

import org.lwjgl.util.Point;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;

import core.Game;

public class Sprite {
	private Point anchorPt;
	private SpriteHook[] hooks;
	private Point[] collisionPoly;
	private Rectangle bounds;
	
	private SpriteSheet spriteSheet;
	
	private int getTexX(){
		return bounds.getX();
	}
	
	private int getTexY(){
		return bounds.getY();
	}
	
	private int getHeight(){
		return bounds.getHeight();
	}
	
	private int getWidth(){
		return bounds.getWidth();
	}
	
	public void draw(float x, float y){
		GUtil.drawSprite(spriteSheet, x, y, Game.SCALE * getWidth(), Game.SCALE * getHeight(),
				getTexX(), getTexY(), getWidth(), getHeight(), ReadableColor.WHITE);
	}
}
