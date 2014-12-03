package core;

import entities.abstr.AbstractEntity.Facing;
import gui.GUtil.SpriteSheetType;
import core.display.Sprite;
import core.display.SpriteInstance;
import core.display.SpriteManager;

public class ActionFactory {
	public static enum ActionType {DROP, SWING, USE, RETREIVE, DIE};
	
	public static Action drop(){
		return new Action(
				ActionType.DROP,
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n_walk"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w_walk"));
	}
	
	public static Action swing(){
		return new Action(
				ActionType.SWING,
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n_walk"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w_walk"));
	}
	
	public static Action use(){
		return new Action(
				ActionType.USE,
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n_walk"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s_walk"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w_walk"));
	}
	
	public static Action retreive(){
		return new Action(
				ActionType.RETREIVE,
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n_stow"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e_stow"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s_stow"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w_stow"));
	}
	
	public static Action die(){
		return new Action(
				ActionType.DIE,
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_n_stow"), 
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_e_stow"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_s_stow"),
				SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "man_w_stow"));
	}
	
	public static class Action {
		
		private SpriteInstance[] anims;
		private ActionType actionType;
		
		public Action(ActionType at, Sprite spr_n, Sprite spr_e, Sprite spr_s, Sprite spr_w){
			actionType = at;
			anims = new SpriteInstance[4];
			anims[0] = spr_n.getInstance(true);
			anims[1] = spr_e.getInstance(true);
			anims[2] = spr_s.getInstance(true);
			anims[3] = spr_w.getInstance(true);
		}
		
		public boolean update(long deltaTime){
			for (int i = 0; i < anims.length; i++){
				if (!anims[i].update(deltaTime)){
					anims[i].restart();
					return false;
				}
			}
			return true;
		}
		
		public SpriteInstance getAnim(Facing facing){
			return anims[facing.ordinal()];
		}
		
		@Override
		public String toString(){
			return actionType.toString();
		}
		
	}
}
