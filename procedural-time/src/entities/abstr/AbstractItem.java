package entities.abstr;

import core.Game;
import core.display.SpriteManager;
import entities.interfaces.Item;
import gui.GUtil.SpriteSheetType;

public abstract class AbstractItem extends AbstractEntity implements Item
{
//	public static enum ItemState {LOOSE, PLACED, HELD, STOWED}
//	private ItemState state;
	public AbstractItem(float x, float y){
		super(x, y);
	}
	
	public AbstractItem(){
		super(0, 0);
	}
	
	public void removeFromMap(){
		Game.getMap().getTile(getTileX(), getTileY()).removeEntity(this);
		this.x = 0;
		this.y = 0;
	}
	
	public void addToMap(float x, float y){
		this.x = x;
		this.y = y;
		warpToClosestClearTile();
		Game.getMap().getTile(getTileX(), getTileY()).addEntity(this);
	}
	
	@Override
	public void draw(float x, float y){
		facing = Facing.NORTH;
		super.draw(x, y);
	}
}