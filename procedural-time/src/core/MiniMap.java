package core;

import gui.GUtil;

import java.util.HashMap;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

import core.Tile.TileType;


public class MiniMap {
	
	private int size;
	private HashMap<String, ReadableColor> colors;
	
	public MiniMap(int size){
		this.size = size;
		colors = new HashMap<>();
		colors.put(TileType.DIRT.name(), new Color(144, 133, 96));
		colors.put(TileType.GRASS.name(), new Color(191, 200, 134));
		colors.put(TileType.SAND.name(), new Color(217, 188, 108));
		colors.put(TileType.WATER.name(), new Color(76, 137, 172));
		colors.put("TREE", new Color(23, 152, 35));
		colors.put("PLAYER", ReadableColor.WHITE);
	}
	
	public int getSize(){
		return size;
	}
	
	public void draw(float x, float y, Tile center){
		Tile start = null;
		ReadableColor c;
		for (Tile tile : Game.getMap().getLocale(size/2, center.getX(), center.getY())){
			if (start == null){
				start = tile;
			}
			c = colors.get(tile.getType().name());
			
			if (tile.walkable && !tile.isWalkable()){
				c = colors.get("TREE");
			}
			
			if (tile.equals(center)){
				c = colors.get("PLAYER");
			}
			GUtil.drawPixel((int)(x + Game.SCALE*(tile.getX() - start.getX())), 
					(int)(y + Game.SCALE*(tile.getY() - start.getY())), (int) Game.SCALE, c);
		}
	}
}
