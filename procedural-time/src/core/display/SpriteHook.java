package core.display;

import org.lwjgl.util.Point;

public class SpriteHook
{
	private String name;
	private Point pt;
	
	public SpriteHook(String name, Point pt){
		this.pt = pt;
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Point getPt()
	{
		return pt;
	}
}
