package entities.concrete;

import entities.abstr.AbstractItem;
import entities.interfaces.Hittable;
import entities.interfaces.Weapon;

public class Heart extends AbstractItem implements Hittable{
	
	
	public Heart(float x, float y) {
		super(x, y);
	}

	@Override
	public void hit(Weapon w, Human wielder) {
		removeFromMap();
		wielder.setHealth(wielder.getHealth() + 1);;
	}
	
}
