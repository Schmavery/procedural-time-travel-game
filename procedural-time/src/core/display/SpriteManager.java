package core.display;

import gui.GUtil.SpriteSheetType;

import java.util.HashMap;

public class SpriteManager {
	
	private static SpriteManager sm;
	private HashMap<SpriteSheetType, SpriteSheet> spriteSheets;
	
	private  SpriteManager() {
		spriteSheets = new HashMap<>();
	}
	
	public static SpriteManager get(){
		if (sm == null){
			sm = new SpriteManager();
		}
		return sm;
	}
	
	public void loadSpriteSheet(SpriteSheet ss){
		spriteSheets.put(ss.getType(), ss);
		PTTGSTDataParser.load(ss, this);
	}
	
	public void addImage(Image img){
		spriteSheets.get(img.getSpriteSheetType()).addImage(img);
	}
	
	public void addAnim(Animation2 anim){
		spriteSheets.get(anim.getSpriteSheetType()).addAnim(anim);
	}
	
	public Image getImage(SpriteSheetType ssType, int id){
		return spriteSheets.get(ssType).getImage(id);
	}
	
}
