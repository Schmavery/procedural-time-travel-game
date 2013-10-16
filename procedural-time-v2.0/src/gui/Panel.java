package gui;

import org.lwjgl.util.Rectangle;

public interface Panel {
	public void click(int x, int y);
	public void actionPerformed(String action);
	public void draw();
}
