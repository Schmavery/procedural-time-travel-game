package entities.interfaces;

public interface Placeable extends Entity{
	public boolean isWalkable();
	public boolean isPlaced();
	public void setPlaced(boolean placed);
}
