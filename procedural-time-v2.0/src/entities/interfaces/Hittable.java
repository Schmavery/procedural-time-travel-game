package entities.interfaces;

import entities.Humanoid;

public interface Hittable {
	public void hit(Weapon w, Humanoid wielder);
}
