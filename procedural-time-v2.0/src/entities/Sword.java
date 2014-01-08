package entities;

import core.Tile;
import entityInterfaces.Hittable;
import entityInterfaces.Weapon;

public class Sword extends AbstractItem implements Weapon{
	public int damage = 1;
	
	public Sword(float x, float y) {
		super(x, y);
	}

	@Override
	public void swing(Humanoid user) {
		for (Tile t : user.tileMap.getLocale(2, user.getTileX(), user.getTileY())){
			//check if there is collision with damageable entities
			for (AbstractEntity h : t.getEntities()){
				if (h instanceof Hittable){
					// TODO: Check for collision
					((Hittable) h).hit(this);
				}
			}
		}
	}

	@Override
	public void use(Humanoid user) {
		swing(user);
	}

	@Override
	public int getDamage() {
		return damage;
	}

}
