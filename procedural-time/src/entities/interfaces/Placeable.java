package entities.interfaces;

import entities.concrete.Human;


public interface Placeable extends Entity{
	public boolean isWalkable();
	public boolean isPlaced();
	public void setPlaced(boolean placed);
	public void recalcSprite();

	/**
	 * Places the item on the ground
	 * @param gridX
	 * @param gridY
	 * @return true if place succeeds
	 */
	public boolean place(int gridX, int gridY);
	
	/**
	 * Places the item on the ground and removes it from the owner
	 * @param owner
	 * @return true if the place succeeds.
	 */
	public boolean place(Human owner);
}
