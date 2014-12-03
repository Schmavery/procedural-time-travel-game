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
	
	/**
	 * For debugging purposes
	 */
	public void printLoadedSpriteSheets(){
		String prt = "";
		for (SpriteSheetType sst : spriteSheets.keySet()){
			prt += sst.name() + ", ";
		}
		System.out.println(prt);
		System.out.println(spriteSheets.get(SpriteSheetType.GUI));
	}
	
	public void addImage(Image img){
		spriteSheets.get(img.getSpriteSheetType()).addImage(img);
	}
	
	public void addAnim(Animation anim){
		spriteSheets.get(anim.getSpriteSheetType()).addAnim(anim);
	}
	
	public Image getImage(SpriteSheetType ssType, int id){
		return spriteSheets.get(ssType).getImage(id);
	}
	
	public Animation getAnim(SpriteSheetType ssType, String key){
		return spriteSheets.get(ssType).getAnim(key);
	}
	
	public Sprite getSprite(SpriteSheetType ssType, String key){
		return spriteSheets.get(ssType).get(key);
	}
}
