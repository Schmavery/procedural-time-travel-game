package entities;

import core.Game;
import core.Tile;
import core.TileMap;

public class EntityFrame {
	private float[] xCorners;
	private float[] yCorners;
	private float xCenter;
	private float yCenter;
	
	public EntityFrame(int padding, int offset){
		float size = Game.TILE_SIZE*Game.SCALE - padding;
		xCenter = size/2;
		yCenter = (size/2) + offset;
		xCorners = new float[4];
		yCorners = new float[4];
		xCorners[0] = padding;
		xCorners[1] = size;
		xCorners[2] = size;
		xCorners[3] = padding;
		yCorners[0] = padding + offset;
		yCorners[1] = padding + offset;
		yCorners[2] = size + offset;
		yCorners[3] = size + offset;
	}
	
	private float getX(int index, float x){
		if (index < 0 || index > 3){
			System.out.println("Invalid index requested.");
			return 0;
		}
		return xCorners[index] + x;
	}

	private float getY(int index, float y){
		if (index < 0 || index > 3){
			System.out.println("Invalid index requested.");
			return 0;
		}
		return yCorners[index] + y;
	}
	
	public boolean isColliding(TileMap tm, float x, float y){
		for (int i = 0; i < 4; i++){
			Tile tile = tm.getWorldTile(getX(i, x), getY(i, y));
			if (tile != null && !tile.isWalkable()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isContained(Tile tile, float x, float y){
		int tileSide = (int) (Game.TILE_SIZE*Game.SCALE);
		for (int i = 0; i < 4; i++){
			if (getX(i, x) <= tile.getLeft()
					|| getX(i, x) >= (tile.getLeft() + tileSide)
					|| getY(i, y) <= tile.getTop()
					|| getY(i, y) >= (tile.getTop() + tileSide))
				return false;
		}
		return true;
	}
	
	public float getCenterX(float x){
		return xCenter + x;
	};
	public float getCenterY(float y){
		return yCenter + y;
	};
}
