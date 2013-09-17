package core;

import java.io.Serializable;

import core.AnimationManager.Animation;

public class Tile implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static enum Type {
		GRASS, DIRT, WATER, SAND;
	}

	private int x, y;
	private Type type;
	private int textureID;
	private int texX, texY;
	private Animation anim;
	double perlinVal;
	
	float elevation;
	float humidity;
	
	private int calcTextureID(){
		
		return 0;
	}
	
	public Tile(Type type, double perlinVal, int textureID, int x, int y, Animation anim){
		this.type = type;
		this.x = x;
		this.y = y;
		//this.texX = textureID % 16;
		//this.texY = textureID / 16;
		//this.textureID = textureID;
		this.anim = anim;
		this.perlinVal = perlinVal;
	}
	
	public void setTextureID(int textureID){
		this.texX = textureID % 16;
		this.texY = textureID / 16;
		this.textureID = textureID;
	}
	
	public void setAnim(Animation anim){
		this.anim = anim;
	}
	
	public Type getType(){return type;}
	public int getX(){return x;}
	public int getY(){return y;}
	//public int getTexX(){return texX;}
	//public int getTexY(){return texY;}
	public int getTexX(){return anim.getDispX();}
	public int getTexY(){return anim.getDispY();}
	public int getTexID(){return textureID;}
	
}