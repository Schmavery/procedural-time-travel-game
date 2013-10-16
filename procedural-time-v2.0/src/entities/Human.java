package entities;

import core.AnimationManager.Animation;
import core.TileMap;

public class Human {
	public static enum Facing {NORTH, EAST, SOUTH, WEST};
	
	private TileMap tileMap;
	private float x, y;
	private float dx, dy;
	private boolean moving;
	private Animation[] movingAnims;
	private Animation[] standingAnims;
	private Facing facing;
	
	public Human(float x, float y, TileMap tileMap){
		this.x = x;
		this.y = y;
		this.tileMap = tileMap;
		this.facing = Facing.SOUTH;
		movingAnims = new Animation[4];
		standingAnims = new Animation[4];
	}
	
	public void update(long deltaTime){
		moving = (dx != 0 || dy != 0);
			
		if (dy < 0){
			facing = Facing.NORTH;
		} else if (dy > 0){
			facing = Facing.SOUTH;
		} else if (dx > 0){
			facing = Facing.EAST;
		} else if (dx < 0){
			facing = Facing.WEST;
		}
			
		x += dx;
		y += dy;
		dx = 0;
		dy = 0;
		
		
		if (moving){
			movingAnims[facing.ordinal()].update(deltaTime);
		}
	}
	
	public void move(float dx, float dy){
		this.dx += dx;
		this.dy += dy;
	}
	
	public void setMovingAnims( Animation anim_n, Animation anim_e, Animation anim_s, Animation anim_w ){
		this.movingAnims[0] = anim_n.cloneAnim();
		this.movingAnims[1] = anim_e.cloneAnim();
		this.movingAnims[2] = anim_s.cloneAnim();
		this.movingAnims[3] = anim_w.cloneAnim();
	}

	public void setStandingAnims( Animation anim_n, Animation anim_e, Animation anim_s, Animation anim_w ){
		this.standingAnims[0] = anim_n.cloneAnim();
		this.standingAnims[1] = anim_e.cloneAnim();
		this.standingAnims[2] = anim_s.cloneAnim();
		this.standingAnims[3] = anim_w.cloneAnim();
	}
	
	public float getX(){return x;}
	public float getY(){return y;}
	
	public int getTexX(){ 
		if (moving){
			return movingAnims[facing.ordinal()].getDispX();
		}
		return standingAnims[facing.ordinal()].getDispX();
	}	
	public int getTexY(){
		if (moving)
			return movingAnims[facing.ordinal()].getDispY();
		return standingAnims[facing.ordinal()].getDispY();
	}	
}
