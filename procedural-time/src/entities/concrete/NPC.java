package entities.concrete;

import entities.town.House;
import gui.GUtil.SpriteSheetType;
import core.display.SpriteManager;

public class NPC extends Human {
	public enum HousePurpose {HOME, WORK};
	public enum DayState {HOME, TO_WORK, WORK, TO_HOME};
	private House[] houses;
	
	public NPC(float x, float y, Gender gender) {
		super(x, y, gender);
		setMovingAnims(SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n_walk"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w_walk"));
		setStandingAnims(SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w"));
		houses = new House[HousePurpose.values().length];
	}
	
	public void setHouse(HousePurpose p, House h){
		houses[p.ordinal()] = h;
	}
	
	public void notifyHouseSwap(House oldH, House newH){
		for (int i = 0; i < houses.length; i++){
			if (houses[i] == oldH){
				houses[i] = newH;
			}
		}
	}
	
	@Override
	public void update(long deltaTime){
		super.update(deltaTime);
	}
	
}
