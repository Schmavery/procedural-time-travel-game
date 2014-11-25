package core.display;

import gui.GUtil.SpriteSheetType;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

import core.util.Poly;

public abstract class Sprite {
	
	
	private SpriteSheetType spriteSheet;
	
	public abstract int getTexX();
	
	public abstract int getTexY();
	
	public abstract int getHeight();
	
	public abstract int getWidth();
	
	public abstract void draw(float x, float y);
//		GUtil.drawSprite(spriteSheet, x, y, Game.SCALE * getWidth(), Game.SCALE * getHeight(),
//				getTexX(), getTexY(), getWidth(), getHeight(), ReadableColor.WHITE);

}
