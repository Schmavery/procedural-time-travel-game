package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import core.ActionFactory.ActionType;
import core.display.AnimationManager;
import core.display.SpriteManager;
import core.display.SpriteSheet;
import entities.Markov;
import entities.concrete.Humanoid;
import entities.concrete.Humanoid.Gender;
import entities.concrete.NPC;
import entities.concrete.Sword;
import entities.interfaces.Drawable;
import gui.GBorderFactory;
import gui.GButton;
import gui.GClickEvent;
import gui.GFont;
import gui.GGridLayout;
import gui.GPanel;
import gui.GTextbox;
import gui.GUtil;
import gui.GUtil.Alignment;
import gui.GUtil.SpriteSheetType;
import gui.IContainer;

public class Game extends Core {
	// Singletons
	private static TileMap tileMap;
	
	public static int TILE_SIZE = 16;
	public static float SCALE = 2f;
	public static Random rand = new Random();
	
	long totalTime = 0;
	
	Humanoid player;
	List<Humanoid> humans;
	List<Drawable> drawList;
	Comparator<Drawable> drawComparator;
	String targetName;
	Markov maleNames;
	Markov femaleNames;
	
	boolean pauseDown = false;
	GPanel screen;
	GPanel panel;
	
	public static TileMap getMap(){
		return tileMap;
	}
	
	public static void main(String[] args){
		new Game();
	}
	
	Game(){
 		super();
		init();
	}
	
	@Override
	public void init() {
		super.init();
		initSpriteSheets();
		drawLoading();
		initMarkov();
		
		int numHumans = 10;
		
		drawList = new ArrayList<>(100);
		drawComparator = new Comparator<Drawable>()
		{
			@Override
			public int compare(Drawable d1, Drawable d2)
			{
				return (int) (d1.getY() - d2.getY());
			}
		};
		AnimationManager.loadAnims("res/animations.txt", SpriteSheetType.MAP);
		AnimationManager.loadAnims("res/peopleAnim.txt", SpriteSheetType.PEOPLE);
		AnimationManager.loadAnims("res/itemAnim.txt", SpriteSheetType.ITEMS);
		

		tileMap = new TileMap(50);
		Random rand = new Random();
		humans = new ArrayList<Humanoid>(numHumans);
		player = new Humanoid((tileMap.getSize()/2)*SCALE*TILE_SIZE, (tileMap.getSize()/2)*SCALE*TILE_SIZE, Gender.MALE, maleNames.genWordInRange(4, 10));
			player.setMovingAnims(AnimationManager.getAnim("man_n_walk"), 
					AnimationManager.getAnim("man_e_walk"),
					AnimationManager.getAnim("man_s_walk"),
					AnimationManager.getAnim("man_w_walk"));
			player.setStandingAnims(AnimationManager.getAnim("man_n"), 
					AnimationManager.getAnim("man_e"),
					AnimationManager.getAnim("man_s"),
					AnimationManager.getAnim("man_w"));
		humans.add(player);
		
		for (int i = 1; i < numHumans; i++){
			float randX;
			float randY;
			do{
				randX = rand.nextFloat()*SCALE*TILE_SIZE*(tileMap.getSize() - 1);
				randY = rand.nextFloat()*SCALE*TILE_SIZE*(tileMap.getSize() - 1);
			} while (!tileMap.getWorldTile(randX, randY).walkable);

			Humanoid tmpHuman;
			if (rand.nextBoolean()){
				tmpHuman = new NPC(randX, randY, Gender.MALE, maleNames.genWordInRange(4, 10));
				
				tmpHuman.setMovingAnims(AnimationManager.getAnim("man_n_walk"), 
						AnimationManager.getAnim("man_e_walk"),
						AnimationManager.getAnim("man_s_walk"),
						AnimationManager.getAnim("man_w_walk"));
				tmpHuman.setStandingAnims(AnimationManager.getAnim("man_n"), 
						AnimationManager.getAnim("man_e"),
						AnimationManager.getAnim("man_s"),
						AnimationManager.getAnim("man_w"));
			} else {
				tmpHuman = new NPC(randX, randY, Gender.FEMALE, femaleNames.genWordInRange(4, 10));
				
				tmpHuman.setMovingAnims(AnimationManager.getAnim("girl_n_walk"), 
						AnimationManager.getAnim("girl_e_walk"),
						AnimationManager.getAnim("girl_s_walk"),
						AnimationManager.getAnim("girl_w_walk"));
				tmpHuman.setStandingAnims(AnimationManager.getAnim("girl_n"), 
						AnimationManager.getAnim("girl_e"),
						AnimationManager.getAnim("girl_s"),
						AnimationManager.getAnim("girl_w"));
			}
			humans.add(tmpHuman);
		}
		
		Humanoid tmp = humans.get(rand.nextInt(numHumans));
		Tile tmpTile = tileMap.getWorldTile(tmp.getX(), tmp.getY());
		System.out.println(tmpTile.getX());
		System.out.println(tmpTile.getY());
		targetName = tmp.getName();
		player.getItem(new Sword(0, 0));
		player.doAction(ActionType.RETREIVE, 0);
		initGUI();
	}
	
	public void initMarkov(){
		maleNames = new Markov("res/mnames.txt", 2);
		femaleNames = new Markov("res/fnames.txt", 2);
	
	}
	
	public static void initSpriteSheets(){
		GFont font = new GFont("res/arial.fnt");
		GUtil.setFont(font);
		
		SpriteSheet testSS = new SpriteSheet(SpriteSheetType.PEOPLE, "res/people.png.dat");
		SpriteManager.get().loadSpriteSheet(testSS);
		System.out.println("Anchor: "+SpriteManager.get().getImage(SpriteSheetType.PEOPLE, 0).getAnchor().getX());
		
		try {
			Texture tileSheetTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/map.png")), GL11.GL_NEAREST);
			Texture peopleTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/people.png")), GL11.GL_NEAREST);
			Texture guiTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/gui.png")), GL11.GL_NEAREST);
			Texture itemTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/items.png")), GL11.GL_NEAREST);
			GUtil.setGuiTex(guiTex);
			GUtil.setPeopleTex(peopleTex);
			GUtil.setMapTex(tileSheetTex);
			GUtil.setItemTex(itemTex);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			exit();
		} catch (IOException e) {
			e.printStackTrace();
			exit();
		}
		
		
	}
	
	public void initGUI(){
		screen = new GPanel("screen", null, new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT));
		panel = new GPanel("panel", null, new Rectangle(10,555,355,230));
		panel.setBorder(GBorderFactory.createBasicBorder(new Color(95, 158, 160)));
		screen.addChild(panel);
		
		GTextbox title = new GTextbox("title", "<< Main Menu >>", 50, 15);
		GButton p1 = new GButton("Dr. Seuss", "Menu", "pause", 10, 45);
		p1.setBorder(GBorderFactory.createButtonBorder(new Color(50, 100, 50)));
		GButton p2 = new GButton("Eggs and Ham", "Pause", null, 10, 105);
		p2.setBorder(GBorderFactory.createButtonBorder(new Color(50, 50, 100)));
		GPanel p3 = new GPanel("p3", null, new Rectangle(130, 45, 210, 110));
		p3.setBorder(GBorderFactory.createBasicBorder(new Color(50, 50, 50)));
		GTextbox tb  = new GTextbox("tb", "test: ---  ", 10, 10);
		GTextbox tb2 = new GTextbox("tb2", "test: ---  ", 10, 30);
		GTextbox tb3 = new GTextbox("tb3", "test: ---  ", 10, 50);
		GTextbox tb4 = new GTextbox("tb4", "test: ---  ", 10, 70);
		tb.setTextColor(new Color(200, 200, 200));
		tb2.setTextColor(new Color(200, 200, 200));
		GButton p5 = new GButton("Sam I Am", "Extra Long Button!", null, 10, 165);
		p5.setBorder(GBorderFactory.createButtonBorder(new Color(100, 50, 50)));
		p3.addChild(tb);
		p3.addChild(tb2);
		p3.addChild(tb3);
		p3.addChild(tb4);
		panel.addChild(title);
		panel.addChild(p1);
		panel.addChild(p2);
		panel.addChild(p3);
		panel.addChild(p5);
		
		
		IContainer test = new GPanel("Test Panel", "none", new Rectangle (435, 655, 550, 130));
		screen.addChild(test);
		test.setLayout(new GGridLayout(2, 2, 10, 10));
		test.setBorder(GBorderFactory.createBasicBorder(new Color(100, 120, 100)));
		test.addChild(new GButton("b4", "Say \"Something\"", "say Something!", new Color(10,100,100)));
		test.addChild(new GButton("b4", "Say \"Hey\"", "say Hey!", new Color(10,100,100)));
		test.addChild(new GButton("b4", "Say \"Hello\"", "say Hello", new Color(100,100,10)));
		test.addChild(new GButton("b4", "Say \"Howdy\"", "say Howdy", new Color(100,10,100)));
		
		IContainer targetPanel = new GPanel("Target", "none", new Rectangle (675,25,300,100));
		targetPanel.setBorder(GBorderFactory.createBasicBorder(ReadableColor.DKGREY));
		targetPanel.setLayout(new GGridLayout(2, 1, 5, 0));
		tb = new GTextbox("message", "Go and find:");
		tb.setTextColor(ReadableColor.WHITE);
		targetPanel.addChild(tb);
		tb = new GTextbox("target", targetName);
		tb.setAlignment(Alignment.CENTER);
		tb.setTextColor(ReadableColor.RED);
		targetPanel.addChild(tb);
		screen.addChild(targetPanel);
		
	}
	
	@Override
	public void update(long deltaTime){
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				exit();
		}
		while (Mouse.next()){
			if (Mouse.getEventButton() == 0 && !Mouse.getEventButtonState()){
				GClickEvent tmp = screen.clickUp(Mouse.getEventX(), SCREEN_HEIGHT - Mouse.getEventY());
				if (tmp != null){
					if (tmp.getAction() != null && tmp.getAction().regionMatches(0, "say", 0, 3)){
						player.say(tmp.getAction().substring(4));
					}
				} else {
					int tileX = (int) (((Mouse.getEventX() + player.getX() - (SCREEN_WIDTH/2))/(SCALE*TILE_SIZE)));
					int tileY = (int) (((SCREEN_HEIGHT - Mouse.getEventY() + player.getY() - (SCREEN_HEIGHT/2))/(SCALE*TILE_SIZE)));
					System.out.println(tileX + ", " + tileY);
					player.walkTo(tileX, tileY);  
				}
			} else if (Mouse.getEventButtonState()){
				GClickEvent tmp = screen.clickDown(Mouse.getEventX(), SCREEN_HEIGHT - Mouse.getEventY());
				if (tmp != null){
					System.out.println(tmp.getSource().getName());
				}
			} else if (Mouse.getEventButton() == -1){
				GClickEvent tmp = screen.clickHold(Mouse.getEventX(), SCREEN_HEIGHT - Mouse.getEventY());
				if (tmp != null){
					System.out.println(tmp.getSource().getName());
				}
			}
		}
	}
	
	@Override
	public void gameUpdate(long deltaTime){

		AnimationManager.update(deltaTime);
		((GTextbox) (panel.search("tb"))).setText("X: "+ ((int) (player.getCenterX()/(SCALE*TILE_SIZE))));
		((GTextbox) (panel.search("tb2"))).setText("Y: "+ ((int) (player.getCenterY()/(SCALE*TILE_SIZE))));
		float speed = 100f;

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
			player.move(0f, -speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			player.move(0f, speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			player.move(speed, 0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			player.move(-speed, 0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_1)){
			player.doAction(ActionType.RETREIVE, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_2)){
			player.doAction(ActionType.RETREIVE, 1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_3)){
			player.doAction(ActionType.RETREIVE, 2);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)){
			player.doAction(ActionType.SWING);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			player.doAction(ActionType.USE);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)){
			player.doAction(ActionType.DROP);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_P)){
			pauseDown = true;
		} else if (pauseDown){
			pauseDown = false;
			pauseGame();
		}

		for (Humanoid human : humans){
			if (!human.equals(player)){
				if (!human.isMoving() && !human.isDead()){
					if (rand.nextInt(100) == 1){
						int destX = human.getTileX() + (rand.nextInt(10) - 5);
						int destY = human.getTileY() + (rand.nextInt(10) - 5);
						human.walkTo(destX, destY);
					}
					if (rand.nextInt(5000) == 1){
						human.say("Hey.");
					}
				}
			}
			human.update(deltaTime);
		}
		screen.update(deltaTime);
	}
	
	@Override
	public void pauseUpdate(long deltaTime){
		if (Keyboard.isKeyDown(Keyboard.KEY_P)){
			pauseDown = true;
		} else if (pauseDown){
			pauseDown = false;
			unpauseGame();
		}
		player.update(deltaTime);
	}
	
	@Override
	public void draw(){
		// Draw TileMap
		float tileSide = TILE_SIZE * SCALE;
		int playerTile_x = (int) Math.floor(player.getX() / (tileSide));
		int playerTile_y = (int) Math.floor(player.getY() / (tileSide));
		
		for (Tile tile : tileMap.getLocale((SCREEN_WIDTH/(int)tileSide)/2 + 1, playerTile_x, playerTile_y)){
			GUtil.drawSprite(SpriteSheetType.MAP, tile.getX() * tileSide - player.getX() + SCREEN_WIDTH/2f,
					tile.getY() * tileSide - player.getY() + SCREEN_HEIGHT/2f,
					tile.getTexX(), tile.getTexY(), tileSide, tileSide, 16);
			
		}
		
		drawList.clear();
		for (Tile tile : tileMap.getLocale((SCREEN_WIDTH/(int)tileSide)/2, playerTile_x, playerTile_y)){
			if (tile.getEntities() != null)
				drawList.addAll(tile.getEntities());
		}
		Collections.sort(drawList, drawComparator);
		for (Drawable d : drawList){
			d.draw(SCREEN_WIDTH/2f - player.getX(), SCREEN_HEIGHT/2f - player.getY());
		}

		screen.draw();
		player.drawStatus(10, 10);
		
	}

}
