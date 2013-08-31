package entities;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import core.TileMap;

public class Human_Old extends Sentient{
	// walk keys are ordered Center,N,E,S,W,NE,SE,SW,NW
	private final int[] walkKeyX = {0,0,1,0,-1,1,1,-1,-1};
	private final int[] walkKeyY = {0,-1,0,1,0,-1,1,1,-1};
	private int[] animKey  = {1,2,3,1,4,2,1,1,2};
	public Point[] collisionPts;
	public boolean speed;
	
	public int hitPts;
	public String name;
	
	private Random rand = new Random();
	private int randTimer;
	private int direction;
	private Point startPoint;
	private boolean collided;

	private LinkedList<Integer> path = new LinkedList<Integer>();
	
	public Human_Old(Point pos, int numOfAnims){
		this(pos, numOfAnims, null);
	}
	
	public Human_Old(Point pos, int numOfAnims, TileMap tileMap){
		super(numOfAnims);
		if (tileMap != null){
			this.setTileMap(tileMap);
		}
		
		hitPts = rand.nextInt(5)+1;
		this.name = NameGen.gen();
		
		setPos(pos);
		setPrevPos((Point) pos.clone());
		
		int offset = 10;
		int xShift = -2;
		int yShift = 7;
		int tileSize = Entity.tileMap.tileSize - offset;
		this.collisionPts = new Point[4];
		this.collisionPts[0] = new Point(offset + xShift,offset + yShift);
		this.collisionPts[1] = new Point(offset + xShift,tileSize + yShift);
		this.collisionPts[2] = new Point(tileSize + xShift,offset + yShift);
		this.collisionPts[3] = new Point(tileSize + xShift,tileSize + yShift);
		setAnimID(0);
		unStick();
	}
	
	//copy constructor
	public Human_Old(Human_Old human, Point pos){
		this(pos, human.getAnims().length);
		this.name = NameGen.gen();
		this.setAnims(human.getAnims().clone());
		this.setAnimTimes(human.getAnimTimes().clone());
		this.setFacingImgs(human.getFacingImgs().clone());
	}
	
	//returns true once it has reaches its destination
	private boolean moveFull(int direction){
		boolean finished = false;
		Point currCollisionPoint = new Point(this.getPos().x + this.collisionPts[0].x, this.getPos().x + this.collisionPts[0].x);
		
		if (startPoint != null){
			startPoint = currCollisionPoint;
			walk2(direction);
		} else if (direction == 1 && tileMap.getTileFromWorld(currCollisionPoint).getY() == tileMap.getTileFromWorld(startPoint).getY() + 1){
			finished =  true;
		} else if (direction == 3 && tileMap.getTileFromWorld(currCollisionPoint).getY() == tileMap.getTileFromWorld(startPoint).getY() - 1){
			finished =  true;
		} else if (direction == 2 && tileMap.getTileFromWorld(currCollisionPoint).getX() == tileMap.getTileFromWorld(startPoint).getX() + 1){
			finished =  true;
		} else if (direction == 4 && tileMap.getTileFromWorld(currCollisionPoint).getX() == tileMap.getTileFromWorld(startPoint).getX() - 1){
			finished =  true;
		} else if (collided){
			this.setPos(new Point(tileMap.getTileFromWorld(currCollisionPoint).getX(),tileMap.getTileFromWorld(currCollisionPoint).getY()));
		}else {
			walk(direction);
		}
		
		if (finished){
			startPoint = null;
		}
		return finished;	
	}
	
	// returns true once path is completed
	private boolean followPath(int[] path){
		
		
		return false;
	}
	
	public void randMove(){
		if (randTimer < 25 + direction && !collided){
			walk2(direction);
			randTimer++;
		}else {
			randTimer = 0;
			int randInt = rand.nextInt(100);
			if (randInt <= 60){
				direction = 0;
			} else if (randInt >= 61 && randInt <= 70){
				if (direction == 1 || direction == 0){direction = 1;} else {direction = 0;}
			} else if (randInt >= 71 && randInt <= 80){
				if (direction == 2 || direction == 0){direction = 2;} else {direction = 0;}
			} else if (randInt >= 81 && randInt <= 90){
				if (direction == 3 || direction == 0){direction = 3;} else {direction = 0;}
			} else if (randInt >= 91 && randInt <= 100){
				if (direction == 4 || direction == 0){direction = 4;} else {direction = 0;}
			}
		}
		
	}
	
	public void walk2(int direction){
		setActionID(direction);
		if (direction > 0 && direction < 5){
			setFacing(direction);
		}
	}
	public void walk(int direction){
		walk2(direction);
		path.addLast(direction);
	}
	
	public void update(long timePassed){
		super.update(timePassed);
		
		
		if (getActionID() != 0)
			setAnimID(animKey[getActionID()]);
		
		
		setPrevPos(((Point) getPos().clone()));
		
		// move person.  check for hacky speed.
		if (speed) {getPos().translate(walkKeyX[getActionID()]*tileMap.scale*10, walkKeyY[getActionID()]*tileMap.scale*10);}
		else {getPos().translate(walkKeyX[getActionID()]*tileMap.scale, walkKeyY[getActionID()]*tileMap.scale);}
		
		// if resolving the collision doesn't help, unstick the poor dude.
		if (resolveCollision()){
			if (resolveCollision()){
				unStick();
			}
		}

	}
	
	private void unStick(){
		int dx = rand.nextInt(10) - 5;
		int dy = rand.nextInt(10) - 5;
		//System.out.println(dx + "," + dy);
	
		int numTries = 1000;
		for(int i = 10; i < numTries; i++){
			dx = (rand.nextInt(3) - 2) * i;
			while (((getPos().x + dx) < 0 || (getPos().x + dx) > tileMap.mapHeight*tileMap.tileSize)){
				dx = rand.nextInt(i) - (i/2);
			}
			dy = (rand.nextInt(3) - 2) * i;
			while (((getPos().y + dy) < 0 || (getPos().y + dy) > tileMap.mapHeight*tileMap.tileSize)){
				//System.out.println("cat2");
				dy = rand.nextInt(i) - (i/2);
			}
			if(tileMap.getWalkableFromWorld(this.getPos().x + dx, this.getPos().y + dy)){
				break;
			}
		}
		int newX = getPos().x + dx;
		int newY = getPos().y + dy;
		int newTileX = tileMap.getTileFromWorld(newX, newY).getX();
		int newTileY = tileMap.getTileFromWorld(newX, newY).getY();
		getPos().setLocation(new Point(tileMap.getTileFromWorld(newTileX, newTileY).getX(),tileMap.getTileFromWorld(newTileX, newTileY).getY()));
		
	}
	
	public boolean checkCollision(int x, int y){
		boolean collide = false;
		for (int i = 0; i < collisionPts.length; i++){
			if (x > 0 && x < tileMap.mapWidth*tileMap.tileSize && y > 0 && y < tileMap.mapHeight*tileMap.tileSize)
				if (!tileMap.getWalkableFromWorld(collisionPts[i].x + getPos().x, collisionPts[i].y + getPos().y)){
					collide = true;
			} else {collide = true;}
		}
		return collide;
	}
	
	public boolean resolveCollision(){
		boolean collide = false;
		for (int i = 0; i < collisionPts.length; i++){
			if (!tileMap.getWalkableFromWorld(collisionPts[i].x + getPos().x, collisionPts[i].y + getPos().y)){
				collide = true;
			}
		}
		
		
		if (collide){
			boolean works = true;
			for (int i = 0; i < collisionPts.length; i++){
				if(!tileMap.getWalkableFromWorld(collisionPts[i].x + getPrevPos().x, collisionPts[i].y + getPos().y)){
					works = false;
				}
			}
			if (works) {
				getPos().setLocation(getPrevPos().x, getPos().y);
				return true;
			}
			works = true;
			for (int i = 0; i < collisionPts.length; i++){
				if(!tileMap.getWalkableFromWorld(collisionPts[i].x + getPos().x, collisionPts[i].y + getPrevPos().y)){
					works = false;
				}
			}
			if (works) {
				getPos().setLocation(getPos().x, getPrevPos().y);
				return true;
			}
			setPos(getPrevPos());
			return true;
		} else {
			return false;
		}	
	}
	

}
