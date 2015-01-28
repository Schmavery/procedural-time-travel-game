package entities.concrete;

import org.lwjgl.util.Point;

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
	public void swing(Human user) {
//		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
			//check if there is collision with damageable entities
			// Old methods of iterating
//			for (Entity h : t.getEntities(); ){
//			for(Iterator<Entity> iter = t.getEntities().iterator(); iter.hasNext();){
//				h = iter.next();
		Point pt = user.getPlacePoint();
		Tile t = Game.getMap().getWorldTile(pt.getX(), pt.getY());
		Entity h;
		for (int i = t.getEntities().size() - 1; i >= 0; i--){
			h = t.getEntities().get(i);
			if (h instanceof Hittable){
				((Hittable) h).hit(this, user);
			} else {
				System.out.println("Not hittable");
			}
		}
//		}
	}

	@Override
	public void use(Human user) {
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
