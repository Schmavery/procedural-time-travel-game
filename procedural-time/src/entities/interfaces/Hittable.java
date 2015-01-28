package entities.interfaces;

import entities.concrete.Human;

public interface Hittable {
	public void hit(Weapon w, Human wielder);
}
