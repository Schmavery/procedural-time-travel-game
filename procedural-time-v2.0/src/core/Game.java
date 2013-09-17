package core;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import core.AnimationManager.SpriteSheet;

public class Game extends Core {
	public static int TILE_SIZE = 16;
	public static float SCALE = 2f;
	
	public float player_x;
	public float player_y;

	private Texture tileSheetTex;
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
		tileMap = new TileMap(1000, animManager);
		player_x = 300f;
		player_y = 300f;
		try {
			tileSheetTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/map.png")), GL11.GL_NEAREST);
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
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)){
			player_y += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){
			player_x += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)){
			player_x -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q) || 
				Keyboard.isKeyDown(Keyboard.KEY_Q)){
			exit();
		}
		
	}
	
	public void draw(){
		
		// Draw TileMap
		float tileSide = TILE_SIZE * SCALE;
		int playerTile_x = (int) Math.floor(player_x / (tileSide));
		int playerTile_y = (int) Math.floor(player_y / (tileSide));
		glBindTexture(GL_TEXTURE_2D, tileSheetTex.getTextureID());
		for (Tile tile : tileMap.getSurroundingTiles(16, playerTile_x, playerTile_y)){

			int tileX = tile.getTexX();
			int tileY = tile.getTexY();
			//System.out.println(tile.getTexID());
			//if (tile.getTexID() == 66){
			//	tileX = animManager.getAnim("flower").getDispX();
			//	tileY = animManager.getAnim("flower").getDispY();
			//	
		//	}
			glPushMatrix();
				glTranslatef(tile.getX() * tileSide - player_x + SCREEN_WIDTH/2f,
						tile.getY() * tileSide - player_y + SCREEN_HEIGHT/2f, 0);
				glBegin(GL_QUADS);
				glTexCoord2f(tileX/16f, tileY/16f);				//short,short
				glVertex2f(0, 0);
				glTexCoord2f((tileX+1)/16f, tileY/16f);			//long, short
				glVertex2f(tileSide, 0);
				glTexCoord2f((tileX+1)/16f, (tileY+1)/16f);		//long,  long
				glVertex2f(tileSide, tileSide);
				glTexCoord2f(tileX/16f, (tileY+1)/16f);			//short, long
				glVertex2f(0, tileSide);
				glEnd();
			glPopMatrix();
		}
		
		//glBindTexture(GL_TEXTURE_2D, peopleTex);
		glPushMatrix();
		glTranslatef(SCREEN_WIDTH/2f, SCREEN_HEIGHT/2f, 0);
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(2f/16, 0);
			glVertex2f(tileSide, 0);
			glTexCoord2f(2f/16, 2f/16);
			glVertex2f(tileSide, tileSide);
			glTexCoord2f(0, 2f/16);
			glVertex2f(0, tileSide);
		glEnd();
		glPopMatrix();
		
	}
}
