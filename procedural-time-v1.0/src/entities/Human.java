package entities;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import core.Animation;
import core.TileMap;

public class Human{
		
		private int id;
		private static int nextID = 0;
	
		//private int[] animKey  = {1,2,3,1,4,2,1,1,2};
		//private TileMap tileMap;
		
		public int hitPts;
		public String name;
		
		private Random rand = new Random();
		
		MoveCore moveCore;
		ImageCore imageCore;
		Inventory inventory = new Inventory(45);
		
		public Human(Point pos, int numOfAnims){
			this(pos, numOfAnims, null);
		}
		
		public Human(Point pos, int numOfAnims, TileMap tileMap){
			imageCore = new ImageCore(numOfAnims);
			
			this.setId(Human.nextID);
			Human.nextID++;
			
			int offset = 10;
			int xShift = -2;
			int yShift = 7;
			int tileSize = tileMap.tileSize - offset;
			Point[] collisionPts = new Point[4];
			collisionPts[0] = new Point(offset + xShift,offset + yShift);
			collisionPts[1] = new Point(offset + xShift,tileSize + yShift);
			collisionPts[2] = new Point(tileSize + xShift,offset + yShift);
			collisionPts[3] = new Point(tileSize + xShift,tileSize + yShift);
			
			
			
			moveCore = new MoveCore(pos, pos, tileMap, collisionPts);
			
			hitPts = rand.nextInt(5)+1;
			this.name = NameGen.gen();			
		}
		
		//copy constructor
		public Human(Human human, Point pos, TileMap tileMap){
			this(pos, human.imageCore.numAnims(), tileMap);
			this.imageCore = human.imageCore.clone();
			this.name = NameGen.gen();
		}
		
		public int getId() {return id;}
		public void setId(int id) {this.id = id;}		
		
		public void update(long delta){
			moveCore.update(delta);
		}
		
		public Rectangle getRect(){
			Dimension dim = new Dimension(getCollisionPts()[4].x - getCollisionPts()[0].x, getCollisionPts()[4].y - getCollisionPts()[0].y);
			Point tempPos = ((Point) getPos().clone());
			tempPos.translate(getCollisionPts()[0].x, getCollisionPts()[0].y);
			return new Rectangle(tempPos, dim);
		}
		
		
		//////////////////// Movement Methods ////////////////////
		public void walk(int direction){
			moveCore.walk(direction);
		}
		public void randMove(){
			moveCore.randMove();
		}
		public void toggleSpeed(){
			moveCore.fast = !moveCore.fast;
		}
		public void stop(){
			moveCore.stop();
		}
		public Point getPos(){
			return moveCore.pos;	
		}
		public Point[] getCollisionPts(){
			return moveCore.collisionPts;
		}
		//////////////////// Image Methods ////////////////////
		public Image getImage(){
			return imageCore.getImage(moveCore.facing, moveCore.walking);
		}
		public void addAnim(int index, Animation anim){
			imageCore.addAnim(index, anim);
		}
		public void setFacingImg(int index, Image img){
			imageCore.setFacingImg(index, img);
		}

		//////////////////// Inventory Methods /////////////////
		public boolean addItem(Item item){
			return inventory.addItem(item);
		}
		public Item[] getInvArray(){
			return inventory.itemArray;
		}
		public int getInvSize(){
			return inventory.size;
		}

}
