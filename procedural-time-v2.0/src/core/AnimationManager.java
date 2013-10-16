package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AnimationManager {
	
	public static enum SpriteSheet {
		MAP, ITEMS, PEOPLE;
	}
	
	private LinkedList<Animation> animList;
	
	public AnimationManager() {
		animList = new LinkedList<Animation>();
	}
	
	public void loadAnims(String path, SpriteSheet sprites){
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String s = null;
			while((s = br.readLine()) != null) {
				loadAnim(s, sprites);
			}
			fr.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("Loaded animations from '" + path + "'.");
	}
	
	public Animation newAnim(int len, long pause, String name, SpriteSheet sprites){
		Animation anim = new Animation(len, pause, name, sprites);
		animList.add(anim);
		return anim;
	}
	
	public Animation cloneAnim(Animation anim){
		Animation clone = anim.cloneAnim();
		animList.add(clone);
		return null;
	}
	
	public Animation loadAnim(String initString, SpriteSheet sprites){
		String[] parts = initString.split(" ");
		//  Parts Legend:  //
		//   0  - name     //
		//   1  - pause    //
		//  ... - frameIDs //
		//System.out.println(initString + ":" + parts.length);
		int numFrames = parts.length - 2;
		int pause = Integer.parseInt(parts[1]);
		Animation anim = new Animation(numFrames, pause, parts[0], sprites);
		animList.add(anim);
		for (int i = 2; i < parts.length; i++){
			int texX = Integer.parseInt(parts[i]) % 16;
			int texY = Integer.parseInt(parts[i]) / 16;
			anim.addFrame(texX, texY);
		}
		
		return anim;
	}
	
	public void addFrame(Animation anim, int x, int y){
		anim.addFrame(x, y);
	}
	
	public void update(long deltaTime){
		for (Animation anim : animList){
			if (anim.animated)
				anim.update(deltaTime);
		}
	}
	
	public Animation getAnim(String name){
		for (Animation anim: animList){
			if (anim.name.equals(name))
				return anim;
		}
		return null;
	}
	
	private void destroy(Animation oldAnim){
		animList.remove(oldAnim);
	}
	
	
	public class Animation {
		private Random rand = new Random();
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
		
		public Animation cloneAnim(){
			Animation anim = new Animation(this.animArrayX.length, 
							this.pause, this.name, this.spriteSheet);
			for (int i = 0; i < this.animArrayX.length; i++){
				anim.addFrame(this.animArrayX[i], this.animArrayY[i]);				
			}
			//anim.randomize();
			return anim;
		}
		
		private void randomize(){
			if (animated)
				timer = (timer + rand.nextInt()) % (pause * animArrayX.length);
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
		
		public void update(long deltaTime){
			timer = (timer + deltaTime) % (pause * animArrayX.length);
			dispPointer = (int) (timer / pause);
			//System.out.println(dispPointer);
		}
	}
}
