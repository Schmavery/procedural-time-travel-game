package editor;

import java.awt.Image;
import java.awt.Point;

import core.*;

public abstract class Entity {
	private static int nextID = 0;
	public static TileMap tileMap;
	public int tileSize;
	
	private int id;             	//id of specific entity
	private Point pos;         		//current position of entity
	private Point prevPos;			//position of entity during previous cycle (for collision detection)
	private int actionID;			//current action being performed by entity (types of movement, attacks, etc)
	private int animID;				//current animation being shown for this entity
	private int nextAnimID;			//next animID to be assigned
	private Animation[] anims;		//array of animations pertinent to the entity
	private long[] animTimes;		//length of each array (for timing attacks etc)
	private int facing; 			//direction the enitity is facing
	private Image[] facingImgs;		//image of each idle position
	
	//constructor
	public Entity(int numOfAnims){
		this.id = nextID;
		nextID++;
		this.animID = 0;
		this.actionID = 0;
		this.nextAnimID = 0;
		anims = new Animation[numOfAnims];
		animTimes = new long[numOfAnims];
		facing = 3;
		facingImgs = new Image[4];
	}
	
	//getters and setters
	public Point getPos() {return pos;}
	public int getID(){return id;}
	public Point getPrevPos() {return prevPos;}
	public int getActionID() {return actionID;}
	public int getFacing() {return facing;}
	public Animation[] getAnims() {return anims;}
	public long[] getAnimTimes() {return animTimes;}
	public TileMap getTileMap() {return tileMap;}
	public Image[] getFacingImgs() {return facingImgs;}
	
	public void setPos(Point pos) {this.pos = pos;}
	public void setPrevPos(Point prevPos) {this.prevPos = prevPos;}
	public void setAnimID(int id) {this.animID = id;}
	public void setActionID(int id) {this.actionID = id;}
	public void setFacing(int face) {this.facing = face;}
	public void setTileMap(TileMap tileMap) {Entity.tileMap = tileMap;}
	public void setAnims(Animation[] anims) {this.anims = anims;}
	public void setAnimTimes(long[] animTimes) {this.animTimes = animTimes;}
	public void setFacingImgs(Image[] imgs) {this.facingImgs = imgs;}
	
	public int initAnim(){
		 // call this to add a new animation to the entity
		 anims[nextAnimID] = new Animation();
		 animTimes[nextAnimID] = 0;
		 nextAnimID++;
		 return nextAnimID - 1;
	}
	
	public void update(long timePassed){
		//anims[animID].updateScene(timePassed);
		return;
	}
	
	public void addScene(int animID, Image img, long length){
		anims[animID].addScene(img, length);
		animTimes[animID] += length;
	}
	
	public void addAnim(int index, Animation anim){
		anims[index] = anim;
	}
	
	public void setFacingImg(int direction, Image img){
		facingImgs[direction - 1] = img;
	}

	public Image getImage(){
		if (actionID == 0) {
			return facingImgs[facing - 1];
		} else {return anims[animID].getImage();}
	}

}

