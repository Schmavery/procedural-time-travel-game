package entities.concrete;

import core.display.SpriteManager;
import entities.abstr.AbstractItem;
import entities.components.HealthTracker;
import entities.interfaces.Hittable;
import entities.interfaces.Item;
import entities.interfaces.Placeable;
import entities.interfaces.Weapon;
import gui.GUtil.SpriteSheetType;

public class Tree extends AbstractItem implements Placeable, Hittable{
	public static enum TreeType {SMALL, BIG, LEAFY, ANY};
	private HealthTracker health;
	private int num_logs;
	
	public Tree(float x, float y, TreeType type){
		super(x, y);
		if (type.equals(TreeType.ANY)){
			switch (rand.nextInt(5)){
				case 1:
				case 2:
					type = TreeType.BIG;
					break;
//				case 2:
//					type = TreeType.LEAFY;
//					break;
				default:
					type = TreeType.SMALL;
					break;
			}
		}
		switch (type){
		case BIG:
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "big_tree"));
			health = new HealthTracker(20);
			break;
		case LEAFY:
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "tree"));
			health = new HealthTracker(10);
			break;
		case SMALL:
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "christmas_tree"));
			health = new HealthTracker(10);
			break;
		}
	}
	
	public void die(){
		for (int i = 0; i < rand.nextInt(num_logs); i++){
			Item w = new Wood(0, 0);
			w.addToMap(getX() + rand.nextInt(20) - 10, getY() + rand.nextInt(20) - 10);
		}
		this.removeFromMap();
	}
	
	@Override
	public boolean isWalkable() {
		return false;
	}

	@Override
	public boolean isPlaced() {
		return true;
	}

	@Override
	public void hit(Weapon w, Humanoid wielder) {
	}
	
}
