package entities.abstr;

import java.util.Random;

import core.AnimationManager.Animation;
import core.Game;
import entities.EntityFrame;
import entities.interfaces.Entity;
import gui.GUtil.SpriteSheet;

public abstract class AbstractEntity implements Entity
{

	public static enum Facing {NORTH, EAST, SOUTH, WEST}

	protected float x;
	protected float y;
	protected Animation[] standingAnims;
	protected Facing facing;
	protected EntityFrame frame;
	protected Random rand;

	public AbstractEntity(float x, float y)
	{
		standingAnims = new Animation[4];
		this.x = x;
		this.y = y;
		rand = new Random();
		facing = Facing.NORTH;
	}
	
	public void setAnim(Animation anim){
		this.standingAnims[0] = anim.cloneAnim();
	}

	public void setStandingAnims(Animation anim_n, Animation anim_e, Animation anim_s, Animation anim_w)
	{
		this.standingAnims[0] = anim_n.cloneAnim();
		this.standingAnims[1] = anim_e.cloneAnim();
		this.standingAnims[2] = anim_s.cloneAnim();
		this.standingAnims[3] = anim_w.cloneAnim();
	}

	public float getX()
	{return x;}

	public float getY()
	{return y;}

	public int getTileX()
	{return (int) (x/(Game.SCALE*Game.TILE_SIZE));}

	public int getTileY()
	{return (int) (y/(Game.SCALE*Game.TILE_SIZE));}

//	public int getTexX()
//	{ 
//		return standingAnims[facing.ordinal()].getTexX();
//	}
//
//	public int getTexY()
//	{
//		return standingAnims[facing.ordinal()].getTexY();
//	}
	
	public void draw(float x, float y){
//		GUtil.drawSprite (getSpriteSheet(), getX() + x,
//				getY() + y, getTexX(), getTexY(), Game.SCALE*Game.TILE_SIZE, Game.SCALE*Game.TILE_SIZE, 16);
		standingAnims[facing.ordinal()].draw(x + getX(), y + getY());
	}
	
	protected abstract SpriteSheet getSpriteSheet();

}