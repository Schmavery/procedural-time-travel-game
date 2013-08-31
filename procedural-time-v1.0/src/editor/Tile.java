package editor;

import java.awt.Point;

public class Tile {
	private Point absPosition;
	private byte typeIndex;
	
	public Tile(Point position, byte typeIndex){
		this.absPosition = position;
		this.typeIndex = typeIndex;
	}
	
	public byte getType(){return typeIndex;}
	public int getX(){return (int) absPosition.getX();}
	public int getY(){return (int) absPosition.getY();}
	
	public void setType(byte typeIndex){this.typeIndex = typeIndex;}
}
