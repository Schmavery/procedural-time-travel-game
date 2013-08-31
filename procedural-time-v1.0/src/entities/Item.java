package entities;

import java.awt.Image;
import java.awt.Point;

public class Item {
	ItemType itemType;
	Point pos, flingPt, startPt;
	public boolean special, dead;
	int flingSpeed = 40;
	
	public Item(ItemType itemType, Point pos){
		this.itemType = itemType;
		this.pos = pos;
		if (pos != null){
			CollisionCore.unStick(this.pos);
		}
	}
	
	public Image getImg(){
		return this.itemType.getItemImage();
	}
	public String getName(){
		return this.itemType.getItemName();
	}
	public Point getPos(){
		return pos;
	}
	
	public void fling(Point flingPt){
		//this.flinging = true;
		this.flingPt = flingPt;

	}
	
	public Point getCentPos(){
		int addX = itemType.getItemImage().getWidth(null)/2;
		int addY = itemType.getItemImage().getHeight(null)/2;
		return new Point(pos.x + addX, pos.y + addY);
		
	}
	
	public void update(long delta){
		if (flingPt != null){
			if (pos.distance(flingPt) > 10){
				int dx = flingPt.x - pos.x;
				int dy = flingPt.y - pos.y;
				pos.translate(Integer.signum(dx)*Math.min(flingSpeed,Math.abs(dx/2)), Integer.signum(dy)*Math.min(flingSpeed,Math.abs(dy/2)));
				//System.out.println(pos.distance(flingPt));
			} else {
				this.dead = true;
			}
		}
	}
	
}
