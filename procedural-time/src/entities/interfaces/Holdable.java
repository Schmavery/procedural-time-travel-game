package entities.interfaces;

import core.display.SpriteInstance;
import entities.concrete.Humanoid;

public interface Holdable extends Item{
	public void swing(Humanoid user);
	public void use(Humanoid user);
	public SpriteInstance[] getSwingArray();
	public SpriteInstance[] getUseArray();
}
