package entities.town;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.Rectangle;

import core.Tile;


public class House {
	public static enum HouseType {D, G, B, R, NONE};
	private static String[] housePrefixes = {"d", "g", "b", "r", ""};
	LinkedList<Tile> doors;
	Rectangle rect;
	HouseType type;
	int swapCount;

	public House(Rectangle rect, HouseType type){
		this.doors = new LinkedList<>();
		this.rect = rect;
		this.type = type;
		swapCount = 0;
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
		return housePrefixes[type.ordinal()];
	}
	
	public HouseType getType(){
		return type;
	}
	
	public void setType(HouseType t){
		this.type = t;
	}
	
	public int getSwapCount() {return swapCount;}
	public void incSwaps() {swapCount++;}
	
	@Override
	public String toString(){
		return "House:("+rect.getX()+","+rect.getY()+","+rect.getWidth()+","+rect.getHeight()+")";
	}
}
