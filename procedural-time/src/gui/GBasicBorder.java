package gui;

import org.lwjgl.util.ReadableColor;

public class GBasicBorder implements IBorder{
	
	private ReadableColor color;
	
	public GBasicBorder(ReadableColor c){
		this.color = c;
	}

	public void drawBorder(GComponent comp) {
		if (comp.isVisible()){
			GUtil.drawRect(comp.getRect(), color);
		}
	}
}