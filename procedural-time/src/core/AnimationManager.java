package core;

import gui.GUtil;
import gui.GUtil.SpriteSheet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class AnimationManager {
	
	private LinkedList<Animation> animList;
	
	public AnimationManager() {
		animList = new LinkedList<Animation>();
	}
	
	/**
	 * Loads a list of animations from a file defined by path.
	 * The file is a list of strings formatted according to the 
	 * documentation for loadAnim().
	 * @param path Filepath of animation list.
	 */
	public void loadAnims(String path, SpriteSheet spr){
		try (FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr)
		){
			
			String s = null;
			while((s = br.readLine()) != null) {
				loadAnim(s, spr);
			}
			fr.close();
			System.out.println("Loaded animations from '" + path + "'.");
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads up an animation defined by a string in the following format:
	 * index | contents
	 * ================
	 *    0  | name     
	 *    1  | pause    
	 *   ... | frameIDs 
	 *   
	 * Where name is the name of the animations,
	 * pause is the time (ms) between frames
	 * and frameIDs is a list of sprite IDs
	 * 
	 * All values are separated by spaces.
	 * @param parseString The string to be parsed.
	 * @param sprites The spritesheet corresponding to the sprite IDs.
	 * @return Animation
	 */
	public Animation loadAnim(String parseString, SpriteSheet spr){
		String[] parts = parseString.split(" ");
		int numFrames = parts.length - 2;
		int pause = Integer.parseInt(parts[1]);
		Animation anim = new Animation(spr, numFrames, pause, parts[0], true);
		animList.add(anim);
		for (int i = 2; i < parts.length; i++){
			int texX = Integer.parseInt(parts[i]) % 16;
			int texY = Integer.parseInt(parts[i]) / 16;
			anim.addFrame(texX, texY);
		}
		
		return anim;
	}
	
	public static void addFrame(Animation anim, int x, int y){
		anim.addFrame(x, y);
	}
	
	/**
	 * Updates all Animations that are stored in the manager.
	 * If an animation has only one frame, it is skipped.
	 * @param deltaTime Time since last cycle.
	 */
	public void update(long deltaTime){
		for (Animation anim : animList){
			if (anim.animated)
				anim.update(deltaTime);
		}
	}
	
	/**
	 *  Searches for an animation in the manager by name.
	 *  The name of each animation is defined in the animation file.
	 * @param name Name of the animation
	 * @return Animation corresponding to the name
	 */
	public Animation getAnim(String name){
		for (Animation anim: animList){
			if (anim.name.equals(name))
				return anim;
		}
		return null;
	}
	
//	private void destroy(Animation oldAnim){
//		animList.remove(oldAnim);
//	}
	
	
	/**
	 * Class describing a single animation.  These can be cloned to create
	 * standalone animations that can be manually updated.
	 * Animations should not be created manually but instead loaded by
	 * the AnimationManager from a file using loadAnims().
	 */
	public final class Animation {
		private boolean animated;		// That's right, some animations aren't animated. Sue me.
//		private boolean autoUpdated;	// true if the animation is held by the AnimationManager.
		private int[] animArrayX;		// X position of frame on spritesheet
		private int[] animArrayY;		// Y position of frame on spritesheet
		private int fillCount;			// Keeps track of index while populating animation
		private int dispPointer;		// Current displayed frame of animation
		private long pause;				// delay between frames
		private long timer;				// milliseconds into loop
		private String name;
		private SpriteSheet spriteSheet;
		
		private Animation(SpriteSheet spr, int len, long pause, String name, boolean autoUpdate){
			spriteSheet = spr;
			this.pause = pause;
			this.name = name;
			animArrayX = new int[len];
			animArrayY = new int[len];
			fillCount = 0;
			animated  = (len > 1);
			dispPointer = 0;
			timer = 0;
//			this.autoUpdated = autoUpdate;
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
		
		private void addFrame(int x, int y){
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
}
