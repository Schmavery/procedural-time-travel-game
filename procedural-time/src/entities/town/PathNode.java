package entities.town;

import java.util.LinkedList;
import java.util.List;

import core.Tile;

public class PathNode {
	Tile position;
	List<PathEdge> children;
	PathEdge parent;
	public PathNode(Tile position) {
		this.position = position;
		children = new LinkedList<>();
	}
	
	@Override
	public String toString(){
		String s = "";
		s += "Position:"+position;
		if (children != null) s += "#Children:"+children.size();
		return s;
	}
}
