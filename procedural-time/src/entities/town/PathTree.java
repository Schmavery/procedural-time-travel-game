package entities.town;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import core.Game;
import core.Tile;
import core.path.NeighbourFunction;
import core.path.PathException;
import core.path.PathFinder;
import core.path.TargetFunction;
import entities.interfaces.Entity.SpecialType;

public class PathTree {
	private PathFinder<Tile> pather;

	
	class PathTarget implements TargetFunction<Tile>{
		Set<Tile> exclude = new HashSet<>();
		public void setExclude(Set<Tile> exclude){
			this.exclude = exclude;
		}
		@Override
		public boolean isTarget(Tile target) {
			return isFreeOf(target, exclude);
		}
	}
//	PathTarget pathTarget = new PathTarget();
	
	TargetFunction<Tile> pathTarget = new TargetFunction<Tile>() {
		@Override
		public boolean isTarget(Tile target) {
			return hasPath(target);
		}
	}; 
	
	NeighbourFunction<Tile> neighourFn = new NeighbourFunction<Tile>() {
		private void addTile(List<Tile> list, int x, int y){
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
	};
	
	
	
	
	PathNode root;
	HashMap<Tile, PathEdge> pathTiles;
	
	public PathTree(Tile center){
		root = new PathNode(center);
		pathTiles = new HashMap<>();
		pather = new PathFinder<Tile>();

		// Dummy edge to support adding houses to root
		PathEdge dummy = new PathEdge();
		dummy.parent = root;
		dummy.child = root;
		System.out.println("Dummy:" + dummy);
		pathTiles.put(center, dummy);
	}
	
	public TreeDiff checkAddHouse(House h, Set<Tile> exclude){
		List<Tile> path = null;
		for (Tile door : h.getDoors()){
			path = findPathToSpine(door, exclude);
			if (path != null){
				break;
			}
		}
		if (path == null) return null;
		return checkAddPath(path, h);
	}
	
	/**
	 * Check if a simple path can be added to the tree.
	 * An edge with a null child ends in a house
	 * @param path Path to be added
	 * @return null if adding path is impossible, otherwise a TreeDiff object to be applied to the tree.
	 */
	public TreeDiff checkAddPath(List<Tile> path, House h){
		// Get tile to connect to tree
		TreeDiff diff = new TreeDiff(this);
		Tile last = path.get(path.size()-1);
		
		PathEdge newEdge = new PathEdge();
		diff.addEdge(newEdge);
		newEdge.path = path;
		newEdge.child = null;
		newEdge.house = h;
		
		if (pathTiles.containsKey(last)){
			PathEdge oldEdge = pathTiles.get(last);
			
			for (Tile t : path){ 
				if (t.equals(last)) continue; // Skip tile that connects to tree
				if (!pathTiles.containsKey(t)){
					diff.addMapping(t, newEdge);
				} else {
					System.out.println("Overlapping paths -> bad path");
					return null;
				}
			}
			
			PathNode parent = oldEdge.parent;
			if (parent.position.equals(last)){
				newEdge.parent = parent;
			} else {
				// make new branch
				System.out.println("Adding path");
				PathNode branch = new PathNode(last);
				PathEdge bottomEdgeHalf = new PathEdge();
				PathEdge topEdgeHalf = new PathEdge();
				bottomEdgeHalf.parent = branch;
				topEdgeHalf.parent = oldEdge.parent;
				topEdgeHalf.child = branch;
				bottomEdgeHalf.child = oldEdge.child;
				
				newEdge.parent = branch;
				
				branch.children.add(newEdge);
				branch.children.add(bottomEdgeHalf);
				branch.parent = topEdgeHalf;
				
				// Split oldEdge path in 2
				bottomEdgeHalf.path = new LinkedList<>();
				topEdgeHalf.path = new LinkedList<>();
				boolean reachedBranch = false;
				for (Tile pathPiece : oldEdge.path){
					diff.removeMapping(pathPiece, oldEdge);
					if (reachedBranch |= (pathPiece.equals(last))){
						topEdgeHalf.path.add(pathPiece);
						diff.addMapping(pathPiece, topEdgeHalf);
					} else {
						bottomEdgeHalf.path.add(pathPiece);
						diff.addMapping(pathPiece, bottomEdgeHalf);
					}
				}
				diff.addEdge(bottomEdgeHalf);
				diff.addEdge(topEdgeHalf);
				diff.removeEdge(oldEdge);
			}
		} else {
			System.out.println("Path does not connect with tree");
			return null;
		}
		return diff;
	}
	
	/**
	 * Remove all excluded edges, maintain list of their orphaned nodes.
	 * Remove any nodes with neither parents nor children
	 * Reconnect children to tree.
	 * @param exclude
	 * @return
	 */
	public TreeDiff checkRewrite(Set<Tile> exclude){
		TreeDiff diff = new TreeDiff(this);
		return diff;
	}
	
	
	
	
	/**
	 * Return true if the target is an existing path Tile that is not
	 * a descendant of any of the tiles in exclude.
	 * @param target Tile being tested
	 * @param exclude Set of excluded parents
	 * @return True if the path can connect to this target.
	 */
	public boolean isFreeOf(Tile target, Set<Tile> exclude){
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
	
	public List<Tile> findPathToSpine(Tile start, Set<Tile> exclude){
		List <Tile> l = null;
		pather.newPath(start, root.position, pathTarget, neighourFn, exclude);
		try {
			while (!pather.generatePath());
		} catch (PathException e) {
			pather.clear();
			return null;
		}
		l = pather.getPath();
		return l;
	}
	
}
