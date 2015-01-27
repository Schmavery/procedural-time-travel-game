package entities.abstr;

import core.Game;
import entities.interfaces.Item;

public abstract class AbstractItem extends AbstractEntity implements Item
{
	public AbstractItem(float x, float y){
		super(x, y);
	}
	
	public AbstractItem(){
		super(0, 0);
	}
	
	public void removeFromMap(){
		Game.getMap().getGridTile(getTileX(), getTileY()).removeEntity(this);
		this.x = 0;
		this.y = 0;
	}
	
	public void addToMap(float x, float y){
		this.x = x;
		this.y = y;
		Game.getMap().getGridTile(getTileX(), getTileY()).addEntity(this);
	}
	
	@Override
	public void draw(float x, float y){
		facing = Facing.NORTH;
		super.draw(x, y);
	}
}