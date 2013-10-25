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

import org.lwjgl.util.Color;

public class GBasicBorder implements GBorder{
	
	private Color color;
	
	public GBasicBorder(Color c){
		this.color = c;
	}

	public void drawBorder(GComponent comp) {
		glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		glPushMatrix();
			glTranslatef(comp.getX(), comp.getY(), 0);
			
			if (comp.isVisible()){
				int innerW = comp.getWidth() - 32;
				int innerH = comp.getHeight() - 32;
				drawSprite(0,      0, 0, 4, 16, 16);		// Top Left
				drawSprite(16,     0, 1, 4, innerW, 16);	// Top Mid
				drawSprite(innerW + 16, 0, 2, 4, 16, 16);	// Top Right
				drawSprite(0,  16, 0, 5, 16, innerH);			// Mid Left
				drawSprite(16, 16, 1, 5, innerW, innerH);		// Mid Mid
				drawSprite(innerW + 16, 16, 2, 5, 16, innerH);
				drawSprite(0,           innerH + 16, 0, 6, 16, 16);		// Bottom Left
				drawSprite(16,          innerH + 16, 1, 6, innerW, 16);	// Bottom Mid
				drawSprite(innerW + 16, innerH + 16, 2, 6, 16, 16);		// Bottom Right
			}
		
		glPopMatrix();
	}
	
	protected static void drawSprite(float x, float y, int texX, int texY, float spriteW, float spriteH){
		glPushMatrix();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
			glTexCoord2f(texX/32f, texY/32f);				//short,short
			glVertex2f(0, 0);
			glTexCoord2f((texX+1)/32f, texY/32f);			//long, short
			glVertex2f(spriteW, 0);
			glTexCoord2f((texX+1)/32f, (texY+1)/32f);		//long,  long
			glVertex2f(spriteW, spriteH);
			glTexCoord2f(texX/32f, (texY+1)/32f);			//short, long
			glVertex2f(0, spriteH);
			glEnd();
		glPopMatrix();
	}

}
