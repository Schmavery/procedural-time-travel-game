package entities.town;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import core.Game;
import core.Tile;
import core.path.NeighbourFunction;
import entities.interfaces.Entity.SpecialType;

public class PathNeighbour implements NeighbourFunction<Tile> {

	PathTree tree;
	Set<Tile> exclude;
	public PathNeighbour(PathTree tree){
		this.tree = tree;
	}
	
	public void setExclude(Set<Tile> exclude){
		this.exclude = exclude;
	}
	
	private void addTile(List<Tile> list, int x, int y){
		Tile t = Game.getMap().getGridTile(x, y);
		if (x >= 0
				&& x < Game.getMap().getSize()
				&& y >= 0
				&& y < Game.getMap().getSize()
				&& t.isWalkable()
				&& !t.hasSpecialType(SpecialType.HOUSE)
				&& (!tree.pathTiles.containsKey(t) || tree.isFreeOf(t, exclude))
				){
			list.add(Game.getMap().getGridTile(x, y));
		}
	}
	@Override
	public List<Tile> getNeighbours(Tile node) {
		List<Tile> reachable = new ArrayList<>(4);
		addTile(reachable, node.getGridX() - 1, node.getGridY());
		addTile(reachable, node.getGridX()    , node.getGridY() - 1);
		addTile(reachable, node.getGridX() + 1, node.getGridY());
		addTile(reachable, node.getGridX()    , node.getGridY() + 1);
		return reachable;
	}
}
