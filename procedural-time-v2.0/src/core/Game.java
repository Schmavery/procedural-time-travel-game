package core;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import core.AnimationManager.SpriteSheet;
import entities.Human;

public class Game extends Core {
	public static int TILE_SIZE = 16;
	public static float SCALE = 3f;
	
	public float player_x;
	public float player_y;
	public Human player;

	private Texture tileSheetTex;
	private Texture peopleTex;
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

		tileMap = new TileMap(1000, animManager);
		player_x = 300f;
		player_y = 300f;
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			exit();
		} catch (IOException e) {
			e.printStackTrace();
			exit();
		}
	
	}
	
	public void update(long deltaTime){
		//System.out.println(deltaTime);
		animManager.update(deltaTime);
		float speed = (float) (0.5*deltaTime);
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)){
			player_y -= speed;
			player.move(0f, -speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)){
			player_y += speed;
			player.move(0f, speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){
			player_x += speed;
			player.move(speed, 0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)){
			player_x -= speed;
			player.move(-speed, 0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q) || 
				Keyboard.isKeyDown(Keyboard.KEY_Q)){
			exit();
		}
		player.update(deltaTime);
		
	}
	
	public void draw(){
		
		// Draw TileMap
		float tileSide = TILE_SIZE * SCALE;
		int playerTile_x = (int) Math.floor(player_x / (tileSide));
		int playerTile_y = (int) Math.floor(player_y / (tileSide));
		glBindTexture(GL_TEXTURE_2D, tileSheetTex.getTextureID());
		for (Tile tile : tileMap.getSurroundingTiles(11, playerTile_x, playerTile_y)){

			drawSprite(tile.getX() * tileSide - player_x + SCREEN_WIDTH/2f,
					tile.getY() * tileSide - player_y + SCREEN_HEIGHT/2f,
					tile.getTexX(), tile.getTexY(), tileSide);
			
		}
		
		glBindTexture(GL_TEXTURE_2D, peopleTex.getTextureID());
		//System.out.println(player.getTexX() + ", " + player.getTexY());
		drawSprite (SCREEN_WIDTH/2f, SCREEN_HEIGHT/2f, player.getTexX(), player.getTexY(), tileSide);
		
	}
	
	public void drawSprite(float x, float y, int texX, int texY, float spriteSide){
		glPushMatrix();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(texX/16f, texY/16f);				//short,short
		glVertex2f(0, 0);
		glTexCoord2f((texX+1)/16f, texY/16f);			//long, short
		glVertex2f(spriteSide, 0);
		glTexCoord2f((texX+1)/16f, (texY+1)/16f);		//long,  long
		glVertex2f(spriteSide, spriteSide);
		glTexCoord2f(texX/16f, (texY+1)/16f);			//short, long
		glVertex2f(0, spriteSide);
		glEnd();
	glPopMatrix();
	}
}
