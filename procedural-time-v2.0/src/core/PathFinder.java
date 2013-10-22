package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PathFinder<T extends Pathable<T>> {
	List<PathNode> open, closed;
	T target;
	PathNode finalNode;
	
	private class PathNode implements Comparable<PathNode>{
		private T node;
		private PathNode parent;
		private int cost;
		private int total;
		
		PathNode(T n, PathNode p) {
			this.node = n;
			this.parent = p;
			this.cost = parent.cost + parent.node.moveCost(n);
			if (target != null){
				total = cost + n.heuristic(target);
			} else {
				total = cost;
			}	
		}
		
		private boolean newParent(PathNode p){
			int newCost = p.cost + p.node.moveCost(node);
			if (newCost < cost){
				this.cost = newCost;
				this.parent = p;
				return true;
			}
			return false;
		}

		public int compareTo(PathNode p) {
			return this.total - p.total;
		}
		
	}
	
	public PathFinder(T start, T target){
		newPath(start, target);
	}

	public void clear(){
		this.open.clear();
		this.closed.clear();
		this.finalNode = null;
		this.target = null;
	}
	
	public void newPath(T start, T target){
		this.target = target;
		PathNode startNode = new PathNode(start, null);
		open = new ArrayList<PathNode>();
		closed = new ArrayList<PathNode>();
		open.add(startNode);
	}
	
	public boolean generatePath(int steps){
		if (steps > 0){
			while (!calcPath()){ /* Run algorithm to completion*/ }
			return true;
		} else {
			// Give the algorithm another few steps to run.
			// If it finishes early/on time, return true.
			for (int i = 0; i < steps; i++){
				if (calcPath()){
					return true;
				}
			}
			// Else, return false.
			return false;
		}
	}
	
	/**
	 * Generate and return path to target, or null if one doesn't exist.
	 * @return The path to the target, or null if one doesn't exist.
	 */
	public List<T> getPath(){
		if (finalNode == null){
			return null;
		}
		PathNode tmpNode = finalNode;
		LinkedList<T> path = new LinkedList<T>();
		while (tmpNode.parent != null){
			path.offerFirst(tmpNode.node);
			tmpNode = tmpNode.parent;
		}
		return path;
	}
	
	/**
	 * This could have many different implementations.  To keep it simple,
	 * this first try will use a sorted ArrayList.
	 * @return true if the calculation is finished, false otherwise
	 */
	private boolean calcPath(){
		// Find node with lowest total cost.
		PathNode node = open.remove(0);

		// Add all neighbouring nodes to open
		for (T p : node.node.getReachable()){
			PathNode tmp = new PathNode(p, node);
			int index = Collections.binarySearch(open, tmp);
			if (index > 0){
				open.get(index).newParent(node);
			} else {
				open.add(-(index + 1), tmp);
			}
		}
		
		// Close node
		closed.add(node);
		if (node.node.equals(target)){
			finalNode = node;
			return true;
		}
		return false;
	}
}
