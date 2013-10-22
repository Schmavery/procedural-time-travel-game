package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathFinder {
	List<PathNode> open, closed;
	Pathable target;
	PathNode finalNode;
	
	private class PathNode implements Comparable<PathNode>{
		private Pathable node;
		private PathNode parent;
		private int cost;
		private int total;
		
		PathNode(Pathable n, PathNode p) {
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
	
	public PathFinder(Pathable start, Pathable target){
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
	
	public List<Pathable> getPath(){
		// Generate returnable path
		return null;
	}
	
	/**
	 * This could have many different implementations.  To keep it simple,
	 * this first try will use a sorted ArrayList.
	 * @return Whether or not the calculation is finished.
	 */
	private boolean calcPath(){
		// Find node with lowest total cost.
		PathNode node = open.remove(0);

		// Add all neighbouring nodes to open
		for (Pathable p : node.node.getReachable()){
			PathNode tmp = new PathNode(p, node);
			int index = Collections.binarySearch(open, tmp);
			if (index > 0){
				open.get(index).newParent(node);
			} else {
				open.add(-(index + 1), tmp);
			}
		}
		//Collections.sort(open);
		
		// Close node
		closed.add(node);
		if (node.node.equals(target)){
			finalNode = node;
			return true;
		}
		return false;
	}
}
