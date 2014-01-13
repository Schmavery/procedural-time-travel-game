package entities.interfaces;

import entities.concrete.Humanoid;

public interface Hittable {
	public void hit(Weapon w, Humanoid wielder);
}
