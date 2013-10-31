package gui;

import org.lwjgl.util.Color;

public class GBorderFactory {
	public static IBorder createBasicBorder(Color c){
		return new GBasicBorder(c);
	}
	
	public static IBorder createButtonBorder(Color c){
		return new GButtonBorder(c);
	}
}
