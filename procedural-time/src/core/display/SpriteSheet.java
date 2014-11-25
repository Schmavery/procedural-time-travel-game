package core.display;

import gui.GUtil.SpriteSheetType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SpriteSheet {
	private SpriteSheetType type;
	private String dataPath;
	ArrayList<Image> imgs;
	HashMap<String, Animation2> anims;
	
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
	}
	
	public String readData(){
		try (BufferedReader br = new BufferedReader(new FileReader(new File(dataPath))))
		{
			StringBuilder sb = new StringBuilder();
			String str = br.readLine();
			while (str != null){
				sb.append(str);
				str = br.readLine();
			}
			return sb.toString();
		} catch (FileNotFoundException e){
			System.out.println("No data file exists for this image.");
		} catch (IOException e){
			e.printStackTrace();
		}
		return "";
	}
	
	public SpriteSheetType getType(){
		return type;
	}
	
	public Image getImage(int id){
		return imgs.get(id);
	}
	
	public Animation2 getAnim(String key){
		return anims.get(key);
	}
	
	public void addImage(Image img){
		imgs.add(img);
	}
	
	public void addAnim(Animation2 anim){
		anims.put(anim.getName(), anim);
	}
}
