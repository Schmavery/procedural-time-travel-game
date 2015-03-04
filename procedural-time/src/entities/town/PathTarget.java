package entities.town;

import java.util.HashSet;
import java.util.Set;

import core.Tile;
import core.path.TargetFunction;

public class PathTarget implements TargetFunction<Tile> {
	Set<Tile> exclude = new HashSet<>();
	PathTree pathTree;
	
	public PathTarget(PathTree pathTree) {
		this.pathTree = pathTree;
	}
	
	public void setExclude(Set<Tile> exclude){
		this.exclude = exclude;
	}

	@Override
	public boolean isTarget(Tile target) {
		return pathTree.isFreeOf(target, exclude);
	}
}
