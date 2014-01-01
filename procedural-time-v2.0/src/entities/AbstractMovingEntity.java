package entities;

import core.AnimationManager.Animation;
import core.PathException;
import core.PathFinder;
import core.Tile;

public abstract class AbstractMovingEntity extends AbstractEntity
{

	protected float dx;
	protected float dy;
	protected float speed;
	protected boolean moving;
	protected boolean collided;
	protected Animation[] movingAnims;
	protected PathFinder<Tile> tilePather;

	public AbstractMovingEntity(float x, float y)
	{
		super(x, y);
	}

	public void move(float dx, float dy)
	{
		this.dx += dx;
		this.dy += dy;
	}

	public void move(Facing f)
	{
		facing = f;
		moving = true;
	}

	public void walkTo(int tileX, int tileY)
	{
		if (tileMap.getTile(tileX, tileY) == null || !tileMap.getTile(tileX, tileY).isWalkable()){
			return;
		} else {
			tilePather.clear();
			tilePather.newPath(	tileMap.getWorldTile(getCenterX(), getCenterY()),
					tileMap.getTile(tileX, tileY));
			pathGen();
		}
	}

	public void pathGen()
	{
		if (tilePather.isRunning()){
			try {
				tilePather.generatePath(100);
			} catch (PathException e){
				System.out.println(e.getMessage());
				tilePather.clear();
			}
		}
	}

	public float getCenterX()
	{
		return frame.getCenterX(x);
	}

	public float getCenterY()
	{
		return frame.getCenterY(y);
	}

	public void setMovingAnims(Animation anim_n, Animation anim_e, Animation anim_s, Animation anim_w)
	{
		this.movingAnims[0] = anim_n.cloneAnim();
		this.movingAnims[1] = anim_e.cloneAnim();
		this.movingAnims[2] = anim_s.cloneAnim();
		this.movingAnims[3] = anim_w.cloneAnim();
	}

	public boolean isMoving()
	{return moving;}
	
	@Override
	public int getTexX()
	{ 
		if (moving){
			return movingAnims[facing.ordinal()].getDispX();
		}
		return standingAnims[facing.ordinal()].getDispX();
	}

	@Override
	public int getTexY()
	{
		if (moving)
			return movingAnims[facing.ordinal()].getDispY();
		return standingAnims[facing.ordinal()].getDispY();
	}

}