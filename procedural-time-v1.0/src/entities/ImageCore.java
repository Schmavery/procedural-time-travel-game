package entities;

import java.awt.Image;

import core.Animation;

public class ImageCore implements Cloneable{
	
	//private int nextAnimID;			//next animID to be assigned
	private Animation[] anims;		//array of animations pertinent to the entity
	private long[] animTimes;		//length of each array (for timing attacks etc)
	private Image[] facingImgs;		//image of each idle position
	
	//constructor
	public ImageCore(int numOfAnims){
		anims = new Animation[numOfAnims];
		animTimes = new long[numOfAnims];
		facingImgs = new Image[numOfAnims];
	}
	
	public ImageCore clone(){
		ImageCore core = new ImageCore(numAnims());
		core.anims = this.anims.clone();
		core.animTimes = this.animTimes.clone();
		core.facingImgs= this.facingImgs.clone();
		return core;
	}
	
	public int numAnims() {return anims.length;}
	
	
/*	public int initAnim(){
		 // call this to add a new animation to the entity
		 anims[nextAnimID] = new Animation();
		 animTimes[nextAnimID] = 0;
		 nextAnimID++;
		 return nextAnimID - 1;
	}*/
	
	public void addScene(int animID, Image img, long length){
		anims[animID].addScene(img, length);
		animTimes[animID] += length;
	}
	
	public void addAnim(int index, Animation anim){
		anims[index] = anim;
	}
	
	public void setFacingImg(int direction, Image img){
		facingImgs[direction] = img;
	}

	public Image getImage(int facing, boolean walking){
		if (facing%2==1){facing--;}
		
		if (walking){
			return anims[facing].getImage();
		} else {
			return facingImgs[facing];
		}
	}


}
