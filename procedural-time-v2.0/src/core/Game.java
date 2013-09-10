package core;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.PlatformManagedObject;

import javax.swing.text.JTextComponent.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Game extends Core {
	public static int TILE_SIZE = 16;
	public static float SCALE = 3f;
	
	public float player_x;
	public float player_y;

	private Texture tileSheetTex;
	private TileMap tileMap;
	
	public static void main(String[] args){
		new Game();
	}
	
	Game(){
 		super();
		init();
	}
	
	public void init() {
		super.init();
		tileMap = new TileMap(1000);
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
		System.out.println(deltaTime);
		float speed = (float) (0.2*deltaTime);
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
			player_y -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			player_y += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			player_x += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			player_x -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q) || 
				Keyboard.isKeyDown(Keyboard.KEY_Q)){
			exit();
		}
		
	}
	
	public void draw(){
		//glClear(GL_COLOR_BUFFER_BIT);
//		int tileID = 73;
//		int tileX = tileID % 16;
//		int tileY
		
		
		float tileSide = TILE_SIZE * SCALE;
		int playerTile_x = (int) Math.floor(player_x / (tileSide));
		int playerTile_y = (int) Math.floor(player_y / (tileSide));
		glBindTexture(GL_TEXTURE_2D, tileSheetTex.getTextureID());
		for (Tile tile : tileMap.getSurroundingTiles(11, playerTile_x, playerTile_y)){

			int tileX = tile.getTexX();
			int tileY = tile.getTexY();
			System.out.println(tileX);
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
