package core;

import entities.interfaces.Entity;
import gui.GUtil;

import java.util.HashMap;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;

import core.Tile.TileType;


public class MiniMap {
	
	private int size;
	private Rectangle frame;
	private HashMap<String, ReadableColor> colors;
	
	public MiniMap(int size){
		this.size = size;
		frame = new Rectangle(0, 0, (int) (size*Game.SCALE) + 20, (int) (size*Game.SCALE) + 20);
		
		colors = new HashMap<>();
		colors.put(TileType.DIRT.name(), new Color(144, 133, 96));
		colors.put(TileType.GRASS.name(), new Color(191, 200, 134));
		colors.put(TileType.SAND.name(), new Color(217, 188, 108));
		colors.put(TileType.WATER.name(), new Color(76, 137, 172));
		colors.put("TREE", new Color(23, 152, 35));
		colors.put("HOUSE", new Color(212, 108, 109));
		colors.put("NPC", ReadableColor.YELLOW);
		colors.put("PLAYER", ReadableColor.WHITE);
	}
	
	public int getSize(){
		return size;
	}
	
	public ReadableColor getColor(Tile tile, Tile center){
		if (tile.equals(center)) return colors.get("PLAYER");
		
		ReadableColor c = colors.get(tile.getType().name());
		
		boolean placed = tile.walkable && !tile.isWalkable();
		
		// Faster than calling tile.hasSpecialType for each type.
		// TODO: fix this so it isn't deterministic on the order of the list
		for (Entity e: tile.getEntities()){
			switch (e.getSpecialType()) {
			case FOLIAGE:
				if (placed) c = colors.get("TREE");
				break;
			case HOUSE:
				if (placed) c = colors.get("HOUSE");
				break;
			case PERSON:
				if (!e.equals(Game.getPlayer())) c = colors.get("NPC");
				break;
			default:
				break;
			}
		}
		return c;
	}
	
	public void draw(float x, float y, Tile center){
		frame.setLocation((int) x - 10, (int) y - 10); 
		GUtil.drawRect(frame, ReadableColor.DKGREY);
		
		Tile start = null;
		for (Tile tile : Game.getMap().getLocale(size/2, center.getGridX(), center.getGridY())){
			if (start == null){
				start = tile;
			}
			
			GUtil.drawPixel((int)(x + Game.SCALE*(tile.getGridX() - start.getGridX())), 
					(int)(y + Game.SCALE*(tile.getGridY() - start.getGridY())), (int) Game.SCALE, getColor(tile, center));
		}
	}
}
