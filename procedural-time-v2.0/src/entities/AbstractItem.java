package entities;

import core.Game;
import entities.interfaces.Item;
import gui.GUtil.SpriteSheet;

public abstract class AbstractItem extends AbstractEntity implements Item
{
	public static enum ItemState {LOOSE, PLACED, HELD, STOWED}
	
//	private ItemState state;
	public AbstractItem(float x, float y){
		super(x, y);
	}

	protected SpriteSheet getSpriteSheet(){
		return SpriteSheet.ITEMS;
	}
	
	public void removeFromMap(){
		Game.getMap().getTile(getTileX(), getTileY()).removeEntity(this);
	}
	
	public void addToMap(float x, float y){
		this.x = x;
		this.y = y;
		if (Game.getMap().getTile(getTileX(), getTileY()) == null)
			System.out.println("...");
		else
			Game.getMap().getTile(getTileX(), getTileY()).addEntity(this);
	}
}