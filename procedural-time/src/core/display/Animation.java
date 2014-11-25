package core.display;

import core.Game;
import gui.GUtil;
import gui.GUtil.SpriteSheetType;
//import core.AnimationManager.Animation;

/**
 * Class describing a single animation.  These can be cloned to create
 * standalone animations that can be manually updated.
 * Animations should not be created manually but instead loaded by
 * the AnimationManager from a file using loadAnims().
 */
public final class Animation {
	protected boolean animated;		// That's right, some animations aren't animated. Sue me.
//	private boolean autoUpdated;	// true if the animation is held by the AnimationManager.
	private int[] animArrayX;		// X position of frame on spritesheet
	private int[] animArrayY;		// Y position of frame on spritesheet
	private int fillCount;			// Keeps track of index while populating animation
	private int dispPointer;		// Current displayed frame of animation
	private long pause;				// delay between frames
	private long timer;				// milliseconds into loop
	protected String name;
	private SpriteSheetType spriteSheet;
	
	protected Animation(SpriteSheetType spr, int len, long pause, String name, boolean autoUpdate){
		spriteSheet = spr;
		this.pause = pause;
		this.name = name;
		animArrayX = new int[len];
		animArrayY = new int[len];
		fillCount = 0;
//		animated  = (len > 1);
		dispPointer = 0;
		timer = 0;
//		this.autoUpdated = autoUpdate;
		}
	
	/**
	 * Used to create clones of animations loaded by AnimationManager.
	 * This should be used only if you want to manually control the update
	 * of the animation.  Otherwise use a reference to the animation held
	 * and updated by AnimationManager.
	 * @return new Animation that is a clone of the calling Animation
	 */
	public Animation cloneAnim(){
		Animation anim = new Animation(this.spriteSheet, this.animArrayX.length, 
						this.pause, this.name, false);
		for (int i = 0; i < this.animArrayX.length; i++){
			anim.addFrame(this.animArrayX[i], this.animArrayY[i]);				
		}
		
		return anim;
	}
	
	public int getTexX(){
		return animArrayX[dispPointer];
	}
	
	public int getTexY(){
		return animArrayY[dispPointer];
	}
	
	protected void addFrame(int x, int y){
		animArrayX[fillCount] = x;
		animArrayY[fillCount] = y;
		fillCount++;
	}
	
	public boolean update(long deltaTime){
		long tmptime = timer;
		timer = (timer + deltaTime) % getAnimLength();
		dispPointer = (int) (timer / pause);
		return (timer > tmptime && deltaTime > 0);
	}
	
	public long getAnimLength(){
		return pause * animArrayX.length;
	}
	
	public String toString(){
		return name;
	}
	
	public void draw(float x, float y, float w, float h){
		GUtil.drawSprite(spriteSheet, x, y, getTexX(), getTexY(), w, h, 16);
	}
	
	public void draw(float x, float y){
		GUtil.drawSprite(spriteSheet, x, y, getTexX(),
				getTexY(), Game.SCALE * Game.TILE_SIZE, Game.SCALE
						* Game.TILE_SIZE, 16);
	}
}