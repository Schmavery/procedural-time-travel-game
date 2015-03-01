package entities.concrete;

import core.Game;
import core.display.SpriteManager;
import entities.abstr.AbstractPlacedItem;
import entities.components.HealthTracker;
import entities.interfaces.Hittable;
import entities.interfaces.Item;
import entities.interfaces.Placeable;
import entities.interfaces.Weapon;
import gui.GUtil.SpriteSheetType;

public class Tree extends AbstractPlacedItem implements Placeable, Hittable{
	public static enum TreeType {SMALL, BIG, LEAFY, ANY};
	private HealthTracker health;
	private int num_logs;
	
	public Tree(float x, float y, TreeType type){
		super(x, y);
		setPlaced(true);
		setAligned(true);
		setWalkable(false);
		setSpecialType(SpecialType.FOLIAGE);
		TreeType tType;
		if (type.equals(TreeType.ANY)){
			switch (rand.nextInt(5)){
				case 1:
				case 2:
					tType = TreeType.BIG;
					break;
				default:
					tType = TreeType.SMALL;
					break;
			}
		} else tType = type;
		switch (tType){
		case BIG:
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "big_tree"));
			health = new HealthTracker(20);
			num_logs = 5;
			break;
		case LEAFY:
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "tree"));
			health = new HealthTracker(10);
			num_logs = 3;
			break;
		case SMALL:
			setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "christmas_tree"));
			health = new HealthTracker(10);
			num_logs = 3;
			break;
		default:
			break;
		}
	}
	
	public void die(){
		for (int i = 0; i < rand.nextInt(num_logs); i++){
			Item w = new Wood(0, 0);
			float s = Game.SCALE;
			w.addToMap(getX() + rand.nextInt((int)(s*10)) - (int)(s*5), 
					getY() + rand.nextInt((int)(s*10)) - (int)(s*5));
		}
		this.removeFromMap();
	}

	@Override
	public void hit(Weapon w, Human wielder) {
		if(health.damage(w.getDamage())){
			die();
		}
	}
	
	@Override
	public void draw(float x, float y){
		super.draw(x, y);
		health.draw(x + getX(), y + getY());
	}
}
