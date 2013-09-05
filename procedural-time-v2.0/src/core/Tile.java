package core;

import java.io.Serializable;

public class Tile implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static enum Type {
		GRASS, DIRT, WATER, SAND;
	}

	private int x, y;
	private Type type;
	private int textureID;
	private int texX, texY;
	
	private int calcTextureID(){
		
		return 0;
	}
	
	public Tile(Type type, int textureId, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
		textureID = calcTextureID();
	}
	
	public Type getType(){return type;}
	public int getX(){return x;}
	public int getY(){return y;}
	public int getTexX(){return texX;}
	public int getTexY(){return texY;}
	
}