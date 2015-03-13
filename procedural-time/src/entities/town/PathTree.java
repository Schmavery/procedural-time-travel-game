package entities.town;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import core.Tile;
import core.path.PathException;
import core.path.PathFinder;

public class PathTree {

	PathTarget rewriteTarget = new PathTarget(this);
	
	PathNode root;
	HashMap<Tile, PathEdge> pathTiles;
	private PathFinder<Tile> pather;
	PathNeighbour neighbourFn = new PathNeighbour(this);
	
	public PathTree(Tile center){
		root = new PathNode(center);
		pathTiles = new HashMap<>();
		pather = new PathFinder<Tile>();

		// Dummy edge to support adding houses to root
		PathEdge dummy = new PathEdge();
		dummy.parent = root;
		dummy.child = root;
		dummy.path = new LinkedList<Tile>();
		dummy.path.add(center);
		pathTiles.put(center, dummy);
	}
	
	public TreeDiff checkAddHouse(House h, Set<Tile> exclude){
		List<Tile> path = null;
		for (Tile door : h.getDoors()){
			path = findPath(door, exclude, rewriteTarget, neighbourFn);
			if (path != null){
				break;
			}
		}
		if (path == null || path.size() == 0) return null; //TODO handle size 0 paths
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
		newEdge.path = path;
		newEdge.child = pathNode;
		newEdge.house = h;
		
		if (pathTiles.containsKey(last)){
			PathEdge oldEdge = pathTiles.get(last);
			
			PathNode parent = oldEdge.child;
			if (parent != null && parent.position.equals(last)){
				newEdge.parent = parent;
				diff.addEdge(newEdge);
			} else {
				// make new branch
				PathNode branch = new PathNode(last);
				PathEdge bottomEdgeHalf = new PathEdge();
				PathEdge topEdgeHalf = new PathEdge();
				bottomEdgeHalf.parent = branch;
				topEdgeHalf.parent = oldEdge.parent;
				bottomEdgeHalf.house = oldEdge.house;
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
					if (pathPiece.equals(last)){
						topEdgeHalf.path.add(pathPiece);
						bottomEdgeHalf.path.add(pathPiece);
						reachedBranch = true;
					} else if (reachedBranch){
						topEdgeHalf.path.add(pathPiece);
					} else {
						bottomEdgeHalf.path.add(pathPiece);
					}
				}
				diff.addEdge(newEdge);
				diff.addEdge(bottomEdgeHalf);
				diff.addEdge(topEdgeHalf);
				diff.removeEdge(oldEdge);
			}
		} else {
			System.out.println("Path does not connect with tree"+last);
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
		TreeDiff removeDiff = new TreeDiff(this);
		Set<PathEdge> orphans = new HashSet<>();
		Set<PathEdge> removed = new HashSet<>();
		
		for (Tile t : exclude){
			if (!pathTiles.containsKey(t)) continue; // Ignore tiles not part of paths
			PathEdge cutEdge = pathTiles.get(t);
			if (removed.contains(cutEdge)) continue; // Don't process edges twice
			removeDiff.removeEdge(cutEdge);
			
			// Removing a node from the tree
			if (t.equals(cutEdge.child)) {
				// Remove the node from orphans
				removed.add(cutEdge);
				orphans.remove(cutEdge);
				// Remove all child edges
				for (PathEdge childEdge : cutEdge.child.children){
					if (orphans.contains(childEdge)) continue; 
					removeDiff.removeEdge(childEdge);
					orphans.add(childEdge);
				}
			} else {
				orphans.add(cutEdge);	
			}
		}
		
		// Attempt to reattach orphans
		boolean reattachFailed = false;
		TreeDiff reattachDiff = new TreeDiff(this);
		do {
			removeDiff.apply(false);
			for (PathEdge orphan : orphans){
				// Generate valid path for this orphan
				List<Tile> path = null;
				// Check if edge connects to house
				if (orphan.child != null){
					path = findPath(orphan.child.position, exclude, rewriteTarget, neighbourFn);
				} else {
					for (Tile door : orphan.house.doors){
						path = findPath(door, exclude, rewriteTarget, neighbourFn);
						if (path != null) break;
					}
				}
				if (path != null){
					TreeDiff orphanDiff;
					if (orphan.child != null){
						orphanDiff = checkAddPath(path, orphan.child);
					} else {
						orphanDiff = checkAddPath(path, orphan.house);
					}
					orphanDiff.apply(false);
					reattachDiff.compose(orphanDiff);
				} else {
					reattachFailed = true;
					break;
				}
			}
			reattachDiff.revert(false);
			if (reattachFailed){
				removeDiff.revert(false);
				if (!decayOrphans(orphans, removeDiff)) {
					break;
				}
				reattachDiff.clear();
			}
		} while (reattachFailed);
		
		if (reattachFailed) return null;
		
		removeDiff.revert(false);
		removeDiff.compose(reattachDiff);
		return removeDiff;
	}
	
	/**
	 * Replaced orphaned PathNodes with their children
	 * @param orphans Set of orphaned PathNodes to be reconnected to the tree
	 * @return false if decay failed (no orphan decay was possible)
	 */
	private static boolean decayOrphans(Set<PathEdge> orphans, TreeDiff diff){
		LinkedList<PathEdge> decayed = new LinkedList<>();
		boolean changed = false;
		for (PathEdge pn : orphans){
			if (pn.child == null || pn.child.children == null){
				decayed.add(pn);
			} else {
				for (PathEdge child: pn.child.children){
					diff.removeEdge(child);
					if (child.child != null) {
						changed = true;
						decayed.add(child);
					}
				}
			}
		}
		orphans.clear();
		orphans.addAll(decayed);
		return (changed);
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
		else if (exclude == null) return true;
		else if (exclude.contains(target)) return false;
		else {
			PathEdge edge = pathTiles.get(target);
			while (edge != null){
				if (edge.parent.equals(root)) return true;
				for (Tile currTile : edge.path){
					if (currTile.equals(target)){
						if (exclude.contains(currTile)) return false;
					}
				}
				edge = edge.parent.parent;
			}
			return false;
		}
	}
	
	public List<Tile> findPath(Tile start, Set<Tile> exclude, PathTarget pathTarget, 
			PathNeighbour neighbourFn){
		List <Tile> l = null;
		pathTarget.setExclude(exclude);
		neighbourFn.setExclude(exclude);
		pather.newPath(start, root.position, pathTarget, neighbourFn, exclude);
		try {
			while (!pather.generatePath());
		} catch (PathException e) {
			pather.clear();
			return null;
		}
		l = pather.getPath();
		
		if (l != null){
			l.add(0, start);
		}
		return l;
	}
	
}
