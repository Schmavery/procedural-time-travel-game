package core.display;


public abstract class Sprite {
	
	private SpriteSheet spriteSheet;
	
	public Sprite(SpriteSheet ss) {
		this.spriteSheet = ss;
	}
	
	public SpriteSheet getSpriteSheet(){
		return spriteSheet;
	}
	
	public abstract int getTexX();
	
	public abstract int getTexY();
	
	public abstract int getHeight();
	
	public abstract int getWidth();
	
	
	/**
	 * This should only ever be called by SpriteInstance,
	 * and never directly.  Use spriteInstance.draw(x, y);
	 * @param x offset
	 * @param y offset
	 */
	public abstract void drawModel(float x, float y);
//		GUtil.drawSprite(spriteSheet, x, y, Game.SCALE * getWidth(), Game.SCALE * getHeight(),
//				getTexX(), getTexY(), getWidth(), getHeight(), ReadableColor.WHITE);
	
	public SpriteInstance getInstance(){
		return new SpriteInstance(this);
	}
}
