package entities.town;

import java.util.List;

import core.Tile;

/**
 * Stores basic node information.  Exactly one of either child or house are always null.
 */
public class PathEdge {
	PathNode parent;
	List<Tile> path;
	PathNode child;
	House house;
	
	@Override
	public String toString(){
		String s = "";
		s += "Parent:(" + parent + ")";
		if (path != null) s += "PathLen:"+path.size();
		s += "Child:(" + child + ")";
		return s;
	}
}