package gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;

public final class GUtil {

	private GUtil(){}
	
	public static enum Alignment {LEFT, CENTER, RIGHT, TOP, BOTTOM};
	
	public static void drawSprite(float x, float y, int texX, int texY, float spriteW, float spriteH, float ssSize){
		glPushMatrix();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
			glTexCoord2f(texX/ssSize, texY/ssSize);				//short,short
			glVertex2f(0, 0);
			glTexCoord2f((texX+1)/ssSize, texY/ssSize);			//long, short
			glVertex2f(spriteW, 0);
			glTexCoord2f((texX+1)/ssSize, (texY+1)/ssSize);		//long,  long
			glVertex2f(spriteW, spriteH);
			glTexCoord2f(texX/ssSize, (texY+1)/ssSize);			//short, long
			glVertex2f(0, spriteH);
			glEnd();
		glPopMatrix();
	}

	public static void drawRect(Rectangle box){
		glPushMatrix();
			glTranslatef(box.getX(), box.getY(), 0);
			int innerW = box.getWidth() - 32;
			int innerH = box.getHeight() - 32;
			drawSprite(0,      0, 0, 4, 16, 16, 32);		// Top Left
			drawSprite(16,     0, 1, 4, innerW, 16, 32);	// Top Mid
			drawSprite(innerW + 16, 0, 2, 4, 16, 16, 32);	// Top Right
			drawSprite(0,  16, 0, 5, 16, innerH, 32);			// Mid Left
			drawSprite(16, 16, 1, 5, innerW, innerH, 32);		// Mid Mid
			drawSprite(innerW + 16, 16, 2, 5, 16, innerH, 32);
			drawSprite(0,           innerH + 16, 0, 6, 16, 16, 32);		// Bottom Left
			drawSprite(16,          innerH + 16, 1, 6, innerW, 16, 32);	// Bottom Mid
			drawSprite(innerW + 16, innerH + 16, 2, 6, 16, 16, 32);		// Bottom Right
		glPopMatrix();
	}

	public static void drawText(int x, int y, ReadableColor c, String text){
		glPushMatrix();
			glTranslatef(x, y, 0);
			glColor3f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f);
			for (int i = 0; i < text.length(); i++){
				char drawCh = text.toUpperCase().charAt(i);
				int texX = (drawCh - ' ')%32;
				int texY = (drawCh - ' ')/32;
				drawSprite(i*16, 0, texX, texY, 16, 16, 32);
			}
		glPopMatrix();
	}
	
	public static void drawBubble(Rectangle box){
		drawRect(box);
		drawSprite(box.getX() + (box.getWidth()/2 - 16), box.getY()+box.getHeight()-16, 3, 4, 16, 16, 32);
		drawSprite(box.getX() + (box.getWidth()/2), box.getY()+box.getHeight()-16,      4, 4, 16, 16, 32);
		drawSprite(box.getX() + (box.getWidth()/2 - 16), box.getY()+box.getHeight(),    3, 5, 16, 16, 32);
		drawSprite(box.getX() + (box.getWidth()/2), box.getY()+box.getHeight(),         4, 5, 16, 16, 32);
	}

}
