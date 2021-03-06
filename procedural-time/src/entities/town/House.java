package entities.town;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.Rectangle;

import core.Game;
import core.RandomManager;
import core.Tile;
import entities.concrete.Human.Gender;
import entities.concrete.NPC;
import entities.concrete.NPC.HousePurpose;


public class House {
	private static int COUNT = 0;
	public static enum HouseType {WORK, RESIDENTIAL2, B, WORK2, RESIDENTIAL};
	private static String[] housePrefixes = {"d", "g", "b", "r", ""};
	
	Town town;
	LinkedList<Tile> doors;
	LinkedList<NPC> dependent;
	Rectangle rect;
	HouseType type;
	Random rand;

	public House(Rectangle rect, Town town, HouseType type){
		rand = new Random(RandomManager.getSeed("House:"+(COUNT++)));
		this.doors = new LinkedList<>();
		this.dependent = new LinkedList<>();
		this.rect = rect;
		this.town = town;
		this.type = type;
	}
	
	public void spawnPerson(){
		if (type != null && (type.equals(HouseType.RESIDENTIAL) || type.equals(HouseType.RESIDENTIAL2))){
			// Spawn person
			NPC tmpHuman;
			Tile placeTile = getRandContainedTile();
			tmpHuman = new NPC(placeTile.getLeft(), placeTile.getTop(), Gender.MALE);
			tmpHuman.setHouse(HousePurpose.HOME, this);
			attachNPC(tmpHuman);
			
			
			// Find work location
			int numWorkLoc = town.getNumType(HouseType.WORK) + town.getNumType(HouseType.WORK2);
			for (House h : town.getHouses()){
				if (h == this) continue;
				if (h.getType().equals(HouseType.WORK) || h.getType().equals(HouseType.WORK2)){
					if (rand.nextFloat() < 1f/numWorkLoc){
						// Pick this house
						tmpHuman.setHouse(HousePurpose.WORK, h);
						h.attachNPC(tmpHuman);
					} else {
						numWorkLoc--;
					}
				}
			}
			
			tmpHuman.findFriends(town.getHouses());
		}
	}
	
	public Tile getRandContainedTile(){
		int x = rand.nextInt(rect.getWidth()-3) + rect.getX() + 1;
		int y = rand.nextInt(rect.getHeight()-3) + rect.getY() + 1;
		return Game.getMap().getGridTile(x, y);
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
			npc.notifyHouseSwap(h, this);
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
	
	public float getDist(House h){
		return Math.abs((this.rect.getX()+(this.rect.getWidth()/2)) - (h.rect.getX()+(h.rect.getWidth()/2)))
			+  Math.abs((this.rect.getY()+(this.rect.getHeight()/2)) - (h.rect.getY()+(h.rect.getHeight()/2)));
	}
	
	public List<NPC> getDependents(){
		return dependent;
	}
}
