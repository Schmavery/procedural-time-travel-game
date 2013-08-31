package editor;

import java.awt.Point;
import java.util.Random;

import editor.TileMap;

public class Human extends Sentient{
	// walk keys are ordered Center,N,E,S,W,NE,SE,SW,NW
	private final int[] walkKeyX = {0,0,1,0,-1,1,1,-1,-1};
	private final int[] walkKeyY = {0,-1,0,1,0,-1,1,1,-1};
	private int[] animKey  = {1,2,3,1,4,2,1,1,2};
	public Point[] collisionPts;
	public boolean speed;
	
	private Random rand = new Random();
	private int randTimer;
	private int randAct;
	private boolean collided;
	
	public Human(Point pos, int numOfAnims){
		this(pos, numOfAnims, null);
	}
	
	public Human(Point pos, int numOfAnims, TileMap tileMap){
		super(numOfAnims);
		if (tileMap != null){
			this.setTileMap(tileMap);
		}
		
		setPos(pos);
		setPrevPos((Point) pos.clone());
		
		int offset = 10;
		int xShift = -2;
		int yShift = 9;
		int tileSize = Entity.tileMap.tileSize - offset;
		this.collisionPts = new Point[4];
		this.collisionPts[0] = new Point(offset + xShift,offset + yShift);
		this.collisionPts[1] = new Point(offset + xShift,tileSize + yShift);
		this.collisionPts[2] = new Point(tileSize + xShift,offset + yShift);
		this.collisionPts[3] = new Point(tileSize + xShift,tileSize + yShift);
		setAnimID(0);
	}
	
	//copy constructor
	public Human(Human human, Point pos){
		this(pos, human.getAnims().length);
		this.setAnims(human.getAnims().clone());
		this.setAnimTimes(human.getAnimTimes().clone());
		this.setFacingImgs(human.getFacingImgs().clone());
	}
	
	public void randMove(){
		if (randTimer < 25 + randAct && !collided){
			walk(randAct);
			randTimer++;
		}else {
			randTimer = 0;
			int randInt = rand.nextInt(100);
			if (randInt <= 60){
				randAct = 0;
			} else if (randInt >= 61 && randInt <= 70){
				if (randAct == 1 || randAct == 0){randAct = 1;} else {randAct = 0;}
			} else if (randInt >= 71 && randInt <= 80){
				if (randAct == 2 || randAct == 0){randAct = 2;} else {randAct = 0;}
			} else if (randInt >= 81 && randInt <= 90){
				if (randAct == 3 || randAct == 0){randAct = 3;} else {randAct = 0;}
			} else if (randInt >= 91 && randInt <= 100){
				if (randAct == 4 || randAct == 0){randAct = 4;} else {randAct = 0;}
			}
		}
		
	}
	
	public void walk(int direction){
		setActionID(direction);
		if (direction >= 1 && direction <= 4){
			setFacing(direction);
		}
	}
	
	public void update(long timePassed){
		super.update(timePassed);
		setAnimID(animKey[getActionID()]);
		setPrevPos(((Point) getPos().clone()));
		if (speed) {getPos().translate(walkKeyX[getActionID()]*tileMap.scale*10, walkKeyY[getActionID()]*tileMap.scale*10);}
		else {getPos().translate(walkKeyX[getActionID()]*tileMap.scale, walkKeyY[getActionID()]*tileMap.scale);}
		checkCollision();
		setActionID(0);		//temporary
	}
	
	public boolean checkCollision(){
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
