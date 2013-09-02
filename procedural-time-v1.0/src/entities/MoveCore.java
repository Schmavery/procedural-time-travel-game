package entities;

import java.awt.Point;
import java.util.Random;

import core.TileMap;

public class MoveCore {
	
	private TileMap tileMap;
	Point pos, prevPos;
	Point[] collisionPts;
	private Random rand = new Random();
	private int randTimer;	
	
	//private double speed = 1.5;
	private double speed = 3;
	
	private int[] walkKeyX = {0,1,1,1,0,-1,-1,-1};
	private int[] walkKeyY = {-1,-1,0,1,1,1,0,-1};
	
	int facing = 4;
	boolean walking, fast, collided;
	
	MoveCore(Point pos, Point prevPos, TileMap tileMap, Point[] collisionPts){
		this.pos = pos;
		this.prevPos = prevPos;
		this.tileMap = tileMap;
		this.collisionPts = collisionPts;
		CollisionCore.unStick(pos);
	}
	
	public void walk(int direction){
		facing = direction;
		walking = true;
	}
	
	public void stop(){
		walking = false;
	}
	
	public void update(long delta){
		prevPos = (Point) pos.clone();
		
		if (walking){
			if (fast) {pos.translate(walkKeyX[facing]*tileMap.scale*10, walkKeyY[facing]*tileMap.scale*10);}
			else {
				pos.setLocation(pos.getX() + walkKeyX[facing]*tileMap.scale*speed, pos.getY() + walkKeyY[facing]*tileMap.scale*speed);
				//pos.translate(walkKeyX[facing]*tileMap.scale*speed, walkKeyY[facing]*tileMap.scale*speed);
				}
		
		}	
		if (CollisionCore.resolveCollision(pos, prevPos, collisionPts)){
			if (CollisionCore.checkCollision(prevPos, collisionPts)){
				CollisionCore.unStick(pos);
			}
			collided = true;
		}else {collided = false;}
		
	}
	
	
	
	public void randMove(){
		if (randTimer < 25 + facing && !collided){
			if (walking) {walk(facing);}
			randTimer++;
		}else {
			randTimer = 0;
			int randInt = rand.nextInt(99);
			
			if (randInt <= 60){
				walking = false;
			} else if (randInt >= 61 && randInt <= 70){
				if (facing == 0 || walking == false){facing = 0; walking = true;} else {walking = false;}
			} else if (randInt >= 71 && randInt <= 80){
				if (facing == 2 || walking == false){facing = 2; walking = true;} else {walking = false;}
			} else if (randInt >= 81 && randInt <= 90){
				if (facing == 4 || walking == false){facing = 4; walking = true;} else {walking = false;}
			} else if (randInt >= 91 && randInt <= 100){
				if (facing == 6 || walking == false){facing = 6; walking = true;} else {walking = false;}
			}
		
		}
	}
	
	private boolean centerOnTile(){
		pos.setLocation(pos.getX(), pos.getY());
		return true;
	}
}
