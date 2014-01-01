package entities;

import gui.GUtil.SpriteSheet;

public class Item extends AbstractEntity
{
	public Item(float x, float y){
		super(x, y);
	}

	protected SpriteSheet getSpriteSheet(){
		return SpriteSheet.ITEMS;
	}
}
