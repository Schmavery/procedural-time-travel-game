package core;

import java.util.LinkedList;

public class AnimationManager {
	
	public static enum SpriteSheet {
		MAP, ITEMS, PEOPLE;
	}
	
	private LinkedList<Animation> animList;
	
	public Animation newAnim(int len, long pause, String name, SpriteSheet sprites){
		Animation anim = new Animation(len, pause, name, sprites);
		animList.add(anim);
		return anim;
	}
	
	public void addFrame(Animation anim, int x, int y){
		anim.addFrame(x, y);
	}
	
	public void update(long deltaTime){
		for (Animation anim : animList){
			anim.update(deltaTime);
		}
	}
	
	private void destroy(Animation oldAnim){
		animList.remove(oldAnim);
	}
	
	
	public class Animation {
		private boolean animated;		// That's right, some animations aren't animated. Sue me.
		private int[] animArrayX;		// X position of frame on spritesheet
		private int[] animArrayY;		// Y position of frame on spritesheet
		private int fillCount;			// Keeps track of index while populating animation
		private int dispPointer;		// Current displayed frame of animation
		private long pause;				// delay between frames
		private long timer;				// milliseconds into loop
		private String name;
		private SpriteSheet spriteSheet;
		
		public Animation(int len, long pause, String name, SpriteSheet spriteSheet){
			this.pause = pause;
			this.spriteSheet = spriteSheet;
			this.name = name;
			animArrayX = new int[len];
			animArrayY = new int[len];
			fillCount = 0;
			animated  = (len > 1);
			dispPointer = 0;
			timer = 0;
			}
		
		public int getDispX(){
			return animArrayX[dispPointer];
		}
		
		public int getDispY(){
			return animArrayY[dispPointer];
		}
		
		public SpriteSheet getSpriteSheet(){
			return spriteSheet;
		}
		
		public void destroy(){
			AnimationManager.this.destroy(this);
		}
		
		private void addFrame(int x, int y){
			animArrayX[fillCount] = x;
			animArrayY[fillCount] = y;
			fillCount++;
		}
		
		private void update(long deltaTime){
			timer = (timer + deltaTime) % (pause * animArrayX.length);
			dispPointer = (int) (timer / pause);
		}
	}
}
