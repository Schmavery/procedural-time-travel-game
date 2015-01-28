package entities.town;

import org.lwjgl.util.Point;

import core.Game;
import core.Tile;

public class SpinePoint {
	public static enum SpineType {OUTER, WELL};
	private SpineType type;
	private Point pt;
	private Tile tile;
	
	public SpinePoint(int gridX, int gridY, SpineType type){
		this.pt = new Point(gridX, gridY);
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
}
