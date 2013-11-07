package gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.lwjgl.util.Color;

public class GBasicBorder implements IBorder{
	
	private Color color;
	
	public GBasicBorder(Color c){
		this.color = c;
	}

	public void drawBorder(GComponent comp) {
		glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		glPushMatrix();
			if (comp.isVisible()){
				GUtil.drawRect(comp.getRect());
			}
		
		glPopMatrix();
	}
}