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
	private Animation anim;
	double perlinVal;
	
	float elevation;
	float humidity;
	
	private int calcTextureID(){
		
		return 0;
	}
	
	public Tile(Type type, double perlinVal, int x, int y, Animation anim){
		this.type = type;
		this.x = x;
		this.y = y;
		this.anim = anim;
		this.perlinVal = perlinVal;
	}
	
	public void setAnim(Animation anim){
		this.anim = anim;
	}
	
	public Type getType(){return type;}
	public int getX(){return x;}
	public int getY(){return y;}
	public int getTexX(){return anim.getDispX();}
	public int getTexY(){return anim.getDispY();}
}