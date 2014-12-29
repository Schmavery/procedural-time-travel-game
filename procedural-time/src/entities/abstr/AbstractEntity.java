package entities.abstr;

import java.util.Random;

import core.Game;
import core.display.Sprite;
import core.display.SpriteInstance;
import entities.EntityFrame;
import entities.interfaces.Entity;

public abstract class AbstractEntity implements Entity
{
	// Note: The order of this enum is important, as the ordinal
	// is used as an array index.
	public static enum Facing {NORTH, EAST, SOUTH, WEST}

	protected float x;
	protected float y;
	protected SpriteInstance[] standingAnims;
	protected Facing facing;
	protected EntityFrame frame;
	protected Random rand;

	public AbstractEntity(float x, float y)
	{
		standingAnims = new SpriteInstance[4];
		this.x = x;
		this.y = y;
		rand = new Random();
		facing = Facing.NORTH;
	}
	
	public void setSprite(Sprite spr){
		if (spr != null){
			this.standingAnims[0] = spr.getInstance();
		} else {
			System.out.println("Tried to set null sprite");
		}
	}

	public void setStandingAnims(Sprite spr_n, Sprite spr_e, Sprite spr_s, Sprite spr_w)
	{
		this.standingAnims[0] = spr_n.getInstance();
		this.standingAnims[1] = spr_e.getInstance();
		this.standingAnims[2] = spr_s.getInstance();
		this.standingAnims[3] = spr_w.getInstance();
	}

	public float getX()
	{return x;}

	public float getY()
	{return y;}

	public int getTileX()
	{return (int) (x/(Game.SCALE*Game.TILE_SIZE));}

	public int getTileY()
	{return (int) (y/(Game.SCALE*Game.TILE_SIZE));}
	
	public void draw(float x, float y){
		standingAnims[facing.ordinal()].draw(x + getX(), y + getY());
	}
	
	public void draw(float x, float y, float w, float h){
		standingAnims[facing.ordinal()].draw(x + getX(), y + getY(), w, h);
	}
	
}