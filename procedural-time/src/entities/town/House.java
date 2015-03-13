package entities.town;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.Rectangle;

import core.Tile;


public class House {
	LinkedList<Tile> doors;
	Rectangle rect;
	String spritePrefix;

	public House(Rectangle rect, String spr){
		doors = new LinkedList<>();
		this.rect = rect;
		spritePrefix = spr;
	}
	
	public void addDoor(Tile t){
		doors.add(t);
	}
	
	public List<Tile> getDoors(){
		return doors;
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
	public String getSpritePrefix(){
		return spritePrefix;
	}
	
	@Override
	public String toString(){
		return "House:("+rect.getX()+","+rect.getY()+","+rect.getWidth()+","+rect.getHeight()+")";
	}
}
