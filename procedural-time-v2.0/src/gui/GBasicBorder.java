package gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

public class GBasicBorder implements IBorder{
	
	private ReadableColor color;
	
	public GBasicBorder(ReadableColor c){
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