package entities.concrete;

import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.util.Rectangle;

import core.Tile;
import core.display.SpriteManager;
import entities.town.House;
import entities.town.House.HouseType;
import gui.GUtil.SpriteSheetType;

public class NPC extends Human {
	private static final float FRIEND_DIST = 10;
	public enum HousePurpose {HOME, WORK};
	public enum DayState {HOME, WORK, RAND};
	private House[] houses;
	private ArrayList<NPC> friends;
	private DayState state;
	private boolean interrupted;
	private int putterDelay;
	
	public NPC(float x, float y, Gender gender) {
		super(x, y, gender);
		state = DayState.RAND;
		putterDelay = 0;
		setMovingAnims(SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n_walk"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w_walk"));
		setStandingAnims(SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w"));
		houses = new House[HousePurpose.values().length];
		friends = new ArrayList<>();
	}
	
	public void setHouse(HousePurpose p, House h){
		houses[p.ordinal()] = h;
		state = DayState.HOME;
		putterDelay = rand.nextInt(10) + 3;
	}
	
	public void notifyHouseSwap(House oldH, House newH){
		for (int i = 0; i < houses.length; i++){
			if (houses[i] == oldH){
				houses[i] = newH;
			}
		}
		interrupted = true;
	}
	
	@Override
	public void update(long deltaTime){
		if ((!isMoving() || interrupted) && !this.isDead()){
			interrupted = false;
			
			
			
			switch (state){
			case HOME:
				if (putterDelay < 0){
//					say("I'm going to go home");
					putterDelay = rand.nextInt(10) + 3;
				}
				if (rand.nextInt(100) == 1){
					Tile t = houses[HousePurpose.HOME.ordinal()].getRandContainedTile();
					walkTo(t.getGridX(), t.getGridY());
					putterDelay--;
					if (putterDelay == 0) state = DayState.WORK;
				}
				
				break;
			case WORK:
				if (putterDelay < 0){
//					say("I'm going to go to work");
					putterDelay = rand.nextInt(10) + 3;
				}
				if (rand.nextInt(100) == 1){
					Tile t = houses[HousePurpose.WORK.ordinal()].getRandContainedTile();
					walkTo(t.getGridX(), t.getGridY());
					putterDelay--;
					if (putterDelay == 0) state = DayState.HOME;
				}
				break;
			case RAND:
			default:
				// Random walk
				if (rand.nextInt(100) == 1){
					int destX = this.getTileX() + (rand.nextInt(10) - 5);
					int destY = this.getTileY() + (rand.nextInt(10) - 5);
					walkTo(destX, destY);
				}
				if (rand.nextInt(5000) == 1){
					say("Hey.");
				}
				break;
			}
		}
		super.update(deltaTime);
	}

	public void findFriends(LinkedList<House> town) {
		for (House h : town){
			if (h.getType().equals(HouseType.RESIDENTIAL)
					|| h.getType().equals(HouseType.RESIDENTIAL2)){
				if (houses[HousePurpose.HOME.ordinal()] != null 
						&& houses[HousePurpose.HOME.ordinal()].getDist(h) < FRIEND_DIST){
					findFriends(h);
				}
			} else if (houses[HousePurpose.WORK.ordinal()] != null 
					&& houses[HousePurpose.WORK.ordinal()].equals(h)){
				findFriends(h);
			}
		}
	}
	
	public void findFriends(House h){
		for (NPC npc : h.getDependents()){
			if (!friends.contains(npc)){
				
			}
		}
	}
	
}
