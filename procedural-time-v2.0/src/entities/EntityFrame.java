package entities;

import core.Game;
import core.Tile;
import core.TileMap;

public class EntityFrame {
	private float[] xCorners;
	private float[] yCorners;
	
	public EntityFrame(){
		float size = Game.TILE_SIZE*Game.SCALE;
		xCorners = new float[4];
		yCorners = new float[4];
		xCorners[0] = 0;
		xCorners[1] = size;
		xCorners[2] = size;
		xCorners[3] = 0;
		yCorners[0] = 0;
		yCorners[1] = 0;
		yCorners[2] = size;
		yCorners[3] = size;
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
			Tile tile = tm.getTile(getX(i, x), getY(i, y));
			if (!tile.isWalkable()){
				return true;
			}
		}
		return false;
	}
}
