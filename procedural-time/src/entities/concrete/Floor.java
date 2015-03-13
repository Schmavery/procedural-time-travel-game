package entities.concrete;

import core.display.SpriteInstance;
import core.display.SpriteManager;
import entities.abstr.AbstractPlacedItem;
import entities.interfaces.Holdable;
import entities.town.House;
import gui.GUtil.SpriteSheetType;

public class Floor extends AbstractPlacedItem implements Holdable{
	
	private House house;
	
	public Floor(float x, float y) {
		super(x, y);
		setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "loose_floor"));
		setSpecialType(SpecialType.HOUSE);
		setDrawPriority(-2);
		setWalkable(true);
		setAligned(true);
	}

	@Override
	public void recalcSprite(){
		if (placed){
			if (house != null){	
				setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, house.getSpritePrefix()+"floor_"+(rand.nextInt(2)+1)));
			} else {
				setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "floor_"+(rand.nextInt(2)+1)));
			}
		} else {
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "loose_floor"));
		}
	}
	
	@Override
	public void swing(Human user) {
		//TODO
	}

	@Override
	public void use(Human user) {
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
	
	public void setHouse(House h){
		this.house = h;
	}

}
