package entities.concrete;

import java.util.HashMap;

import org.lwjgl.util.Point;

import core.Game;
import core.Tile;
import core.display.Sprite;
import core.display.SpriteInstance;
import core.display.SpriteManager;
import entities.abstr.AbstractPlacedItem;
import entities.interfaces.Entity;
import entities.interfaces.Holdable;
import entities.interfaces.Placeable;
import gui.GUtil.SpriteSheetType;

public class House extends AbstractPlacedItem implements Placeable, Holdable {

	private HashMap<String, Sprite> sprites;
	private String[][] bitmaskKeys;
	
	public House(float x, float y){
		super(x, y);
		sprites = new HashMap<>();
		String[] spriteNames = {"wall_s_1", "wall_s_2", "wall_e", "wall_w", "wall_n_1",
				"wall_ne", "wall_nw", "door_s", "door_n", "window_s", "roof_n", "loose_house"};
		String[] southKeys = {"wall_s_1", "wall_s_2", "window_s"};
		String[] empty = {"roof_n"};
		String[][] tmp = {empty, {"wall_w"},{"wall_n_1"},{"wall_nw"},{"wall_e"},empty,{"wall_ne"},
				empty, southKeys, southKeys, empty, empty, southKeys, southKeys, empty, empty};
		bitmaskKeys = tmp;
		for (String s : spriteNames){
			sprites.put(s, SpriteManager.get().getSprite(SpriteSheetType.ITEMS, s));
		}
		setSpecialType(SpecialType.HOUSE);
		setSprite(sprites.get("loose_house"));
	}
	
	@Override
	public boolean isWalkable() {
		return false;
	}

	@Override
	public boolean isPlaced() {
		return true;
	}
	
	/**
	 * Calculates bitmask to determine how the house item is surrounded.
	 */
	private static int calcBitmask(int x, int y, boolean recalcSurrounding){
		int total = 0;
		int size = Game.getMap().getSize();
		int[] offsets = {0, 0, -1, 1};
		int add = 1;
		for (int i = 0; i < offsets.length; i++){
			if (x + offsets[i] >= 0 &&
				x + offsets[i] < size &&
				y + offsets[(i+2)%4] >= 0 &&
				y + offsets[(i+2)%4] < size){
				Tile t = Game.getMap().getTile(x + offsets[i], y + offsets[(i+2)%4]);
				boolean containsHouse = false;
				for (Entity e : t.getEntities()){
					if (e.getSpecialType().equals(SpecialType.HOUSE)){
						if (recalcSurrounding) ((House) e).recalcSprite(false);
						containsHouse = true;
						break;
					}
				}
				if (containsHouse){
					total += add;
					add *= 2;
				}
			}
		}
		return total;
	}
	
	
	public void recalcSprite(boolean recalcSurrounding){
		int bitmask = calcBitmask(getTileX(), getTileY(), recalcSurrounding);
		setSprite(sprites.get(bitmaskKeys[bitmask][rand.nextInt(bitmaskKeys[bitmask].length)]));
	}

	@Override
	public void setPlaced(boolean placed) {
		super.setPlaced(placed);
		if (placed){
			recalcSprite(true);			
		} else {
			setSprite(sprites.get("loose_house"));
		}
	}
	
	@Override
	public void draw(float x, float y){
		super.draw(x, y);
	}

	@Override
	public void swing(Humanoid user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use(Humanoid user) {
		Point pt = user.getPlacePoint();
		addToMap(pt.getX(), pt.getY());
		setPlaced(true);
	}

	@Override
	public SpriteInstance[] getSwingArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpriteInstance[] getUseArray() {
		// TODO Auto-generated method stub
		return null;
	}


}
