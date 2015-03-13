package entities.concrete;

import java.util.HashMap;

import core.Game;
import core.Tile;
import core.display.Sprite;
import core.display.SpriteInstance;
import core.display.SpriteManager;
import entities.abstr.AbstractPlacedItem;
import entities.interfaces.Holdable;
import entities.interfaces.Placeable;
import entities.town.House;
import gui.GUtil.SpriteSheetType;

public class HousePiece extends AbstractPlacedItem implements Placeable, Holdable {

	protected HashMap<String, Sprite> sprites;
	private String[][] bitmaskKeys;
	private House house;
	
	public HousePiece(float x, float y){
		super(x, y);
		sprites = new HashMap<>();
		String[] spriteNames = {"wall_s_1", "wall_s_2", "wall_e", "wall_w", "wall_n_1",
				"wall_ne", "wall_nw", "door_s", "door_n", "window_s", "roof_n",
				"loose_house", "door_n", "door_s", "door_e", "door_w", "door_e_open", 
				"door_w_open","door_1"};
		String[] southKeys = {"wall_s_1", "wall_s_2", "window_s"};
		String[] empty = {"roof_n"};
		String[][] tmp = {empty, empty, empty, southKeys, empty, empty, {"wall_ne"},
				/*7*/ {"wall_e"}, empty, southKeys, empty, southKeys, {"wall_nw"}, 
				/*13*/{"wall_w"}, {"wall_n_1"}, empty};
		bitmaskKeys = tmp;
		for (String s : spriteNames){
			sprites.put(s, SpriteManager.get().getSprite(SpriteSheetType.ITEMS, s));
		}
		setSpecialType(SpecialType.HOUSE);
		setSprite(sprites.get("loose_house"));
		setWalkable(false);
		setAligned(true);
	}
	
	/**
	 * Calculates bitmask to determine how the house item is surrounded.
	 * Key below indicates which cardinal directions have adjacent house tiles
	 * 0	- none
	 * 1	- n
	 * 2	- w
	 * 3	- nw
	 * 4	- s
	 * 5	- ns
	 * 6	- ws
	 * 7	- nws
	 * 8	- e
	 * 9	- ne
	 * 10	- ew
	 * 11	- nwe
	 * 12	- se
	 * 13	- nse
	 * 14	- wse
	 * 15	- nwse
	 */
	protected int calcBitmask() {
		int total = 0;
		int size = Game.getMap().getSize();
		// N, W, S, E
		int[] offsets = {0, -1, 0, 1};
		int add = 1;
		for (int i = 0; i < offsets.length; i++){
			if (getTileX() + offsets[i] >= 0 &&
				getTileX() + offsets[i] < size &&
				getTileY() + offsets[(i+1)%4] >= 0 &&
				getTileY() + offsets[(i+1)%4] < size){
				Tile t = Game.getMap().getGridTile(getTileX() + offsets[i], getTileY() + offsets[(i+1)%4]);
				
				if (t.hasSpecialType(SpecialType.HOUSE)) total += add;
				
				add *= 2;
			}
		}
		return total;
	}

	@Override
	public void recalcSprite() {
		if (placed){
			int bitmask = calcBitmask();
			setSprite(sprites.get(bitmaskKeys[bitmask][rand.nextInt(bitmaskKeys[bitmask].length)]));	
			if (bitmask == 7 || bitmask == 13){
				// Sidewalls of the house
				setDrawPriority(1);
			} else {
				setDrawPriority(0);
			}
		} else {
			setSprite(sprites.get("loose_house"));
		}
	}
	
	@Override
	public void draw(float x, float y){
		super.draw(x, y);
	}

	@Override
	public void swing(Human user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use(Human user) {
		if (place(user)) user.getItem(new HousePiece(0,0));
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

	public void setHouse(House h){
		this.house = h;
	}


}
