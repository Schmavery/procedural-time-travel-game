package entities.interfaces;

import core.display.SpriteInstance;
import entities.concrete.Human;

public interface Holdable extends Item{
	public void swing(Human user);
	public void use(Human user);
	public SpriteInstance[] getSwingArray();
	public SpriteInstance[] getUseArray();
}
