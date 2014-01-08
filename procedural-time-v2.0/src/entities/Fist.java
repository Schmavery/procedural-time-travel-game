package entities;

import core.Tile;
import entities.interfaces.Entity;
import entities.interfaces.Hittable;
import entities.interfaces.Holdable;
import entities.interfaces.Weapon;

public class Fist extends AbstractItem implements Weapon{
public int damage = 1;

	public Fist() {
		super(0f, 0f);
	}

	@Override
	public void swing(Humanoid user) {
		for (Tile t : user.tileMap.getLocale(2, user.getTileX(), user.getTileY())){
			//check if there is collision with damageable entities
			for (Entity h : t.getEntities()){
				if (h instanceof Hittable){
					// TODO: Check for collision
					((Hittable) h).hit(this);
				}
			}
		}
	}

	@Override
	public void use(Humanoid user) {
		if (user.inventoryFull()){
			return;
		}
		Holdable removeItem = null;
		for (Tile t : user.tileMap.getLocale(2, user.getTileX(), user.getTileY())){
			for (Entity e : t.getEntities()){
				if (e instanceof Holdable){
					// TODO: Check for collision
					user.getItem((Holdable) e);
					removeItem = (Holdable) e;
					break;
				}
			}
			if (removeItem != null){
				t.removeEntity(removeItem);
				
			}
		}
	}

	@Override
	public int getDamage() {
		return damage;
	}

}
