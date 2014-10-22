package gui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

public final class GUtil {
	public static enum SpriteSheet {
		MAP, ITEMS, PEOPLE, GUI, FONT;
	}
	
	public static GFont fnt;
	private static Texture guiTex, mapTex, peopleTex, itemTex;
	private static SpriteBatch batch = new SpriteBatch();
	
	private GUtil(){}
	
	public static enum Alignment {LEFT, CENTER, RIGHT, TOP, BOTTOM};
	
	public static void drawSprite(SpriteSheet spr, float x, float y, int texX, int texY, 
			float spriteW, float spriteH, float ssSize){
		drawSprite(spr, x, y, texX, texY, spriteW, spriteH, ssSize, ReadableColor.WHITE);
	}
	
	public static void drawSprite(SpriteSheet spr, float x, float y, int texX, int texY, 
			float spriteW, float spriteH, float ssSize, ReadableColor c){

		float[] arr = {texX/ssSize, texY/ssSize, (texX+1)/ssSize, texY/ssSize, 
				(texX+1)/ssSize, (texY+1)/ssSize, texX/ssSize, (texY+1)/ssSize};
		batch.draw(getTexID(spr), arr, x, y, spriteW, spriteH, c);
	}
	
	public static void drawSprite(SpriteSheet spr, float x, float y, float spriteW, float spriteH, 
									float texX, float texY, float texW, float texH, ReadableColor c){
		int size = getTex(spr).getImageHeight();
		float[] arr = {texX/size, texY/size, (texX+texW)/size, texY/size, 
				(texX+texW)/size, (texY+texH)/size, texX/size, (texY+texH)/size};
		batch.draw(getTexID(spr), arr, x, y, spriteW, spriteH, c);
	}

	public static void drawRect(Rectangle box, ReadableColor c){
		int x = box.getX();
		int y = box.getY();
		int innerW = box.getWidth() - 32;
		int innerH = box.getHeight() - 32;
		drawSprite(SpriteSheet.GUI, x,      y, 0, 4, 16, 16, 32, c);					// Top Left
		drawSprite(SpriteSheet.GUI, x+16,     y, 1, 4, innerW, 16, 32, c);				// Top Mid
		drawSprite(SpriteSheet.GUI, x+innerW + 16, y, 2, 4, 16, 16, 32, c);				// Top Right
		drawSprite(SpriteSheet.GUI, x,      y+16, 0, 5, 16, innerH, 32, c);					// Mid Left
		drawSprite(SpriteSheet.GUI, x+16, y+16, 1, 5, innerW, innerH, 32, c);				// Mid Mid
		drawSprite(SpriteSheet.GUI, x+innerW + 16, y+16, 2, 5, 16, innerH, 32, c);			// Mid Right
		drawSprite(SpriteSheet.GUI, x,           y+innerH + 16, 0, 6, 16, 16, 32, c);		// Bottom Left
		drawSprite(SpriteSheet.GUI, x+16,          y+innerH + 16, 1, 6, innerW, 16, 32, c);	// Bottom Mid
		drawSprite(SpriteSheet.GUI, x+ innerW + 16, y+innerH + 16, 2, 6, 16, 16, 32, c);		// Bottom Right
	}

	public static void drawText(int x, int y, ReadableColor c, String text){
		drawText(x, y, c, text, fnt);
	}

	public static void drawText(int x, int y, ReadableColor c, String text, GFont font){
		font.drawText(text, x, y, c);
	}
	
	public static int textLength(String text){
		return fnt.stringLength(text);
	}
	
	public static void drawBubble(Rectangle box, ReadableColor c){
		drawRect(box, c);
		drawSprite(SpriteSheet.GUI, box.getX() + (box.getWidth()/2 - 16), box.getY()+box.getHeight()-16, 3, 4, 16, 16, 32, c);
		drawSprite(SpriteSheet.GUI, box.getX() + (box.getWidth()/2), box.getY()+box.getHeight()-16,      4, 4, 16, 16, 32, c);
		drawSprite(SpriteSheet.GUI, box.getX() + (box.getWidth()/2 - 16), box.getY()+box.getHeight(),    3, 5, 16, 16, 32, c);
		drawSprite(SpriteSheet.GUI, box.getX() + (box.getWidth()/2), box.getY()+box.getHeight(),         4, 5, 16, 16, 32, c);
	}
	
	public static void setFont(GFont font){
		fnt = font;
	}
	public static void setGuiTex(Texture tex){
		guiTex = tex;
	}
	public static void setPeopleTex(Texture tex){
		peopleTex = tex;
	}
	public static void setMapTex(Texture tex){
		mapTex = tex;
	}
	public static void setItemTex(Texture tex){
		itemTex = tex;
	}
	
	public static void begin(){
		SpriteBatch.begin();
	}
	
	public static void end(){
		batch.end();
	}
	
	private static int getTexID(SpriteSheet spr){
		return getTex(spr).getTextureID();
	}
	
	private static Texture getTex(SpriteSheet spr){
		switch (spr){
		case PEOPLE:
			return peopleTex;
		case MAP:
			return mapTex;
		case FONT:
			return fnt.getTex();
		case GUI:
			return guiTex;
		case ITEMS:
			return itemTex;
		default: 
			return null;
		}
	}
	
	static String readFile(String path, Charset encoding)
	{
		try {
			return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
		} catch (IOException e) {
			System.out.println("Error: Could not read file.");
			return "";
		}
	}

}
