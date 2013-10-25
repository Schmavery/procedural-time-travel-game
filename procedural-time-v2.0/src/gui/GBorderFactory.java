package gui;

import javax.swing.border.Border;

import org.lwjgl.util.Color;

public class GBorderFactory {
	public static GBorder createBasicBorder(Color c){
		return new GBasicBorder(c);
	}
}
