package core.display;

import gui.GUtil.SpriteSheet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AnimationManager {
	
	private static List<Animation> animList = new LinkedList<>();
	

	/**
	 * Loads a list of animations from a file defined by path.
	 * The file is a list of strings formatted according to the 
	 * documentation for loadAnim().
	 * @param path Filepath of animation list.
	 */
	public static void loadAnims(String path, SpriteSheet spr){
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
	public static Animation loadAnim(String parseString, SpriteSheet spr){
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
	public static void update(long deltaTime){
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
	public static Animation getAnim(String name){
		for (Animation anim: animList){
			if (anim.name.equals(name))
				return anim;
		}
		return null;
	}
	
}
