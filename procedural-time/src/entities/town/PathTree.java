package entities.town;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import core.Tile;

public class PathTree {
	class PathNode {
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
	
	class PathEdge {
		PathNode parent;
		List<Tile> path;
		PathNode child;
		
		@Override
		public String toString(){
			String s = "";
			s += "Parent:(" + parent + ")";
			if (path != null) s += "PathLen:"+path.size();
			s += "Child:(" + child + ")";
			return s;
		}
	}
	
	PathNode root;
	HashMap<Tile, PathEdge> pathTiles;
	
	public PathTree(Tile center){
		root = new PathNode(center);
		pathTiles = new HashMap<>();
		
		// Dummy edge to support adding houses to root
		PathEdge dummy = new PathEdge();
		dummy.parent = root;
		dummy.child = root;
		System.out.println("Dummy:" + dummy);
		pathTiles.put(center, dummy);
	}
	
	/**
	 * Add simple path to the tree
	 */
	public void addPath(List<Tile> path){
		// Get tile to connect to tree
		Tile last = path.get(path.size()-1);
		
		PathEdge newEdge = new PathEdge();
		newEdge.path = path;
		
		if (pathTiles.containsKey(last)){
			PathEdge oldEdge = pathTiles.get(last);
			
			Tile t;
			for (int i = 0; i < path.size()-1; i++){ // Skip tile that connects to tree
				t = path.get(i);
				if (!pathTiles.containsKey(t)){
					pathTiles.put(t, newEdge);
				} else {
					throw new RuntimeException("Overlapping paths -> bad path");
				}
			}
			
			PathNode parent = oldEdge.parent;
			if (parent == null){
				int i = 0;
				System.out.println(i);
			}
			if (parent.position.equals(last)){
				newEdge.parent = parent;
				newEdge.child = null;
				parent.children.add(newEdge);
			} else {
				// make new branch
				System.out.println("Adding path");
				PathNode branch = new PathNode(last);
				PathEdge bottomEdgeHalf = new PathEdge();
				PathEdge topEdgeHalf = new PathEdge();
				bottomEdgeHalf.path = new LinkedList<>();
				topEdgeHalf.path = new LinkedList<>();
				bottomEdgeHalf.parent = branch;
				topEdgeHalf.parent = oldEdge.parent;
				topEdgeHalf.child = branch;
				bottomEdgeHalf.child = oldEdge.child;
				
				newEdge.parent = branch;
				newEdge.child = null;
				
				branch.children.add(newEdge);
				branch.children.add(bottomEdgeHalf);
				branch.parent = topEdgeHalf;
				
				if (oldEdge.child != null) oldEdge.child.parent = bottomEdgeHalf;
				oldEdge.parent.children.remove(oldEdge);
				oldEdge.parent.children.add(topEdgeHalf);
				
				boolean reachedBranch = false;
				for (Tile pathPiece : oldEdge.path){
					if (reachedBranch |= (pathPiece.equals(last))){
						topEdgeHalf.path.add(pathPiece);
						pathTiles.put(pathPiece, topEdgeHalf);
					} else {
						bottomEdgeHalf.path.add(pathPiece);
						pathTiles.put(pathPiece, bottomEdgeHalf);
					}
				}
			}
		} else {
			throw new RuntimeException("Path does not connect with tree");
		}
	}
	
	/**
	 * Return true if the target is an existing path Tile that is not
	 * a descendant of any of the tiles in exclude.
	 * @param target Tile being tested
	 * @param exclude Set of excluded parents
	 * @return True if the path can connect to this target.
	 */
	public boolean isFreeOf(Tile target, List<Tile> exclude){
		if (!pathTiles.containsKey(target)) return false;
		else {
			PathEdge edge = pathTiles.get(target);
			boolean targetReached = false;
			while (edge != null){
				for (Tile currTile : edge.path){
					if (targetReached |= currTile.equals(target)){
						if (exclude.contains(currTile)) return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean hasPath(Tile t){
		return pathTiles.containsKey(t);
	}
	
}
