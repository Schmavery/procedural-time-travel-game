package core;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import core.AnimationManager.SpriteSheet;
import entities.Human;
import entities.Human.Gender;
import gui.GBorderFactory;
import gui.GButton;
import gui.GClickEvent;
import gui.GGridLayout;
import gui.GPanel;
import gui.GTextbox;
import gui.GUtil;
import gui.GUtil.Alignment;
import gui.IContainer;

public class Game extends Core {
	public static enum Time {NIGHT, DAY, EVENING, MORNING};
	public static int TILE_SIZE = 16;
	public static float SCALE = 3f;
	
	long totalTime = 0;
	Time time = Time.DAY;
	
	Human player;
	Human[] humans;
	String targetName;
	Markov maleNames;
	Markov femaleNames;
	
	boolean pauseDown = false;
	GPanel screen;
	GPanel panel;

	private Texture tileSheetTex;
	private Texture peopleTex;
	private Texture guiTex;
	
	private TileMap tileMap;
	private AnimationManager animManager;
	
	public static void main(String[] args){
		new Game();
	}
	
	Game(){
 		super();
		init();
	}
	
	public void init() {
		super.init();
		markovInit();
		animManager = new AnimationManager();
		animManager.loadAnims("res/animations.txt", SpriteSheet.MAP);
		animManager.loadAnims("res/peopleAnim.txt", SpriteSheet.PEOPLE);
		

		tileMap = new TileMap(1000, animManager);
		Random rand = new Random();
		humans = new Human[10000];
		player = new Human(300f, 300f, Gender.MALE, maleNames.genWordInRange(4, 10), tileMap);
			player.setMovingAnims(animManager.getAnim("man_n_anim"), 
					animManager.getAnim("man_e_anim"),
					animManager.getAnim("man_s_anim"),
					animManager.getAnim("man_w_anim"));
			player.setStandingAnims(animManager.getAnim("man_n"), 
					animManager.getAnim("man_e"),
					animManager.getAnim("man_s"),
					animManager.getAnim("man_w"));
		humans[0] = player;
		
		for (int i = 1; i < humans.length; i++){
			float randX = rand.nextFloat()*SCALE*TILE_SIZE*999;
			float randY = rand.nextFloat()*SCALE*TILE_SIZE*999;
			if (!tileMap.getWorldTile(randX, randY).walkable){
				i--;
				continue;
			}
			
			if (rand.nextBoolean()){
				humans[i] = new Human(randX, randY, Gender.MALE, maleNames.genWordInRange(4, 10), tileMap);
				
				humans[i].setMovingAnims(animManager.getAnim("man_n_anim"), 
						animManager.getAnim("man_e_anim"),
						animManager.getAnim("man_s_anim"),
						animManager.getAnim("man_w_anim"));
				humans[i].setStandingAnims(animManager.getAnim("man_n"), 
						animManager.getAnim("man_e"),
						animManager.getAnim("man_s"),
						animManager.getAnim("man_w"));
			} else {
				humans[i] = new Human(randX, randY, Gender.FEMALE, femaleNames.genWordInRange(4, 10), tileMap);
				
				humans[i].setMovingAnims(animManager.getAnim("girl_n_anim"), 
						animManager.getAnim("girl_e_anim"),
						animManager.getAnim("girl_s_anim"),
						animManager.getAnim("girl_w_anim"));
				humans[i].setStandingAnims(animManager.getAnim("girl_n"), 
						animManager.getAnim("girl_e"),
						animManager.getAnim("girl_s"),
						animManager.getAnim("girl_w"));
			}
		}
		
		Human tmp = humans[rand.nextInt(humans.length)];
		Tile tmpTile = tileMap.getWorldTile(tmp.getX(), tmp.getY());
		System.out.println(tmpTile.getX());
		System.out.println(tmpTile.getY());
		targetName = tmp.getName();
		
		try {
			tileSheetTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/map.png")), GL11.GL_NEAREST);
			peopleTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/people.png")), GL11.GL_NEAREST);
			guiTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/gui.png")), GL11.GL_NEAREST);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			exit();
		} catch (IOException e) {
			e.printStackTrace();
			exit();
		}
	
		initGUI();
	}
	
	public void markovInit(){
		maleNames = new Markov("res/mnames.txt", 2);
		femaleNames = new Markov("res/fnames.txt", 2);
//		for (int i = 0; i < 10; i++){
//			System.out.println(maleNames.genWordInRange(4, 10));
//		}
	
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
		GButton p5 = new GButton("Sam I Am", "extra long button!", null, 10, 165);
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
		test.addChild(new GButton("b4", "Say \"Something\"", "say something", new Color(10,100,100)));
		test.addChild(new GButton("b4", "Say \"Hey\"", "say hey", new Color(10,100,100)));
		test.addChild(new GButton("b4", "Say \"Hello\"", "say hello", new Color(100,100,10)));
		test.addChild(new GButton("b4", "Say \"Howdy\"", "say howdy", new Color(100,10,100)));
		
		IContainer targetPanel = new GPanel("Target", "none", new Rectangle (675,25,300,100));
		targetPanel.setBorder(GBorderFactory.createBasicBorder(Color.DKGREY));
		targetPanel.setLayout(new GGridLayout(2, 1, 5, 0));
		tb = new GTextbox("message", "Go and find:");
		tb.setTextColor(Color.WHITE);
		targetPanel.addChild(tb);
		tb = new GTextbox("target", targetName);
		tb.setAlignment(Alignment.CENTER);
		tb.setTextColor(Color.RED);
		targetPanel.addChild(tb);
		screen.addChild(targetPanel);
		
	}
	
	public void update(long deltaTime){
		if (Keyboard.isKeyDown(Keyboard.KEY_Q) || 
				Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				exit();
		}
		while (Mouse.next()){
			if (Mouse.getEventButton() == 0 && !Mouse.getEventButtonState()){
				GClickEvent tmp = screen.clickUp(Mouse.getEventX(), SCREEN_HEIGHT - Mouse.getEventY());
				if (tmp != null){
					if (tmp.getAction() != null && tmp.getAction().regionMatches(0, "say", 0, 3)){
						Message.say(player.getX(), player.getY(), tmp.getAction().substring(4), player);
					}
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
	
	public void gameUpdate(long deltaTime){
		totalTime += deltaTime;
		if (totalTime % 100000 < 10000){
			time = Time.MORNING;
		} else if (totalTime % 100000 < 50000){
			time = Time.DAY;
		} else if (totalTime % 100000 < 60000){
			time = Time.EVENING;
		} else {
			time = Time.NIGHT;
		}
		
		animManager.update(deltaTime);
		((GTextbox)((IContainer) (panel.getChild("p3"))).getChild("tb")).setText("X: "+String.valueOf((int) (player.getX()/(SCALE*TILE_SIZE))));
		((GTextbox)((IContainer) (panel.getChild("p3"))).getChild("tb2")).setText("Y: "+String.valueOf((int) (player.getY()/(SCALE*TILE_SIZE))));
		float speed = 100f;

		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)){
			player.move(0f, -speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)){
			player.move(0f, speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){
			player.move(speed, 0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)){
			player.move(-speed, 0f);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_P)){
			pauseDown = true;
		} else if (pauseDown){
			pauseDown = false;
			pauseGame();
		}
//		player.update(deltaTime);
		for (int i = 0; i < humans.length; i++){
			humans[i].update(deltaTime);
		}
		screen.update(deltaTime);
		Message.update();
		
	}
	
	public void pauseUpdate(long deltaTime){
		if (Keyboard.isKeyDown(Keyboard.KEY_P)){
			pauseDown = true;
		} else if (pauseDown){
			pauseDown = false;
			unpauseGame();
		}
		player.update(deltaTime);
	}
	
	
	public void draw(){
		float shade;
		switch (time){
		case DAY:
			shade = 1f;
			break;
		case NIGHT:
			shade = .2f;
			break;
		case EVENING:
		case MORNING:
			shade = .7f;
			break;
		default:
			shade = 1f;
		}
		
		glColor3f(shade, shade, shade);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// Draw TileMap
		float tileSide = TILE_SIZE * SCALE;
		int playerTile_x = (int) Math.floor(player.getX() / (tileSide));
		int playerTile_y = (int) Math.floor(player.getY() / (tileSide));
		glBindTexture(GL_TEXTURE_2D, tileSheetTex.getTextureID());
		
//		Tile[] closeTiles = tileMap.getSurroundingTiles((SCREEN_WIDTH/(int)tileSide)/2 + 1, 
//				playerTile_x, playerTile_y);
		
		for (Tile tile : tileMap.getLocale((SCREEN_WIDTH/(int)tileSide)/2, playerTile_x, playerTile_y)){
			GUtil.drawSprite(tile.getX() * tileSide - player.getX() + SCREEN_WIDTH/2f,
					tile.getY() * tileSide - player.getY() + SCREEN_HEIGHT/2f,
					tile.getTexX(), tile.getTexY(), tileSide, tileSide, 16);
			
		}
		
		glBindTexture(GL_TEXTURE_2D, peopleTex.getTextureID());
		
		for (Tile tile : tileMap.getLocale((SCREEN_WIDTH/(int)tileSide)/2, playerTile_x, playerTile_y)){
			for (Human h : tile.entities){
				GUtil.drawSprite (h.getX() - player.getX() + SCREEN_WIDTH/2f,
						h.getY() - player.getY() + SCREEN_HEIGHT/2f,
						h.getTexX(), h.getTexY(), tileSide, tileSide, 16);
			}
		}
		
		glBindTexture(GL_TEXTURE_2D, guiTex.getTextureID());
		
		for (Message m : Message.getOldMessages()){
			Rectangle rect = new Rectangle(
					(int) (m.getSender().getX() - player.getX() + (SCREEN_WIDTH/2) - (m.getText().length()-1)*8), 
					(int) (m.getSender().getY() - player.getY() + (SCREEN_HEIGHT/2) - 60),
					(m.getText().length()+2)*16, 49);
			glColor3f(200/255f, 200/255f, 175/255f);
			GUtil.drawBubble(rect);
			GUtil.drawText(rect.getX()+16, rect.getY()+16, ReadableColor.BLACK, m.getText());
		}
		
		for (Message m : Message.getMessages()){
			if (m.getText().indexOf(targetName) != -1){
				((GTextbox)((IContainer) (screen.getChild("Target"))).getChild("target")).setText("A LIFE");
				((GTextbox)((IContainer) (screen.getChild("Target"))).getChild("target")).setTextColor(Color.GREEN);
			}
			Rectangle rect = new Rectangle(
					(int) (m.getSender().getX() - player.getX() + (SCREEN_WIDTH/2) - (m.getText().length()-1)*8), 
					(int) (m.getSender().getY() - player.getY() + (SCREEN_HEIGHT/2) - 60),
					(m.getText().length()+2)*16, 49);
			glColor3f(200/255f, 200/255f, 175/255f);
			GUtil.drawBubble(rect);
			GUtil.drawText(rect.getX()+16, rect.getY()+16, ReadableColor.BLACK, m.getText());
		}
		
		screen.draw();
		
	}

}
