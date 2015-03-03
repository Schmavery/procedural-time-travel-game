package entities.town;

import java.util.ArrayList;
import java.util.List;

import core.Game;
import core.Tile;
import core.path.NeighbourFunction;
import entities.interfaces.Entity.SpecialType;

public class PathNeighbour implements NeighbourFunction<Tile> {

	private static void addTile(List<Tile> list, int x, int y){
		if (x >= 0
				&& x < Game.getMap().getSize()
				&& y >= 0
				&& y < Game.getMap().getSize()
				&& Game.getMap().getGridTile(x, y).isWalkable()
				&& !Game.getMap().getGridTile(x, y).hasSpecialType(SpecialType.HOUSE)){
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
