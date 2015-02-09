package entities.town;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.Rectangle;

import core.Tile;


public class House {
	LinkedList<Tile> doors;
	Rectangle rect;

	public House(Rectangle rect){
		doors = new LinkedList<>();
		this.rect = rect;
	}
	public void addDoor(Tile gridPt){
		doors.add(gridPt);
	}
	public List<Tile> getDoors(){
		return doors;
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
	@Override
	public String toString(){
		return "House:("+rect.getX()+","+rect.getY()+","+rect.getWidth()+","+rect.getHeight()+")";
	}
}
