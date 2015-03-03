package entities.town;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import core.Tile;
import core.path.NeighbourFunction;
import core.path.PathException;
import core.path.PathFinder;
import core.path.TargetFunction;

public class PathTree {

//	PathTarget pathTarget = new PathTarget();
	PathTarget rewriteTarget = new PathTarget(this);
	TargetFunction<Tile> pathTarget = new TargetFunction<Tile>() {
		@Override
		public boolean isTarget(Tile target) {
			return hasPath(target);
		}
	}; 
	
	PathNode root;
	HashMap<Tile, PathEdge> pathTiles;
	private PathFinder<Tile> pather;
	NeighbourFunction<Tile> neighbourFn = new PathNeighbour();
	
	public PathTree(Tile center){
		root = new PathNode(center);
		pathTiles = new HashMap<>();
		pather = new PathFinder<Tile>();

		// Dummy edge to support adding houses to root
		PathEdge dummy = new PathEdge();
		dummy.parent = root;
		dummy.child = root;
		pathTiles.put(center, dummy);
	}
	
	public TreeDiff checkAddHouse(House h, Set<Tile> exclude){
		List<Tile> path = null;
		for (Tile door : h.getDoors()){
			path = findPath(door, exclude, pathTarget, neighbourFn);
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
	 * @param h Optional House to be associated with path
	 * @return null if adding path is impossible, otherwise a TreeDiff object to be applied to the tree.
	 */
	public TreeDiff checkAddPath(List<Tile> path, House h){
		return checkAddPath(path, h, null);
	}
	
	/**
	 * Check if a simple path can be added to the tree.
	 * An edge with a null child ends in a house
	 * @param path Path to be added
	 * @param pathNode Optional PathNode to be associated with path
	 * @return null if adding path is impossible, otherwise a TreeDiff object to be applied to the tree.
	 */
	public TreeDiff checkAddPath(List<Tile> path, PathNode pathNode){
		return checkAddPath(path, null, pathNode);
	}
	
	private TreeDiff checkAddPath(List<Tile> path, House h, PathNode pathNode){
		// Get tile to connect to tree
		TreeDiff diff = new TreeDiff(this);
		Tile last = path.get(path.size()-1);
		
		PathEdge newEdge = new PathEdge();
		diff.addEdge(newEdge);
		newEdge.path = path;
		newEdge.child = pathNode;
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
	 * @param exclude Tiles to be routed around
	 * @return Appropriate diff for rewriting tree
	 */
	public TreeDiff checkRewrite(Set<Tile> exclude){
		TreeDiff diff = new TreeDiff(this);
		Set<PathNode> orphans = new HashSet<>();
		
		for (Tile t : exclude){
			if (!pathTiles.containsKey(t)) continue; // Ignore tiles not part of paths
			PathEdge cutEdge = pathTiles.get(t);
			diff.removeEdge(cutEdge);
			
			// Removing a node from the tree
			if (t.equals(cutEdge.child)){
				// Remove the node from orphans
				orphans.remove(cutEdge.child);
				// Remove all child edges
				for (PathEdge childEdge : cutEdge.child.children){
					diff.removeEdge(childEdge);
				}
			} else {
				orphans.add(cutEdge.child);	
			}
		}
		System.out.println("Created orphan list");
		
		// Attempt to reattach orphans
		boolean reattachFailed = false;
		TreeDiff reattachDiff = new TreeDiff(this);
		do {
			System.out.println("stuck");
			for (PathNode orphan : orphans){
				System.out.println("stuck2");
				// Generate valid path for this orphan
				System.out.println("Searching for path");
				List<Tile> path = findPath(orphan.position, exclude, rewriteTarget, neighbourFn);
				System.out.println("Found path");
				if (path != null){
					reattachFailed = true;
					break;
				}
				TreeDiff orphanDiff = checkAddPath(path, orphan);
				if (!reattachFailed) reattachDiff.compose(orphanDiff);
			}
		} while (reattachFailed && decayOrphans(orphans, diff));
		System.out.println("Not stuck");
		
		if (reattachFailed) return null;
		return diff;
	}
	
	/**
	 * Replaced orphaned PathNodes with their children
	 * @param orphans Set of orphaned PathNodes to be reconnected to the tree
	 * @return false if decay failed (no orphan decay was possible)
	 */
	private static boolean decayOrphans(Set<PathNode> orphans, TreeDiff diff){
		LinkedList<PathNode> decayed = new LinkedList<>();
		for (PathNode pn : orphans){
			System.out.println("Stuck decaying");
			for (PathEdge child: pn.children){
				diff.removeEdge(child);
				decayed.add(child.child);
			}
		}
		orphans.clear();
		orphans.addAll(decayed);
		return (!decayed.isEmpty());
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
	
	public List<Tile> findPath(Tile start, Set<Tile> exclude, TargetFunction<Tile> pathTarget, 
			NeighbourFunction<Tile> neighbourFn){
		List <Tile> l = null;
		pather.newPath(start, root.position, pathTarget, neighbourFn, exclude);
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
