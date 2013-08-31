package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import util.ImageTools;
import core.TileTypes.TileType;
import entities.CollisionCore;
import entities.Human;
import entities.Item;
import entities.ItemType;

public class Game extends Core{
	private int SCALE = 2;
	private int TILE_SIZE = 16*SCALE;
	private Point offset;

	private ImageTools imgTools = new ImageTools();
	
	private Image[][] imgArray;
	private BufferedImage[][] guiArray;
	//private Image[][] itemArray;
	private ItemType[] itemTypes;
	private LinkedList<Item> worldItems = new LinkedList<Item>();
	
	private TileMap tileMap;

	private Human player;
	private Human NPC[] = new Human[500];
	private Animation[] charAnims;
	private int incPause;
	private Random rand = new Random();
	private int mapSize = 1000;
	
	
	private GUI gui;
	
	private boolean infoShowing;
	
	public static void main(String argv[]) {
		new Game().run();
	}
	
	public void init(){
		super.init();
		
		offset = new Point(sm.getScreenSize().width/2,sm.getScreenSize().height/2);
		
		
		
		loadMap();
		CollisionCore.init(rand, tileMap);
		loadItems();
		
		
		//tileMap.serializeMap("map1", tileMap.getByteMap());
		
		//System.out.println(tileMap.deserializeMap("map1").equals(tileMap.getByteMap()));
		
		//tileMap.serializeMap("map2", tileMap.deserializeMap());


		player = new Human(new Point(rand.nextInt((mapSize - 10)*TILE_SIZE) + 30,rand.nextInt((mapSize - 10)*TILE_SIZE) + 30), 8, tileMap);

		charAnims = new Animation[10];
		charAnims[0] = new Animation();
		charAnims[0].addScene(imgArray[0][5], 200);
		charAnims[0].addScene(imgArray[1][5], 200);
		charAnims[0].addScene(imgArray[2][5], 200);
		charAnims[0].addScene(imgArray[3][5], 200);
		charAnims[1] = new Animation();
		charAnims[1].addScene(imgArray[4][5], 200);
		charAnims[1].addScene(imgArray[5][5], 200);
		charAnims[1].addScene(imgArray[6][5], 200);
		charAnims[1].addScene(imgArray[7][5], 200);
		charAnims[2] = new Animation();
		charAnims[2].addScene(imgArray[0][6], 200);
		charAnims[2].addScene(imgArray[1][6], 200);
		charAnims[2].addScene(imgArray[2][6], 200);
		charAnims[2].addScene(imgArray[3][6], 200);
		charAnims[3] = new Animation();
		charAnims[3].addScene(imgArray[4][6], 200);
		charAnims[3].addScene(imgArray[5][6], 200);
		charAnims[3].addScene(imgArray[6][6], 200);
		charAnims[3].addScene(imgArray[7][6], 200);
		

		charAnims[4] = new Animation();
		charAnims[4].addScene(imgArray[0][3], 200);
		charAnims[4].addScene(imgArray[1][3], 200);
		charAnims[4].addScene(imgArray[2][3], 200);
		charAnims[4].addScene(imgArray[3][3], 200);
		charAnims[5] = new Animation();
		charAnims[5].addScene(imgArray[4][3], 200);
		charAnims[5].addScene(imgArray[5][3], 200);
		charAnims[5].addScene(imgArray[6][3], 200);
		charAnims[5].addScene(imgArray[7][3], 200);
		charAnims[6] = new Animation();
		charAnims[6].addScene(imgArray[0][4], 200);
		charAnims[6].addScene(imgArray[1][4], 200);
		charAnims[7] = new Animation();
		charAnims[7].addScene(imgArray[2][4], 200);
		charAnims[7].addScene(imgArray[3][4], 200);
		
		player.addAnim(4, charAnims[4]);
		player.addAnim(0, charAnims[5]);
		player.addAnim(2, charAnims[6]);
		player.addAnim(6, charAnims[7]);

		
		player.setFacingImg(4, imgArray[0][3]);
		player.setFacingImg(0, imgArray[4][3]);
		player.setFacingImg(2, imgArray[0][4]);
		player.setFacingImg(6, imgArray[2][4]);
		
		for (int i = 0; i < NPC.length; i++){
			NPC[i] = new Human(player, new Point(rand.nextInt((mapSize - 10)*TILE_SIZE) + 30,rand.nextInt((mapSize - 10)*TILE_SIZE) + 30), tileMap);
			if (i%2 == 0){
				NPC[i].addAnim(4, charAnims[0]);
				NPC[i].addAnim(0, charAnims[1]);
				NPC[i].addAnim(2, charAnims[2]);
				NPC[i].addAnim(6, charAnims[3]);
				NPC[i].setFacingImg(4, imgArray[0][5]);
				NPC[i].setFacingImg(0, imgArray[4][5]);
				NPC[i].setFacingImg(2, imgArray[0][6]);
				NPC[i].setFacingImg(6, imgArray[4][6]);
			} else {
/*				NPC[i].addAnim(0, charAnims[4]);
				NPC[i].addAnim(2, charAnims[5]);
				NPC[i].addAnim(4, charAnims[6]);
				NPC[i].addAnim(6, charAnims[7]);
				NPC[i].setFacingImg(4, imgArray[0][3]);
				NPC[i].setFacingImg(0, imgArray[4][3]);
				NPC[i].setFacingImg(2, imgArray[0][4]);
				NPC[i].setFacingImg(6, imgArray[2][4]);*/
			}
		}
		loadGUI();
		for (int i = 0; i < 18; i++){
		player.addItem(new Item(itemTypes[rand.nextInt(itemTypes.length)], null));
		}
	}
	
	
	private void loadMap(){
		Image img = imgTools.loadImage("map.png");
		imgArray = imgTools.loadSprites(img, TILE_SIZE, 10, 10, SCALE);
		tileMap = new TileMap(imgArray, null, TILE_SIZE, SCALE, mapSize, mapSize);
	}
	
	private void loadGUI(){
		Image guiImg = imgTools.loadImage("gui.png");
		guiArray = imgTools.loadBuffSprites(guiImg, TILE_SIZE, SCALE);
		gui = new GUI(sm.getScreenSize(), TILE_SIZE, guiArray, player);
	}
	
	private void loadItems(){
		Image itemsImg = imgTools.loadImage("items.png");
		Image[][] itemArray = imgTools.loadBuffSprites(itemsImg, TILE_SIZE, SCALE);
		
		itemTypes = new ItemType[4];
		itemTypes[0] = new ItemType("rupee", itemArray[0][0]);
		itemTypes[1] = new ItemType("wood", itemArray[1][0]);
		//itemTypes[2] = new ItemType("heart", itemArray[2][0]);
		itemTypes[2] = new ItemType("apple", itemArray[3][0]);
		itemTypes[3] = new ItemType("orb", itemArray[4][0]);
		
		for (int i = 0; i < 1000; i++){
			//worldItems[i] = new Item(itemTypes[rand.nextInt(itemTypes.length)], new Point(rand.nextInt((mapSize - 10)*TILE_SIZE) + 30,rand.nextInt((mapSize - 10)*TILE_SIZE) + 30));
			worldItems.add(new Item(itemTypes[rand.nextInt(itemTypes.length)], new Point(rand.nextInt((mapSize - 10)*TILE_SIZE) + 30,rand.nextInt((mapSize - 10)*TILE_SIZE) + 30)));
		}
		
		
	}
	
	
	public void update(long delta){
		if(incPause > 0){incPause--;}
		
		if (newClick){
			lastClick.translate(-TILE_SIZE/2, -TILE_SIZE/2);
			lastClick.translate(player.getPos().x,player.getPos().y);
			lastClick.translate(-offset.x, -offset.y);
			
			for (int i = 0; i < NPC.length; i++){
				if (lastClick.distance(NPC[i].getPos()) < 30){
					gui.setPerson(NPC[i]);
				}
			}
			
			if (lastClick.distance(player.getPos()) < 100){
				gui.setPerson(player);
			}
			newClick = false;
		}
		
		
		gui.update(delta);
		
		player.update(delta);
		for (int i = 0; i < NPC.length; i++){
			NPC[i].update(delta);
			NPC[i].randMove();
		}
		for (int i = 0; i < charAnims.length; i++){
			if (charAnims[i] != null) {
				charAnims[i].updateScene(delta);
			}
		}

		tileMap.update(delta);
		
		

		for(Item item: worldItems){
			if (player.getPos().distance(item.getPos()) < 30 && !item.dead){
				if(player.addItem(item)){
					Point tempPt = (Point) player.getPos().clone();
					tempPt.translate((2*sm.getScreenSize().width)/3, (-sm.getScreenSize().height)/3);
					item.fling(tempPt);
				}
			}
		}
		
		for (int i = 0; i < NPC.length; i++){
			for(Item item: worldItems){
				if (NPC[i].getPos().distance(item.getPos()) < 30 && !item.dead){
					if(NPC[i].addItem(item))
						item.dead = true;
				}
			}
		}
		
		//remove old items and update
		Collection<Item> toRemove = new LinkedList<Item>();
		for(Item item: worldItems){
			if (item.dead){
				toRemove.add(item);
			} else {
				item.update(delta);
			}
		}
		for(Object item : toRemove)
		    worldItems.remove(item);
		
		
		if(keys_pressed[27] || keys_pressed[81]){
			stop();
		}
		
		
		// arrow keys
		if(keys_pressed[38] || keys_pressed[87]){
			//up
			if(keys_pressed[39] || keys_pressed[68]){
				player.walk(1); 									//up-right
			} else if (keys_pressed[37] || keys_pressed[65]){
				player.walk(7);										//up-left
			} else {
			player.walk(0);											//up
			}
			
		} else if (keys_pressed[40] || keys_pressed[83]){
			//down
			if(keys_pressed[39] || keys_pressed[68]){
				player.walk(3);										//down-right
			} else if (keys_pressed[37] || keys_pressed[65]){
				player.walk(5);										//down-left
			} else {
			player.walk(4);											//down
			}
			
		} else if (keys_pressed[37] || keys_pressed[65]){
			//left
			player.walk(6);											//left
			
		} else if (keys_pressed[39] || keys_pressed[68]){
			//right
			player.walk(2);											//right
			
		} else {
			player.stop();
		}
		
		
		if(keys_pressed[32]){
			//spacebar
			if (incPause==0){
			player.toggleSpeed();
			incPause = 10;
			}
		}
		if(keys_pressed[69]){
			//E
			if (incPause==0){
			tileMap.permuteTrees();
			incPause = 10;
			}
		}
		if(keys_pressed[73]){
			//I
			if (incPause==0){
				gui.toggleInventory();
				incPause = 10;
			}
		}
		if(keys_pressed[79]){
			//O
			if (incPause==0){
				infoShowing = !infoShowing;
				incPause = 10;
			}
		}
		
	}
	
	
	public void draw(Dimension dim, Graphics2D g, long delta){
		g.setColor(Color.black);
		g.fillRect(0,0, sm.getScreenSize().width, sm.getScreenSize().height);
		g.setColor(Color.white);
		
		//g.drawString("HELLO WORLD", (int) lastClick.getX(), (int) lastClick.getY());
		
		int centX = sm.getScreenSize().width/2;
		int centY = sm.getScreenSize().height/2;
		
		//////// Draw Map ///////////
		for (int i = 0; i < tileMap.mapWidth; i++){
			for (int j = 0; j < tileMap.mapHeight; j++){
				Tile tempTile = tileMap.getTile(i, j); 
				if (tempTile.getX() < player.getPos().x + SCALE*centX && tempTile.getX() > player.getPos().x - SCALE*centX && tempTile.getY() < player.getPos().y + SCALE*centY && tempTile.getY() > player.getPos().y - SCALE*centY )
				{g.drawImage(tileMap.getTileImage(i, j), tempTile.getX() - player.getPos().x + offset.x, tempTile.getY() - player.getPos().y + offset.y, null);}
			}
		}
		
		//////// Draw Map Items ////////
		for (Item item : worldItems){
			g.drawImage(item.getImg(), item.getPos().x - player.getPos().x + offset.x, item.getPos().y - player.getPos().y + offset.y, null);
		}
		
		//////// Draw NPCs //////////
		for (int i = 0; i < NPC.length; i++){
			if (NPC[i].getPos().x < player.getPos().x + SCALE*centX && NPC[i].getPos().x > player.getPos().x - SCALE*centX && NPC[i].getPos().y < player.getPos().y + SCALE*centY && NPC[i].getPos().y > player.getPos().y - SCALE*centY)
			{g.drawImage(NPC[i].getImage(), NPC[i].getPos().x - player.getPos().x + offset.x, NPC[i].getPos().y - player.getPos().y + offset.y, null);}
		}
		
		
		g.drawImage(player.getImage(), centX, centY, null);
		
		
		g.drawImage(gui.getGUI(), 0, 0, null);
		
		
		/////// Diagnostic info --press "o" in-game. //////////////
		if (infoShowing){
			for (int i = 0; i < player.getCollisionPts().length; i++){
				g.fillOval(offset.x + player.getCollisionPts()[i].x, offset.y + player.getCollisionPts()[i].y, 3, 3);
			}
			
			g.setColor(Color.gray);
			g.fillRect(5, 510, 200, 60);
			g.setColor(Color.white);
			g.drawString("Screen Position: (" + centX + ", " + centY + ")", 10, 520);
			g.drawString("World Position: (" + player.getPos().x + ", " + player.getPos().y + ")", 10, 530);
			g.drawString("Tile Position: (" + player.getPos().x/TILE_SIZE + ", " + player.getPos().y/TILE_SIZE + ")", 10, 540);
			TileType tempType = tileMap.getTileType(tileMap.getTileFromWorld(player.getPos().x, player.getPos().y));
			g.drawString("Tile Type: " + tempType.getTileName(), 10, 550);
			g.drawString("Is Walkable: " + tileMap.getWalkableFromWorld(player.getPos().x, player.getPos().y), 10, 560);
			long FPS;
			if (delta != 0) {FPS = 1000/delta;}
			else  {FPS = 1000;}
			g.drawString("FPS: " + FPS, 10, 570);
		}
		
	}


	
}
