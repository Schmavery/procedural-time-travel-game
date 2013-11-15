package gui;

import org.lwjgl.util.ReadableColor;

public class GBorderFactory {
	public static IBorder createBasicBorder(ReadableColor c){
		return new GBasicBorder(c);
	}
	
	public static IBorder createButtonBorder(ReadableColor c){
		return new GButtonBorder(c);
	}
}
