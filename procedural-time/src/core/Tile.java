package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import core.display.SpriteInstance;
import core.path.Pathable;
import entities.interfaces.Entity;
import entities.interfaces.Entity.SpecialType;
import entities.interfaces.Placeable;

public class Tile implements Serializable, Pathable<Tile>{
	
	private static final long serialVersionUID = 1L;
	public static enum TileType {
		GRASS, DIRT, WATER, SAND;
	}

	private int x, y;
	private TileType type;
	private SpriteInstance spr;
	double perlinVal;
	boolean walkable;
	List<Entity> entities;
	
	public Tile(TileType type, double perlinVal, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
		this.perlinVal = perlinVal;
		this.entities = new ArrayList<>();
		if (type == TileType.WATER){
			walkable = false;
		} else {
			walkable = true;
		}
		
	}
	
	public void setSprite(SpriteInstance spr){
		this.spr = spr;
//		if (spr.getModel().getName().indexOf("rock") > -1){
//			walkable = false;
//		}
	}
	
	public boolean isWalkable() {
		if (!walkable){
			return false;
		} else {
			for (Entity e : entities){
				if (e instanceof Placeable){
					if (((Placeable) e).isPlaced() && !((Placeable) e).isWalkable()){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public TileType getType(){return type;}
	public int getGridX(){return x;}
	public int getGridY(){return y;}
	public float getTop(){return Game.SCALE*Game.TILE_SIZE*y;}
	public float getLeft(){return Game.SCALE*Game.TILE_SIZE*x;}
	
	public void draw(int x, int y){
		spr.draw(x, y);
	}
	

	///////////////////////////////////////
	////// Inherited Pathable Methods /////
	///////////////////////////////////////
	
	/**
	 * Implemented with a Manhattan distance measure.
	 */
	public int heuristic(Tile p) {
		return 5*(Math.abs(this.x - p.x)  + Math.abs(this.y - p.y)); 
	}
	
	private static void addWalkableTileToList(List<Tile> list, int x, int y){
		if (isWalkableTile(x, y)){
			list.add(Game.getMap().getGridTile(x, y));
		}
	}
	
	private static boolean isWalkableTile(int x, int y){
		if (x >= 0
				&& x < Game.getMap().getSize()
				&& y >= 0
				&& y < Game.getMap().getSize()){
			if (Game.getMap().getGridTile(x, y).isWalkable()){
				return true;
			}
		}
		return false;
	}

	public List<Tile> getReachable() {
		List<Tile> reachable = new ArrayList<>(8);
		addWalkableTileToList(reachable, x - 1, y);
		addWalkableTileToList(reachable, x    , y - 1);
		addWalkableTileToList(reachable, x + 1, y);
		addWalkableTileToList(reachable, x    , y + 1);
		
		if (isWalkableTile(x+1, y+1)){
			if (isWalkableTile(x+1, y) && isWalkableTile(x, y+1)){
				reachable.add(Game.getMap().getGridTile(x+1, y+1));
			}
		}
		
		if (isWalkableTile(x-1, y-1)){
			if (isWalkableTile(x-1, y) && isWalkableTile(x, y-1)){
				reachable.add(Game.getMap().getGridTile(x-1, y-1));
			}
		}
		
		if (isWalkableTile(x-1, y+1)){
			if (isWalkableTile(x-1, y) && isWalkableTile(x, y+1)){
				reachable.add(Game.getMap().getGridTile(x-1, y+1));
			}
		}
		
		if (isWalkableTile(x+1, y-1)){
			if (isWalkableTile(x+1, y) && isWalkableTile(x, y-1)){
				reachable.add(Game.getMap().getGridTile(x+1, y-1));
			}
		}
		return reachable;
	}

	public boolean isSameNode(Tile p) {
		return (p.x == this.x && p.y == this.y);
	}

	public int moveCost(Tile p) {
		if (p.x - this.x != 0 && p.y - this.y != 0){
			// Diagonal, costs a little more.
			return 30;
		}
		return 10;
	}
	
	///////////////////////////
	//// Entity Management ////
	///////////////////////////
	
	public void addEntity(Entity h){
		entities.add(h);
		Game.getEM().addEntity(h);
	}
	
	public void removeEntity(Entity h){
		entities.remove(h);
		Game.getEM().removeEntity(h);
	}
	
	/**
	 * Removes the first entity found of a given type
	 * @param type
	 * @return Whether an entity was removed
	 */
	public boolean removeEntityBySpecialType(SpecialType type){
		Iterator<Entity> iter = entities.iterator();
		Entity e;
		while (iter.hasNext()){
			e = iter.next();
			if (e.getSpecialType().equals(type)){
				Game.getEM().removeEntity(e);
				iter.remove();
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns unmodifiable list of entities.
	 * @return
	 */
	public List<Entity> getEntities(){
		return Collections.unmodifiableList(entities);
	}
	
	public boolean hasSpecialType(SpecialType type){
		for (Entity e : entities){
			if (e.getSpecialType().equals(type)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "Tile: " + x + ", " + y + " - " + type;
	}
	
}