package core;

import static core.Game.SCALE;
import static core.Game.TILE_SIZE;
import java.io.Serializable;

public class Tile implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static enum Type {
		GRASS, DIRT, WATER, SAND;
	}
	private int x, y;
	private Type type;
	
	public Tile(Type type, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public Type getType(){return type;}
	public int getX(){return x;}
	public int getY(){return y;}
	
}