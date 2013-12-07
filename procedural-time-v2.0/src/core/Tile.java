package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import core.AnimationManager.Animation;
import entities.Human;

public class Tile implements Serializable, Pathable<Tile>{
	
	private static final long serialVersionUID = 1L;
	public static enum Type {
		GRASS, DIRT, WATER, SAND;
	}
	static TileMap tileMap;

	private int x, y;
	private Type type;
	private Animation anim;
	double perlinVal;
	boolean walkable;
	LinkedList<Human> entities;
	
	public Tile(Type type, double perlinVal, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
		this.perlinVal = perlinVal;
		this.entities = new LinkedList<Human>();
		if (type == Type.WATER){
			walkable = false;
		} else {
			walkable = true;
		}
	}
	
	public void setAnim(Animation anim){
		this.anim = anim;
		if (anim.toString().indexOf("rock") > -1){
			walkable = false;
		}
	}
	
	public boolean isWalkable() {return walkable;}
	public Type getType(){return type;}
	public int getX(){return x;}
	public int getY(){return y;}
	public int getTexX(){return anim.getDispX();}
	public int getTexY(){return anim.getDispY();}
	public float getTop(){return Game.SCALE*Game.TILE_SIZE*y;}
	public float getLeft(){return Game.SCALE*Game.TILE_SIZE*x;}
	

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
		if (x >= 0
				&& x < tileMap.getSize()
				&& y >= 0
				&& y < tileMap.getSize()){
			if (tileMap.getTile(x, y).walkable){
				list.add(tileMap.getTile(x, y));
			}
		}
	}

	public List<Tile> getReachable() {
		List<Tile> reachable = new ArrayList<>(4);
		addTile(reachable, x - 1, y);
		addTile(reachable, x    , y - 1);
		addTile(reachable, x + 1, y);
		addTile(reachable, x    , y + 1);
		return reachable;
	}

	public boolean isSameNode(Tile p) {
		return (p.x == this.x && p.y == this.y);
	}

	public int moveCost(Tile p) {
		if (p.x - this.x != 0 && p.y - this.y != 0){
			// Diagonal, costs a little more.
			return 14;
		}
		return 10;
	}
	
	public void addEntity(Human h){
		entities.add(h);
	}
	
	public void removeEntity(Human h){
		entities.remove(h);
	}
	
	@Override
	public String toString(){
		return "Tile: " + x + ", " + y;
	}
	
}