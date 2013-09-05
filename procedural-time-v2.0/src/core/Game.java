package core;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Game extends Core {
	public static int TILE_SIZE = 16;
	public static float SCALE = 3f;
	
	public float player_x = 300;
	public float player_y = 300;

	private Texture tileSheetTex;
	private TileMap tileMap;
	
	public static void main(String[] args){
		new Game();
	}
	
	Game(){
		super();
		init();
	}
	
	public void init(){
		super.init();
		tileMap = new TileMap(20);
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
	
	public void update(long delta){
		
	}
	
	public void draw(){
		//glClear(GL_COLOR_BUFFER_BIT);
		
		glBindTexture(GL_TEXTURE_2D, tileSheetTex.getTextureID());
		for (Tile tile : tileMap.getSurroundingTiles(5,5,5)){
			glPushMatrix();
				glTranslatef(tile.getX()*TILE_SIZE*SCALE, tile.getY()*TILE_SIZE*SCALE, 0);
				glBegin(GL_QUADS);
				glTexCoord2f(0, 0);
				glVertex2f(0, 0);
				glTexCoord2f(1f/16, 0);
				glVertex2f(0, TILE_SIZE*SCALE);
				glTexCoord2f(1f/16, 1f/16);
				glVertex2f(TILE_SIZE*SCALE, TILE_SIZE*SCALE);
				glTexCoord2f(0, 1f/16);
				glVertex2f(TILE_SIZE*SCALE, 0);
			glEnd();
			glPopMatrix();
		}
	}
}
