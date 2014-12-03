package entities.abstr;

import core.Game;
import core.Tile;
import core.display.Sprite;
import core.display.SpriteInstance;
import core.path.PathException;
import core.path.PathFinder;

public abstract class AbstractMovingEntity extends AbstractEntity
{

	protected float dx;
	protected float dy;
	protected float speed;
	protected boolean moving;
	protected boolean collided;
	protected SpriteInstance[] movingAnims;
	protected PathFinder<Tile> tilePather;
	

	public AbstractMovingEntity(float x, float y)
	{
		super(x, y);
		movingAnims = new SpriteInstance[4];
	}

	public void move(float dx, float dy)
	{
		this.dx += dx;
		this.dy += dy;
		tilePather.clear();
	}

	public void move(Facing f)
	{
		facing = f;
		moving = true;
	}
	
	public void seek(){
		//TODO 
	}
	
	public void flee(){
		//TODO
	}
	
	public void arrive(){
		//TODO
	}
	
	public void pursue(){
		//TODO
	}
	
	public void evade(){
		//TODO
	}
	
	public void wander(){
		//TODO
	}

	public void walkTo(int tileX, int tileY)
	{
		if (Game.getMap().getTile(tileX, tileY) == null || !Game.getMap().getTile(tileX, tileY).isWalkable()){
			return;
		} else {
			tilePather.clear();
			tilePather.newPath(	Game.getMap().getWorldTile(getCenterX(), getCenterY()),
					Game.getMap().getTile(tileX, tileY));
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

	public void setMovingAnims(Sprite spr_n, Sprite spr_e, Sprite spr_s, Sprite spr_w)
	{
		this.movingAnims[0] = spr_n.getInstance();
		this.movingAnims[1] = spr_e.getInstance();
		this.movingAnims[2] = spr_s.getInstance();
		this.movingAnims[3] = spr_w.getInstance();
	}

	public boolean isMoving()
	{return moving;}
	
	@Override
	public void draw(float x, float y){
		if (moving)
			movingAnims[facing.ordinal()].draw(x + getX(), y + getY());
		else 
			super.draw(x, y);
	}

}