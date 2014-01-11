package entities;

import core.Game;
import core.Tile;
import entities.interfaces.Entity;
import entities.interfaces.Hittable;
import entities.interfaces.Holdable;
import entities.interfaces.Weapon;

public class Fist extends AbstractItem implements Weapon{
public int damage = 1;

	public Fist() {
		super(0f, 0f);
		setAnim(Game.getAnims().getAnim("fist"));
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
		if (user.inventoryFull()){
			return;
		}
		Holdable removeItem = null;
		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
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
	
	
	@Override
	public int getTexX() {
		return 6;
	}
	
	@Override
	public int getTexY() {
		return 0;
	}

}
