package gui;

import gui.GUtil.SpriteSheet;

import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;

public class GChar
{
	private int id, x, y, width, height, xOffset, yOffset, xAdvance;

	@Override
	public String toString()
	{
		return "GChar [id=" + (char) id + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", xOffset="
				+ xOffset + ", yOffset=" + yOffset + ", xAdvance=" + xAdvance + "]";
	}

	public GChar(int id, int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xAdvance = xAdvance;
	}
	
	public int draw(int xPos, int yPos, Texture tex, ReadableColor c){
		GUtil.drawSprite(SpriteSheet.FONT, xPos+xOffset, yPos+yOffset, width, height, 
				x, y, width, height, tex.getTextureHeight(), c);
		return xAdvance;
	}

	public int getId()
	{
		return id;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getxOffset()
	{
		return xOffset;
	}

	public int getyOffset()
	{
		return yOffset;
	}

	public int getxAdvance()
	{
		return xAdvance;
	}



}
