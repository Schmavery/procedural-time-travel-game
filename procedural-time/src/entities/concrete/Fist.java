package entities.concrete;

import org.lwjgl.util.Point;

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
	public void swing(Human user) {
//		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
		Point pt = user.getPlacePoint();
		Tile t = Game.getMap().getWorldTile(pt.getX(), pt.getY());
			//check if there is collision with damageable entities
			for (Entity h : t.getEntities()){
				if (h instanceof Hittable){
					// TODO: Check for collision
					((Hittable) h).hit(this, user);
				}
			}
//		}
	}

	@Override
	public void use(Human user) {
		if (user.inventoryFull()){
			return;
		}
//		Tile t = Game.getMap().getWorldTile(pt.getX(), pt.getY());
		Point pt = user.getPlacePoint();
		Holdable min = null;
		int minDist = Integer.MAX_VALUE;
		for (Tile t : Game.getMap().getLocale(2, user.getTileX(), user.getTileY())){
			for (Entity e : t.getEntities()){
				// TODO: Check for collision
				if (e instanceof Holdable){
					if (getDist(pt, e) < minDist){
						minDist = getDist(pt, e);
						min = (Holdable) e;
					}
				}
			}
		}
		if (min != null && user.getItem(min)){
			(min).removeFromMap();
		}
	}
	
	public static int getDist(Point pt, Entity e){
		int dist = (pt.getX()- (int) e.getX())*(pt.getX()- (int) e.getX());
		dist += (pt.getY()- (int) e.getY())*(pt.getY()- (int) e.getY());
		return dist;
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
