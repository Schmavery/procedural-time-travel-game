package entities.concrete;

import core.Game;
import core.Tile;
import core.display.SpriteInstance;
import core.display.SpriteManager;
import entities.abstr.AbstractPlacedItem;
import entities.interfaces.Holdable;
import gui.GUtil.SpriteSheetType;

public class Floor extends AbstractPlacedItem implements Holdable{
	
	public Floor(float x, float y) {
		super(x, y);
		setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "loose_floor"));
		setSpecialType(SpecialType.HOUSE);
		setDrawPriority(-1);
		setWalkable(true);
		setAligned(true);
	}

	@Override
	public void setPlaced(boolean placed){
		super.setPlaced(placed);
		if (placed){
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "floor_"+(rand.nextInt(2)+1)));
		} else {
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "loose_floor"));
		}
	}
	
	@Override
	public void swing(Humanoid user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use(Humanoid user) {
		place(user);
	}
	
//	@Override
//	public void place(Humanoid user){
//		super.place(user);
//		int[] offsets = {-1, 0, 1, 0};
//		Tile t;
//		for (int i = 0; i < offsets.length; i++){
//			t = Game.getMap().getTile(user.getTileX() + offsets[i], user.getTileY() + offsets[(i+3)%4]);
//		}
//	}

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
