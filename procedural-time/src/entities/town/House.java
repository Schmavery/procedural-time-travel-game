package entities.town;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.Rectangle;

import core.Game;
import core.Tile;
import entities.concrete.Human.Gender;
import entities.concrete.NPC;
import entities.concrete.NPC.HousePurpose;


public class House {
	public static enum HouseType {D, G, B, R, RESIDENTIAL};
	private static String[] housePrefixes = {"d", "g", "b", "r", ""};
	LinkedList<Tile> doors;
	LinkedList<NPC> dependent;
	Rectangle rect;
	HouseType type;

	public House(Rectangle rect, HouseType type){
		this.doors = new LinkedList<>();
		this.dependent = new LinkedList<>();
		this.rect = rect;
		this.type = type;
	}
	
	public void spawnPerson(){
		if (type != null && type.equals(HouseType.RESIDENTIAL)){
			// Spawn person
			NPC tmpHuman;
			tmpHuman = new NPC((rect.getX() + rect.getWidth()/2)*Game.SCALE*Game.TILE_SIZE, 
					(rect.getY() + rect.getHeight()/2)*Game.SCALE*Game.TILE_SIZE, Gender.MALE);
			tmpHuman.setHouse(HousePurpose.HOME, this);
			attachNPC(tmpHuman);
		}
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
	
	/**
	 * Swap NPC dependencies on this house and h
	 * @param h House with which to swap
	 */
	public void swap(House h){
		// TODO: Notify all attached NPCs
		for (NPC npc : dependent){
			npc.notifyHouseSwap(this, h);
		}
		for (NPC npc : h.dependent){
			npc.notifyHouseSwap(this, h);
		}
		LinkedList<NPC> tmp = dependent;
		dependent = h.dependent;
		h.dependent = tmp;
	}
	
	public void attachNPC(NPC npc){
		dependent.add(npc);
	}
	
	@Override
	public String toString(){
		return "House:("+rect.getX()+","+rect.getY()+","+rect.getWidth()+","+rect.getHeight()+")";
	}
}
