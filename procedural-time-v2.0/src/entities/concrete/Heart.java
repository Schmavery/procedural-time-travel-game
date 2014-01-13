package entities.concrete;

import entities.abstr.AbstractItem;
import entities.interfaces.Hittable;
import entities.interfaces.Weapon;

public class Heart extends AbstractItem implements Hittable{
	
	
	public Heart(float x, float y) {
		super(x, y);
	}

	@Override
	public void hit(Weapon w, Humanoid wielder) {
		removeFromMap();
		wielder.setHealth(wielder.getHealth() + 1);;
	}

//	@Override
//	public int getTexX() {
//		return 2;
//	}
//	
//	@Override
//	public int getTexY() {
//		return 0;
//	}
	
}
