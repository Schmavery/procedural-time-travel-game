package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import core.display.SpriteInstance;
import core.path.Pathable;
import entities.interfaces.Entity;
import entities.interfaces.Placeable;

public class Tile implements Serializable, Pathable<Tile>{
	
	private static final long serialVersionUID = 1L;
	public static enum Type {
		GRASS, DIRT, WATER, SAND;
	}

	private int x, y;
	private Type type;
	private SpriteInstance spr;
	double perlinVal;
	boolean walkable;
	List<Entity> entities;
	
	public Tile(Type type, double perlinVal, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
		this.perlinVal = perlinVal;
		this.entities = new LinkedList<>();
		if (type == Type.WATER){
			walkable = false;
		} else {
			walkable = true;
		}
	}
	
	public void setAnim(SpriteInstance spr){
		this.spr = spr;
		if (spr.getModel().getName().indexOf("rock") > -1){
			walkable = false;
		}
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
	
	public Type getType(){return type;}
	public int getX(){return x;}
	public int getY(){return y;}
//	public int getTexX(){return spr.getTexX();}
//	public int getTexY(){return spr.getTexY();}
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
	
	private void addTile(List<Tile> list, int x, int y){
		if (isWalkableTile(x, y)){
			list.add(Game.getMap().getTile(x, y));
		}
	}
	
	private boolean isWalkableTile(int x, int y){
		if (x >= 0
				&& x < Game.getMap().getSize()
				&& y >= 0
				&& y < Game.getMap().getSize()){
			if (Game.getMap().getTile(x, y).walkable){
				return true;
			}
		}
		return false;
	}

	public List<Tile> getReachable() {
		List<Tile> reachable = new ArrayList<>(4);
		addTile(reachable, x - 1, y);
		addTile(reachable, x    , y - 1);
		addTile(reachable, x + 1, y);
		addTile(reachable, x    , y + 1);
		
		if (isWalkableTile(x+1, y+1)){
			if (isWalkableTile(x+1, y) && isWalkableTile(x, y+1)){
				reachable.add(Game.getMap().getTile(x+1, y+1));
			}
		}
		
		if (isWalkableTile(x-1, y-1)){
			if (isWalkableTile(x-1, y) && isWalkableTile(x, y-1)){
				reachable.add(Game.getMap().getTile(x-1, y-1));
			}
		}
		
		if (isWalkableTile(x-1, y+1)){
			if (isWalkableTile(x-1, y) && isWalkableTile(x, y+1)){
				reachable.add(Game.getMap().getTile(x-1, y+1));
			}
		}
		
		if (isWalkableTile(x+1, y-1)){
			if (isWalkableTile(x+1, y) && isWalkableTile(x, y-1)){
				reachable.add(Game.getMap().getTile(x+1, y-1));
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
	
	//// Entity Management ////
	
	public void addEntity(Entity h){
		entities.add(h);
	}
	
	public void removeEntity(Entity h){
		entities.remove(h);
	}
	
	public List<Entity> getEntities(){
		return entities;
	}
	
	@Override
	public String toString(){
		return "Tile: " + x + ", " + y + " - " + type;
	}
	
}