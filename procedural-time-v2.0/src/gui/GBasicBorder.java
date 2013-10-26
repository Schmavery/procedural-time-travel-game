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
				GUtil.drawRect(comp.boundingBox);
			}
		
		glPopMatrix();
	}
}