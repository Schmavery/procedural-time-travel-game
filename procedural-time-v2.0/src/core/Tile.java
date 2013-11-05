package core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import core.AnimationManager.Animation;

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
	
	public Tile(Type type, double perlinVal, int x, int y, Animation anim){
		this.type = type;
		if (type == Type.WATER){
			walkable = false;
		} else {
			walkable = true;
		}
		this.x = x;
		this.y = y;
		this.anim = anim;
		this.perlinVal = perlinVal;
	}
	
	public void setAnim(Animation anim){
		this.anim = anim;
	}
	
	public boolean isWalkable() {return walkable;}
	public Type getType(){return type;}
	public int getX(){return x;}
	public int getY(){return y;}
	public int getTexX(){return anim.getDispX();}
	public int getTexY(){return anim.getDispY();}

	///////////////////////////////////////
	////// Inherited Pathable Methods /////
	///////////////////////////////////////
	
	/**
	 * Implemented with a Manhattan distance measure.
	 */
	public int heuristic(Tile p) {
		return Math.abs(this.x - p.x)  + Math.abs(this.y - p.y); 
	}

	public List<Tile> getReachable() {
		List<Tile> reachable = Arrays.asList(tileMap.getSurroundingTiles(1, x, y));
		reachable.remove(this);
		for (int i = reachable.size(); i >= 0; i--){
			if (!reachable.get(i).walkable){
				reachable.remove(i);
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
			return 14;
		}
		return 10;
	}
}