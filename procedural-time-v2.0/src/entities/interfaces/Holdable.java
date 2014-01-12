package entities.interfaces;

import entities.concrete.Humanoid;

public interface Holdable extends Item{
	public void swing(Humanoid user);
	public void use(Humanoid user);
}
