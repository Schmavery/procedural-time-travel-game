package gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.lwjgl.util.ReadableColor;

public class GButtonBorder implements IBorder{

	private ReadableColor color;
	
	public GButtonBorder(ReadableColor c) {
		this.color = c;
	}

	@Override
	public void drawBorder(GComponent comp) {
		if (comp.isClicked()){
			glColor3f((color.getRed()+100)/255f, (color.getGreen()+100)/255f, (color.getBlue()+100)/255f);
		} else {
			glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		}
		glPushMatrix();
			if (comp.isVisible()){
				GUtil.drawRect(comp.getRect());
			}
		
		glPopMatrix();
	}
		
}