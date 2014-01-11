package entities;

import core.Game;
import core.Tile;
import entities.interfaces.Entity;
import entities.interfaces.Hittable;
import entities.interfaces.Weapon;

public class Sword extends AbstractItem implements Weapon{
	public int damage = 3;
	
	public Sword(float x, float y) {
		super(x, y);
		setAnim(Game.getAnims().getAnim("sword"));
	}

	@Override
	public void swing(Humanoid user) {
		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
			//check if there is collision with damageable entities
			for (Entity h : t.getEntities()){
				if (h instanceof Hittable && !h.equals(user)){
					// TODO: Check for collision
					((Hittable) h).hit(this, user);
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
	
	@Override
	public int getTexX() {
		return 5;
	}
	
	@Override
	public int getTexY() {
		return 0;
	}
}
