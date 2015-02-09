package entities.concrete;

import core.display.SpriteManager;
import entities.abstr.AbstractPlacedItem;
import gui.GUtil.SpriteSheetType;

public class Path extends AbstractPlacedItem {
	public Path(float x, float y) {
		super(x, y);
		setSprite(SpriteManager.get().getSprite(SpriteSheetType.ITEMS, "path_"+(rand.nextInt(3)+1)));
		setSpecialType(SpecialType.PATH);
		setDrawPriority(-1);
		setWalkable(true);
		setAligned(true);
	}
}
