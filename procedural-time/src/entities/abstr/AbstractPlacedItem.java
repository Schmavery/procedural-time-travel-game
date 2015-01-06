package entities.abstr;

import org.lwjgl.util.Point;

import core.Game;
import entities.concrete.Humanoid;
import entities.interfaces.Placeable;

public abstract class AbstractPlacedItem extends AbstractItem implements Placeable{
	protected boolean placed;
	protected boolean walkable;
	protected boolean aligned;
	
	public AbstractPlacedItem(float x, float y) {
		super(x, y);
		placed = false;
		walkable = true;
		aligned = false;
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
	
	public void place(Humanoid owner){
		Point pt = owner.getPlacePoint();
		setPlaced(true);
		int placeX = snapToTile(pt.getX());
		int placeY = snapToTile(pt.getY());
		addToMap(placeX, placeY);
		setPlaced(true);
		owner.removeCurrentItem();
	}
	
	private static int snapToTile(float coord){
		int scale = (int) (Game.TILE_SIZE*Game.SCALE);
		return ((int) coord/scale)*scale;
	}	
}
