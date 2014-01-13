package entities.interfaces;

import core.AnimationManager.Animation;
import entities.concrete.Humanoid;

public interface Holdable extends Item{
	public void swing(Humanoid user);
	public void use(Humanoid user);
	public Animation[] getSwingArray();
	public Animation[] getUseArray();
}
