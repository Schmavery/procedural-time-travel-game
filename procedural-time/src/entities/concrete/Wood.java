package entities.concrete;

import core.display.Animation;
import core.display.AnimationManager;
import entities.abstr.AbstractItem;
import entities.interfaces.Holdable;

public class Wood extends AbstractItem implements Holdable{
	public Wood(float x, float y) {
		super(x, y);
		setAnim(AnimationManager.getAnim("wood"));
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
	public Animation[] getSwingArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation[] getUseArray() {
		// TODO Auto-generated method stub
		return null;
	}
}
