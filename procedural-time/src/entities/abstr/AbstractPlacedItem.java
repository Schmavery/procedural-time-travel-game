package entities.abstr;

import org.lwjgl.util.Point;

import core.Game;
import core.Tile;
import entities.concrete.Humanoid;
import entities.interfaces.Entity;
import entities.interfaces.Placeable;



public abstract class AbstractPlacedItem extends AbstractItem implements Placeable{
	protected boolean placed;
	protected boolean walkable;
	protected boolean aligned;
	
	public AbstractPlacedItem(float x, float y) {
		super(x, y);
		// DEFAULTS
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
	public void removeFromMap(){
		super.removeFromMap();
		setPlaced(false);
	}
	
	@Override
	public void setPlaced(boolean placed) {
		System.out.println("setting placed");
		this.placed = placed;
		recalcSprite();
		int[] offsets = {0, -1, 0, 1};
		float size = (Game.getMap().getSize()*Game.TILE_SIZE*Game.SCALE);
		for (int i = 0; i < offsets.length; i++){
			if (getTileX() + offsets[i] >= 0 &&
					getTileX() + offsets[i] < size &&
					getTileY() + offsets[(i+1)%4] >= 0 &&
					getTileY() + offsets[(i+1)%4] < size){
				Tile t = Game.getMap().getGridTile(getTileX() + offsets[i], getTileY() + offsets[(i+1)%4]);
				if (t == null || t.getEntities() == null) {
//					System.out.println("Null:"+(x + offsets[i]) + "," + (y + offsets[(i+3)%4]));
					continue;
				}
				for (Entity e : t.getEntities()){
					if (e instanceof Placeable){
						((Placeable) e).recalcSprite();
					}
				}
				
			}
		}
	};
	
	@Override
	public boolean isPlaced() {
		return placed;
	}
	
	public void setAligned(boolean aligned) {
		this.aligned = aligned;
	}
	
	@Override
	public boolean place(int x, int y){
		Tile t = Game.getMap().getGridTile(x, y);
		if (t == null) return false;
		if (!t.isWalkable()) return false;
		
		// Otherwise place item
		addToMap(t.getLeft(), t.getTop());
		setPlaced(true);
		return true;
	}
	
	@Override
	public boolean place(Humanoid owner){
		Point pt = owner.getPlacePoint();
		int placeX = snapToTile(pt.getX());
		int placeY = snapToTile(pt.getY());
		Tile t = Game.getMap().getWorldTile(placeX, placeY);
		if (t == null) return false;
		if (!t.isWalkable()) return false;
		
		// Otherwise place item
		addToMap(placeX, placeY);
		setPlaced(true);
		owner.removeCurrentItem();
		return true;
	}
	
	/**
	 * No side effects, simply returns a world coordinate snapped to the 
	 * closest (but under) tile boundary.
	 * @param coord
	 * @return
	 */
	private static int snapToTile(float coord){
		int scale = (int) (Game.TILE_SIZE*Game.SCALE);
		return ((int) coord/scale)*scale;
	}	
	
	@Override
	public void recalcSprite() {
		// Do nothing as default action
	}
	
}
