package gui;

import org.lwjgl.util.Color;

public class GBorderFactory {
	public static GBorder createBasicBorder(Color c){
		return new GBasicBorder(c);
	}
	
	public static GBorder createButtonBorder(Color c){
		return new GButtonBorder(c);
	}
}
