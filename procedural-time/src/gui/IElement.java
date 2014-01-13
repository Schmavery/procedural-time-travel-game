package gui;

import org.lwjgl.util.Rectangle;

public interface IElement {

	public void hide();

	public void show();

	public void setBorder(IBorder border);

	public String getName();

	public boolean isVisible();

	public boolean isClicked();

	public boolean overlaps(IElement e);

	/**
	 * On a click event, returns a corresponding GClickEvent
	 * @return GClickEvent
	 */
	public GClickEvent clickDown(int x, int y);

	/**
	 * On a click event, returns a corresponding GClickEvent
	 * @return GClickEvent
	 */
	public GClickEvent clickUp(int x, int y);

	public GClickEvent clickHold(int x, int y);

	public void update(long deltaTime);

	public void draw();

	public void setRect(Rectangle rect);
	
	public void translateRect(int x, int y);

	public Rectangle getRect();

	public int getHeight();

	public int getWidth();

	public int getX();

	public int getY();

}