package gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

public class GTextbox extends GElement{

	private String text;
	private int textLen;
	private Color textColor;
	
	public GTextbox(String name, String text, int posX, int posY) {
		super(name, null, null, null);
		Rectangle rect = new Rectangle(posX, posY, 16*text.length(), 16);
		setRect(rect);
		this.text = text;
		this.textLen = text.length();
		this.textColor = new Color(0, 0, 0);
	}

	public void setText(String text){
		if (text.length() > textLen){
			this.text = text.substring(0, textLen);
			return;
		}
		this.text = text;
	}
	
	public void setTextColor(Color c){
		this.textColor = c;
	}
	
	public ClickEvent clickDown(int x, int y) {
		return null;
	}

	public ClickEvent clickUp(int x, int y) {
		return null;
	}

	public ClickEvent clickHold(int x, int y) {
		return null;
	}

	public void update(long deltaTime) {
		
	}

	public void draw() {
		glPushMatrix();
			glColor3f(textColor.getRed()/255f, textColor.getGreen()/255f, textColor.getBlue()/255f);
			glTranslatef(getX(), getY(), 0);
			drawText(text);
		glPopMatrix();
	}

}
