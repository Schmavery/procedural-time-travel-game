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
	
	float elevation;
	float humidity;
	
	private int calcTextureID(){
		
		return 0;
	}
	
	public Tile(Type type, int textureID, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
		this.texX = textureID % 16;
		this.texY = textureID / 16;
		//System.out.println(x);
//		textureID = calcTextureID();
	}
	
	public Type getType(){return type;}
	public int getX(){return x;}
	public int getY(){return y;}
	public int getTexX(){return texX;}
	public int getTexY(){return texY;}
	
}