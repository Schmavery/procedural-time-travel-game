package entities.town;

import core.Game;
import core.Tile;

public class SpinePoint {
	public static enum SpineType {OUTER, WELL};
	private SpineType type;
	private Tile tile;
	
	public SpinePoint(int gridX, int gridY, SpineType type){
		this.type = type;
		tile = Game.getMap().getGridTile(gridX, gridY);
	}

	public SpineType getType() {
		return type;
	}

	public void setType(SpineType type) {
		this.type = type;
	}
	
	public Tile getTile(){
		return tile;
	}
	
	public int getX(){ return tile.getGridX();}
	public int getY(){ return tile.getGridY();}
}
