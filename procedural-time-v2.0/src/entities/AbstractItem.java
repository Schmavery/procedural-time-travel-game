package entities;

import gui.GUtil.SpriteSheet;

public abstract class AbstractItem extends AbstractEntity
{
	public AbstractItem(float x, float y){
		super(x, y);
	}

	protected SpriteSheet getSpriteSheet(){
		return SpriteSheet.ITEMS;
	}
	
	public abstract void swing(Humanoid user);
	
	public abstract void use(Humanoid user);
}
