package gui;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Color;

public class GButtonBorder implements IBorder{

	private Color color;
	
	public GButtonBorder(Color c) {
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