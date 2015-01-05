package entities.abstr;

import entities.interfaces.Placeable;

public abstract class AbstractPlacedItem extends AbstractItem implements Placeable{
	protected boolean placed;
	protected boolean walkable;
	protected boolean aligned;
	
	public AbstractPlacedItem(float x, float y) {
		super(x, y);
	}
	
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
	
	@Override
	public boolean isWalkable() {
		return walkable;
	}
	
	@Override
	public void setPlaced(boolean placed) {
		this.placed = placed;
	};
	
	@Override
	public boolean isPlaced() {
		return placed;
	}
	
	public void setAligned(boolean aligned) {
		//TODO: Implement autoalignment
		this.aligned = aligned;
	}
}
