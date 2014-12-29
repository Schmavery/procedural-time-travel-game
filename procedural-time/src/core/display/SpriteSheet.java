package core.display;

import gui.GUtil.SpriteSheetType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class SpriteSheet {
	private SpriteSheetType type;
	private String dataPath;
	private String dataStr;
	private Texture tex;
	ArrayList<Image> imgs;
	HashMap<String, Animation> anims;
	HashMap<String, Sprite> sprites;
	
	/**
	 * All the information that is needed before calling
	 * SpriteManager.loadSpriteSheet(SpriteSheet);
	 * @param type Type describing what is in the SS (people, map...)
	 * @param dataPath Location of SS data file on disk (pttg-sprite-tool format)
	 */
	public SpriteSheet(SpriteSheetType type, String dataPath){
		this.type = type;
		this.dataPath = dataPath;
		imgs = new ArrayList<>();
		anims = new HashMap<>();
		sprites = new HashMap<>();
	}
	
	public void loadTexture(){
		String path;
		String firstLine = readData().split("\n")[0];
		if (firstLine.startsWith("##")){
			path = firstLine.substring(2);
			try {
				tex = TextureLoader.getTexture("PNG", new FileInputStream(new File(path)), GL11.GL_NEAREST);
			} catch (IOException e) {
				System.out.println("Texture load failed: "+e.getMessage());
			}
		} else {
			System.out.println("Texture load failed.");
		}
	}
	
	public String readData(){
		if (dataStr != null && dataStr.length() > 0){
			return dataStr;
		} else {
			try (BufferedReader br = new BufferedReader(new FileReader(new File(dataPath))))
			{
				StringBuilder sb = new StringBuilder();
				String str = br.readLine();
				while (str != null){
					sb.append(str);
					sb.append("\n");
					str = br.readLine();
				}
				dataStr = sb.toString();
			} catch (FileNotFoundException e){
				System.out.println("No data file exists for this image.");
				dataStr = "";
			} catch (IOException e){
				e.printStackTrace();
				dataStr = "";
			}
			return dataStr;
		}
	}
	
	public SpriteSheetType getType(){
		return type;
	}
	
	public Image getImage(int id){
		return imgs.get(id);
	}
	
	public Texture getTex(){
		return tex;
	}
	
	public Animation getAnim(String key){
		return anims.get(key);
	}
	
	public void addImage(Image img){
		while(imgs.size() <= img.getId()){
			imgs.add(null);
		}
		imgs.set(img.getId(), img);
		sprites.put(img.getName(), img);
	}
	
	public void addAnim(Animation anim){
		anims.put(anim.getName(), anim);
		sprites.put(anim.getName(), anim);
	}
	
	public Sprite get(String key){
		if (!sprites.containsKey(key)){
			System.out.println("Could not find sprite: "+key);
		}
		return sprites.get(key);
	}
}
