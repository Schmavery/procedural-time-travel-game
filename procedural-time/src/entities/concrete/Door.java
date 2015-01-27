package entities.concrete;

import entities.interfaces.Holdable;
import entities.interfaces.Placeable;

public class Door extends House implements Placeable, Holdable {
	private String[] bitmaskKeys;
	public Door(float x, float y) {
		super(x, y);
		recalcSprite();
		setWalkable(true);
		String empty = "roof_n";
		String[] tmp = {empty, empty, empty, empty, empty, empty, empty,
				/*7*/ "wall_e", empty, empty, empty, "door_s", empty, 
				/*13*/"wall_w", "door_n", empty};
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
	public void use(Humanoid user) {
		if (place(user)) user.getItem(new Door(0,0));
	}
	
	@Override
	public void recalcSprite() {
		if (placed){
			int bitmask = calcBitmask();
			setSprite(sprites.get(bitmaskKeys[bitmask]));	
		} else {
			setSprite(sprites.get("door_1"));
		}
	}
	
}
