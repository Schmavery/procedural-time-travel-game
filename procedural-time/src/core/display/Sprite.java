package core.display;

import gui.GUtil.SpriteSheetType;


public abstract class Sprite {
	
	private SpriteSheetType ssType;
	private String name;
	
	public Sprite(SpriteSheetType ssType) {
		this.ssType = ssType;
	}
	
	public SpriteSheetType getSpriteSheetType(){
		return ssType;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
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
	public abstract void drawModel(float x, float y, float w, float h, int index);
	
	public SpriteInstance getInstance(){
		return new SpriteInstance(this);
	}
	
	public SpriteInstance getInstance(boolean repeat){
		return new SpriteInstance(this, repeat);
	}
}
