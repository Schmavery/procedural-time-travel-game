package entities.components;

import gui.GUtil;

import org.lwjgl.util.ReadableColor;

import core.Game;
import core.Tile;
import core.TileMap;

public class EntityFrame {
	private float[] xCorners;
	private float[] yCorners;
	private float xCenter;
	private float yCenter;
	
	public EntityFrame(int xPadding, int yPadding, int yOffset){
		float scaledXPadding = Game.SCALE*xPadding;
		float scaledYPadding = Game.SCALE*yPadding;
		float scaledOffset = Game.SCALE*yOffset;
		float xSize = Game.SCALE*Game.TILE_SIZE - scaledXPadding;
		float ySize = Game.SCALE*Game.TILE_SIZE - scaledYPadding;
		xCenter = xSize/2;
		yCenter = (ySize/2) + scaledOffset;
		xCorners = new float[4];
		yCorners = new float[4];
		xCorners[0] = scaledXPadding;
		xCorners[1] = xSize;
		xCorners[2] = xSize;
		xCorners[3] = scaledXPadding;
		yCorners[0] = scaledYPadding + scaledOffset;
		yCorners[1] = scaledYPadding + scaledOffset;
		yCorners[2] = ySize + scaledOffset;
		yCorners[3] = ySize + scaledOffset;
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
	}
	
	public float getCenterY(float y){
		return yCenter + y;
	}
	
	public void draw(float x, float y){
		for (int i = 0; i < xCorners.length; i++){
			GUtil.drawPixel((int) getX(i, x), (int) getY(i, y), 2, ReadableColor.PURPLE); 
		}
	}
}
