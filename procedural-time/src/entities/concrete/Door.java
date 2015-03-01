package entities.concrete;

import core.Game;
import entities.interfaces.Holdable;
import entities.interfaces.Placeable;

public class Door extends HousePiece implements Placeable, Holdable {
	private String[] bitmaskKeys;
	private int currBitMask = 0;
	public Door(float x, float y) {
		super(x, y);
		recalcSprite();
		setWalkable(true);
		String empty = "roof_n";
		String[] tmp = {empty, empty, empty, empty, empty, empty, empty,
				/*7*/ "door_e", empty, empty, empty, "door_s", empty, 
				/*13*/"door_w", "door_n", empty};
		bitmaskKeys = tmp;
		
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
	@Override
	public void use(Human user) {
		if (place(user)) user.getItem(new Door(0,0));
	}
	
	@Override
	public void recalcSprite() {
		if (placed){
			currBitMask = calcBitmask();
			setSprite(sprites.get(bitmaskKeys[currBitMask]));
			if (currBitMask == 7 || currBitMask == 13){
				// Sidewalls of the house
				setDrawPriority(1);
			} else {
				setDrawPriority(0);
			}
		} else {
			setSprite(sprites.get("door_1"));
			currBitMask = 0;
		}
	}
	
	@Override
	public void draw(float x, float y){
		// Redundant check because hasSpecialType is expensive.
		if (currBitMask == 7 || currBitMask == 13){
			if (Game.getMap().getGridTile(getTileX(), getTileY()).hasSpecialType(SpecialType.PERSON)){
				if (currBitMask == 7){
					setSprite(sprites.get("door_e_open"));
				} else {
					setSprite(sprites.get("door_w_open"));
				}
			} else {
				if (currBitMask == 7){
					setSprite(sprites.get("door_e"));
				} else {
					setSprite(sprites.get("door_w"));
				}
			}
		}
		super.draw(x, y);
	}
	
}
