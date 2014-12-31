package entities.concrete;

import java.util.Iterator;

import core.Game;
import core.Tile;
import core.display.SpriteInstance;
import core.display.SpriteManager;
import entities.abstr.AbstractItem;
import entities.interfaces.Entity;
import entities.interfaces.Hittable;
import entities.interfaces.Weapon;
import gui.GUtil.SpriteSheetType;

public class Sword extends AbstractItem implements Weapon{
	public int damage = 5;
	
	public Sword(float x, float y) {
		super(x, y);
		setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "sword"));
	}

	@Override
	public void swing(Humanoid user) {
		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
			//check if there is collision with damageable entities
//			for (Entity h : t.getEntities(); ){
			Entity h;
			for(Iterator<Entity> iter = t.getEntities().iterator(); iter.hasNext();){
				h = iter.next();
				if (h instanceof Hittable){
					// TODO: Check for collision
//					System.out.println("Hitting "+h+"on tile "+t+"->"+t.getEntities());
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
	public SpriteInstance[] getSwingArray(){
		return null;
	}
	
	@Override
	public SpriteInstance[] getUseArray(){
		return null;
	}
}
