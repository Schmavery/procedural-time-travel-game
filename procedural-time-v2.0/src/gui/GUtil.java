package gui;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import core.SpriteBatch;

public final class GUtil {

	public static GFont fnt;
	private static Texture guiTex;
	private static SpriteBatch batch = new SpriteBatch();
	
	private GUtil(){}
	
	public static enum Alignment {LEFT, CENTER, RIGHT, TOP, BOTTOM};
	
	public static void drawSprite(int texID, float x, float y, int texX, int texY, 
			float spriteW, float spriteH, float ssSize){
		drawSprite(texID, x, y, texX, texY, spriteW, spriteH, ssSize, Color.WHITE);
	}
	
	public static void drawSprite(int texID, float x, float y, int texX, int texY, 
			float spriteW, float spriteH, float ssSize, ReadableColor c){

		float[] arr = {texX/ssSize, texY/ssSize, (texX+1)/ssSize, texY/ssSize, 
				(texX+1)/ssSize, (texY+1)/ssSize, texX/ssSize, (texY+1)/ssSize};
		batch.draw(texID, arr, x, y, spriteW, spriteH, c);
	}
	
	public static void drawSprite(int texID, float x, float y, float spriteW, float spriteH, 
									float texX, float texY, float texW, float texH, float size, ReadableColor c){
//		glPushMatrix();
//			glTranslatef(x, y, 0);
//			glBegin(GL_QUADS);
//			glTexCoord2f(texX/size, texY/size);					//short,short
//			glVertex2f(0, 0);
//			glTexCoord2f((texX+texW)/size, texY/size);			//long, short
//			glVertex2f(spriteW, 0);
//			glTexCoord2f((texX+texW)/size, (texY+texH)/size);	//long,  long
//			glVertex2f(spriteW, spriteH);
//			glTexCoord2f(texX/size, (texY+texH)/size);			//short, long
//			glVertex2f(0, spriteH);
//			glEnd();
//		glPopMatrix();
		float[] arr = {texX/size, texY/size, (texX+texW)/size, texY/size, 
				(texX+texW)/size, (texY+texH)/size, texX/size, (texY+texH)/size};
		batch.draw(texID, arr, x, y, spriteW, spriteH, c);
	}

	public static void drawRect(Rectangle box, ReadableColor c){
		int x = box.getX();
		int y = box.getY();
		int innerW = box.getWidth() - 32;
		int innerH = box.getHeight() - 32;
		drawSprite(guiTex.getTextureID(), x,      y, 0, 4, 16, 16, 32, c);					// Top Left
		drawSprite(guiTex.getTextureID(), x+16,     y, 1, 4, innerW, 16, 32, c);				// Top Mid
		drawSprite(guiTex.getTextureID(), x+innerW + 16, y, 2, 4, 16, 16, 32, c);				// Top Right
		drawSprite(guiTex.getTextureID(), x,      y+16, 0, 5, 16, innerH, 32, c);					// Mid Left
		drawSprite(guiTex.getTextureID(), x+16, y+16, 1, 5, innerW, innerH, 32, c);				// Mid Mid
		drawSprite(guiTex.getTextureID(), x+innerW + 16, y+16, 2, 5, 16, innerH, 32, c);			// Mid Right
		drawSprite(guiTex.getTextureID(), x,           y+innerH + 16, 0, 6, 16, 16, 32, c);		// Bottom Left
		drawSprite(guiTex.getTextureID(), x+16,          y+innerH + 16, 1, 6, innerW, 16, 32, c);	// Bottom Mid
		drawSprite(guiTex.getTextureID(), x+ innerW + 16, y+innerH + 16, 2, 6, 16, 16, 32, c);		// Bottom Right
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
		drawSprite(guiTex.getTextureID(), box.getX() + (box.getWidth()/2 - 16), box.getY()+box.getHeight()-16, 3, 4, 16, 16, 32, c);
		drawSprite(guiTex.getTextureID(), box.getX() + (box.getWidth()/2), box.getY()+box.getHeight()-16,      4, 4, 16, 16, 32, c);
		drawSprite(guiTex.getTextureID(), box.getX() + (box.getWidth()/2 - 16), box.getY()+box.getHeight(),    3, 5, 16, 16, 32, c);
		drawSprite(guiTex.getTextureID(), box.getX() + (box.getWidth()/2), box.getY()+box.getHeight(),         4, 5, 16, 16, 32, c);
	}
	
	public static void setFont(GFont font){
		fnt = font;
	}
	public static void setGuiTex(Texture tex){
		guiTex = tex;
	}
	
	public static void begin(){
		batch.begin();
	}
	
	public static void end(){
		batch.end();
	}
	

}
