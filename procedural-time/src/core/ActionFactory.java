package core;

import core.AnimationManager.Animation;
import entities.abstr.AbstractEntity.Facing;

public class ActionFactory {
	public static enum ActionType {DROP, SWING, USE, RETREIVE, DIE};
	
	public static Action drop(){
		return new Action(
				ActionType.DROP,
				Game.getAnims().getAnim("man_n_walk"), 
				Game.getAnims().getAnim("man_e_walk"),
				Game.getAnims().getAnim("man_s_walk"),
				Game.getAnims().getAnim("man_w_walk"));
	}
	
	public static Action swing(){
		return new Action(
				ActionType.SWING,
				Game.getAnims().getAnim("man_n_walk"), 
				Game.getAnims().getAnim("man_e_walk"),
				Game.getAnims().getAnim("man_s_walk"),
				Game.getAnims().getAnim("man_w_walk"));
	}
	
	public static Action use(){
		return new Action(
				ActionType.USE,
				Game.getAnims().getAnim("man_n_walk"), 
				Game.getAnims().getAnim("man_e_walk"),
				Game.getAnims().getAnim("man_s_walk"),
				Game.getAnims().getAnim("man_w_walk"));
	}
	
	public static Action retreive(){
		return new Action(
				ActionType.RETREIVE,
				Game.getAnims().getAnim("man_n_stow"), 
				Game.getAnims().getAnim("man_e_stow"),
				Game.getAnims().getAnim("man_s_stow"),
				Game.getAnims().getAnim("man_w_stow"));
	}
	
	public static Action die(){
		return new Action(
				ActionType.DIE,
				Game.getAnims().getAnim("man_n_stow"), 
				Game.getAnims().getAnim("man_e_stow"),
				Game.getAnims().getAnim("man_s_stow"),
				Game.getAnims().getAnim("man_w_stow"));
	}
	
	public static class Action {
		
		private Animation[] anims;
		private ActionType actionType;
		
		public Action(ActionType at, Animation a_n, Animation a_e, Animation a_s, Animation a_w){
			actionType = at;
			anims = new Animation[4];
			anims[0] = a_n.cloneAnim();
			anims[1] = a_e.cloneAnim();
			anims[2] = a_s.cloneAnim();
			anims[3] = a_w.cloneAnim();
		}
		
		public boolean update(long deltaTime){
			for (int i = 0; i < anims.length; i++){
				if (!anims[i].update(deltaTime)){
					return false;
				}
			}
			return true;
		}
		
		public Animation getAnim(Facing facing){
			return anims[facing.ordinal()];
		}
		
		@Override
		public String toString(){
			return actionType.toString();
		}
		
	}
}
