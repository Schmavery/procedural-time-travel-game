package gui;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

public class GButtonBorder implements IBorder{

	private ReadableColor color;
	private ReadableColor lightColor;
	
	public GButtonBorder(ReadableColor c) {
		this.color = c;
		this.lightColor = new Color((color.getRed()+100), (color.getGreen()+100), (color.getBlue()+100));
	}

	@Override
	public void drawBorder(GComponent comp) {
		if (comp.isVisible()){
			if (comp.isClicked()){
				GUtil.drawRect(comp.getRect(), lightColor);
			} else {
				GUtil.drawRect(comp.getRect(), color);
			}
		}
	}
	
}