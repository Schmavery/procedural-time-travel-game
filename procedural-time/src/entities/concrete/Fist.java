package entities.concrete;

import core.Game;
import core.Tile;
import core.display.SpriteInstance;
import core.display.SpriteManager;
import entities.abstr.AbstractItem;
import entities.interfaces.Entity;
import entities.interfaces.Hittable;
import entities.interfaces.Holdable;
import entities.interfaces.Weapon;
import gui.GUtil.SpriteSheetType;

public class Fist extends AbstractItem implements Weapon{
public int damage = 1;

	public Fist() {
		super();
		setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "fist"));
	}

	@Override
	public void swing(Humanoid user) {
		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
			//check if there is collision with damageable entities
			for (Entity h : t.getEntities()){
				if (h instanceof Hittable){
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
		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
			for (Entity e : t.getEntities()){
				if (e instanceof Holdable){
					// TODO: Check for collision
					if (user.getItem((Holdable) e));
						((Holdable) e).removeFromMap();
					return;
				}
			}			
		}
	}

	@Override
	public int getDamage() {
		return damage;
	}
	
	@Override
	public SpriteInstance[] getSwingArray(){
		return null;
	}
	
	@Override
	public SpriteInstance[] getUseArray(){
		return null;
	}
}
