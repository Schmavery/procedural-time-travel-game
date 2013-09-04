package core;

public class Tile {
	public static enum Type {
		GRASS, DIRT, WATER;
	}
	
	private Type type;
	
	public Tile(Type type){
		this.type = type;
	}
	
	public Type getType(){
		return type;
	}
}