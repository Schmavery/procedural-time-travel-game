package entities.concrete;

import core.display.SpriteInstance;
import core.display.SpriteManager;
import entities.abstr.AbstractItem;
import entities.interfaces.Holdable;
import gui.GUtil.SpriteSheetType;

public class Wood extends AbstractItem implements Holdable{
	public Wood(float x, float y) {
		super(x, y);
		setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "wood"));
	}

	@Override
	public void swing(Humanoid user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use(Humanoid user) {
		// TODO Auto-generated method stub
		
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
