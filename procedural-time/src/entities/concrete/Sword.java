package entities.concrete;

import core.Game;
import core.Tile;
import core.display.Animation;
import core.display.AnimationManager;
import entities.abstr.AbstractItem;
import entities.interfaces.Entity;
import entities.interfaces.Hittable;
import entities.interfaces.Weapon;

public class Sword extends AbstractItem implements Weapon{
	public int damage = 5;
	
	public Sword(float x, float y) {
		super(x, y);
		setAnim(AnimationManager.getAnim("sword"));
	}

	@Override
	public void swing(Humanoid user) {
		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
			//check if there is collision with damageable entities
			for (Entity h : t.getEntities()){
				if (h instanceof Hittable){
					// TODO: Check for collision
					((Hittable) h).hit(this, user);
				} else {
					System.out.println("Not hittable");
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
	public Animation[] getSwingArray(){
		return null;
	}
	
	@Override
	public Animation[] getUseArray(){
		return null;
	}
}
