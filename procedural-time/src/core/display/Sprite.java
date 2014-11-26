package core.display;

import gui.GUtil.SpriteSheetType;


public abstract class Sprite {
	
	private SpriteSheetType ssType;
	
	public Sprite(SpriteSheetType ssType) {
		this.ssType = ssType;
	}
	
	public SpriteSheetType getSpriteSheetType(){
		return ssType;
	}
	
	public abstract int getTexX();
	
	public abstract int getTexY();
	
	public abstract int getHeight();
	
	public abstract int getWidth();
	
	public abstract long getAnimTime();
	public abstract int getPause();
	/**
	 * This should only ever be called by SpriteInstance,
	 * and never directly.  Use spriteInstance.draw(x, y);
	 * @param x - offset
	 * @param y - offset
	 * @param index - Animation frame index
	 */
	public abstract void drawModel(float x, float y, int index);
//		GUtil.drawSprite(spriteSheet, x, y, Game.SCALE * getWidth(), Game.SCALE * getHeight(),
//				getTexX(), getTexY(), getWidth(), getHeight(), ReadableColor.WHITE);
	
	public SpriteInstance getInstance(){
		return new SpriteInstance(this);
	}
}
