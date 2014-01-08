package entities;

import core.AnimationManager.Animation;
import core.Game;
import core.TileMap;
import entities.interfaces.Entity;
import gui.GUtil;
import gui.GUtil.SpriteSheet;

public abstract class AbstractEntity implements Entity
{

	public static enum Facing {NORTH, EAST, SOUTH, WEST}

	protected TileMap tileMap;
	protected float x;
	protected float y;
	protected Animation[] standingAnims;
	protected Facing facing;
	protected EntityFrame frame;

	public AbstractEntity(float x, float y)
	{
		super();
		this.x = x;
		this.y = y;
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

	public int getTexX()
	{ 
		return standingAnims[facing.ordinal()].getDispX();
	}

	public int getTexY()
	{
		return standingAnims[facing.ordinal()].getDispY();
	}
	
	public void draw(float x, float y){
		GUtil.drawSprite (getSpriteSheet(), getX() + x,
				getY() + y, getTexX(), getTexY(), Game.SCALE*Game.TILE_SIZE, Game.SCALE*Game.TILE_SIZE, 16);
	}
	
	protected abstract SpriteSheet getSpriteSheet();

}