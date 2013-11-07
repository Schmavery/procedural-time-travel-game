package core;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
import gui.GBorderFactory;
import gui.GButton;
import gui.GClickEvent;
import gui.GGridLayout;
import gui.GPanel;
import gui.GTextbox;
import gui.GUtil;
import gui.IContainer;

public class Game extends Core {
	public static int TILE_SIZE = 16;
	public static float SCALE = 3f;
	
	Human player;
	
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
		animManager = new AnimationManager();
		animManager.loadAnims("res/animations.txt", SpriteSheet.MAP);
		animManager.loadAnims("res/peopleAnim.txt", SpriteSheet.PEOPLE);
		
		initGUI();

		tileMap = new TileMap(1000, animManager);
		player = new Human(300f, 300f, tileMap);
		player.setMovingAnims(animManager.getAnim("main_n_anim"), 
				animManager.getAnim("main_e_anim"),
				animManager.getAnim("main_s_anim"),
				animManager.getAnim("main_w_anim"));
		player.setStandingAnims(animManager.getAnim("main_n"), 
				animManager.getAnim("main_e"),
				animManager.getAnim("main_s"),
				animManager.getAnim("main_w"));
		
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
	
	}
	
	public void initGUI(){
		screen = new GPanel("screen", null, new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT));
//		screen.hide();
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
		
		
		IContainer test = new GPanel("Test Panel", "none", new Rectangle (435, 555, 550, 230));
		test.setLayout(new GGridLayout(2, 2, 10, 10));
		screen.addChild(test);
		IContainer inner = new GPanel("Inner Panel");
		inner.setLayout(new GGridLayout(2, 2, 5, 5));
		test.addChild(inner);
		test.setBorder(GBorderFactory.createBasicBorder(new Color(100, 120, 100)));

//		inner.addChild(new GButton("b1", "123", null, new Color(100, 50, 100)));
//		inner.addChild(new GButton("b2", "abc", null, new Color(100, 50, 100)));
//		inner.addChild(new GButton("b3", "xyz", null, new Color(100, 50, 50)));
		test.addChild(new GButton("b4", "Say \"Hi\"", "say hi", new Color(10,100,100)));
		test.addChild(new GButton("b4", "Say \"Hello\"", "say hello", new Color(100,100,10)));
		test.addChild(new GButton("b4", "Say \"Howdy\"", "say howdy", new Color(100,10,100)));
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
//					System.out.println(tmp.getSource().getName());
					if (tmp.getAction().regionMatches(0, "say", 0, 3)){
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
		animManager.update(deltaTime);
		((GTextbox)((IContainer) (panel.getChild("p3"))).getChild("tb")).setText("X: "+String.valueOf((int) (player.getX()/(SCALE*TILE_SIZE))));
		((GTextbox)((IContainer) (panel.getChild("p3"))).getChild("tb2")).setText("Y: "+String.valueOf((int) (player.getY()/(SCALE*TILE_SIZE))));
		float speed = (float) (0.3*deltaTime);

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
		player.update(deltaTime);
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
		
		glColor3f(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// Draw TileMap
		float tileSide = TILE_SIZE * SCALE;
		int playerTile_x = (int) Math.floor(player.getX() / (tileSide));
		int playerTile_y = (int) Math.floor(player.getY() / (tileSide));
		glBindTexture(GL_TEXTURE_2D, tileSheetTex.getTextureID());
		for (Tile tile : tileMap.getSurroundingTiles((SCREEN_WIDTH/(int)tileSide)/2 + 1, 
				playerTile_x, playerTile_y)){

			GUtil.drawSprite(tile.getX() * tileSide - player.getX() + SCREEN_WIDTH/2f,
					tile.getY() * tileSide - player.getY() + SCREEN_HEIGHT/2f,
					tile.getTexX(), tile.getTexY(), tileSide, tileSide, 16);
			
		}
		
		glBindTexture(GL_TEXTURE_2D, peopleTex.getTextureID());
		GUtil.drawSprite (SCREEN_WIDTH/2f, SCREEN_HEIGHT/2f, player.getTexX(), player.getTexY(), tileSide, tileSide, 16);
		
		glBindTexture(GL_TEXTURE_2D, guiTex.getTextureID());
		
		for (Message m : Message.getMessages()){
			Rectangle rect = new Rectangle(
					(int) (m.getSender().getX() - player.getX() + (SCREEN_WIDTH/2) - (m.getText().length()-1)*8), 
					(int) (m.getSender().getY() - player.getY() + (SCREEN_HEIGHT/2) - 60),
					(m.getText().length()+2)*16, 49);
			glColor3f(200/255f, 200/255f, 175/255f);
			GUtil.drawRect(rect);
			GUtil.drawText(rect.getX()+16, rect.getY()+16, ReadableColor.BLACK, m.getText());
		}
		
		screen.draw();
		
	}

}
