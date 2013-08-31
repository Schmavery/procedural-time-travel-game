package entities;

import java.awt.Point;
import java.util.Random;

import core.TileMap;

public class CollisionCore {

	static Random rand;
	static TileMap tileMap;
	
	public static void init(Random rand, TileMap tileMap){
		CollisionCore.rand = rand;
		CollisionCore.tileMap = tileMap;
	}
	
	public static boolean checkCollision(Point pos, Point[] collisionPts){
		boolean collide = false;
		for (int i = 0; i < collisionPts.length; i++){
			if (!tileMap.getWalkableFromWorld(collisionPts[i].x + pos.x, collisionPts[i].y + pos.y)){
				collide = true;
			}
		}
		return collide;
	}
	
	public static boolean resolveCollision(Point pos, Point prevPos, Point[] collisionPts){
		boolean collide = false;
		for (int i = 0; i < collisionPts.length; i++){
			if (!tileMap.getWalkableFromWorld(collisionPts[i].x + pos.x, collisionPts[i].y + pos.y)){
				collide = true;
			}
		}
		
		
		if (collide){
			boolean works = true;
			for (int i = 0; i < collisionPts.length; i++){
				if(!tileMap.getWalkableFromWorld(collisionPts[i].x + prevPos.x, collisionPts[i].y + pos.y)){
					works = false;
				}
			}
			if (works) {
				pos.setLocation(prevPos.x, pos.y);
				return true;
			}
			
			works = true;
			for (int i = 0; i < collisionPts.length; i++){
				if(!tileMap.getWalkableFromWorld(collisionPts[i].x + pos.x, collisionPts[i].y + prevPos.y)){
					works = false;
				}
			}
			if (works) {
				pos.setLocation(pos.x, prevPos.y);
				return true;
			}
			
			pos.setLocation(prevPos.x, prevPos.y);
			return true;
		} else {
			return false;
		}	
	}
	
	static void unStick(Point pos){
		int dx = rand.nextInt(10) - 5;
		int dy = rand.nextInt(10) - 5;
	
		int numTries = 1000;
		for(int i = 10; i < numTries; i++){
			dx = (rand.nextInt(3) - 2) * i;
			while (((pos.x + dx) < 0 || (pos.x + dx) > tileMap.mapHeight*tileMap.tileSize)){
				dx = rand.nextInt(i) - (i/2);
			}
			dy = (rand.nextInt(3) - 2) * i;
			while (((pos.y + dy) < 0 || (pos.y + dy) > tileMap.mapHeight*tileMap.tileSize)){
				dy = rand.nextInt(i) - (i/2);
			}
			if(tileMap.getWalkableFromWorld(pos.x + dx, pos.y + dy)){
				break;
			}
		}
		int newX = pos.x + dx;
		int newY = pos.y + dy;
		int newTileX = tileMap.getTileFromWorld(newX, newY).getX();
		int newTileY = tileMap.getTileFromWorld(newX, newY).getY();
		pos.setLocation(new Point(tileMap.getTileFromWorld(newTileX, newTileY).getX(),tileMap.getTileFromWorld(newTileX, newTileY).getY()));
	}
	
}
