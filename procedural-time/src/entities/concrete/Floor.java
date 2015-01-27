package entities.concrete;

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
	public void recalcSprite(){
		if (placed){
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "floor_"+(rand.nextInt(2)+1)));
		} else {
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "loose_floor"));
		}
	}
	
	@Override
	public void swing(Humanoid user) {
		//TODO
	}

	@Override
	public void use(Humanoid user) {
		if (place(user)) user.getItem(new Floor(0,0));
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
