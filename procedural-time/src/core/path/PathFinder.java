package core.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PathFinder<T extends Pathable<T>> {
	List<PathNode> open, closed;
	T target;
	PathNode finalNode;
	List<T> path;
	boolean running = false;
	
	private class PathNode implements Comparable<PathNode>{
		private T node;
		private PathNode parent;
		private int cost = 0;
		private int total;
		
		PathNode(T n, PathNode p) {
			this.node = n;
			this.parent = p;
			if (p != null)
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
				this.total = newCost + node.heuristic(target);
				this.parent = p;
				return true;
			}
			return false;
		}

		public int compareTo(PathNode p) {
			return this.total - p.total;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object o){
			if (o == null)
				return false;
			else if (o instanceof PathFinder.PathNode){
				return this.node.equals(((PathFinder.PathNode) o).node);
			}
			return false;
		}
		
		@Override
		public int hashCode(){
			return node.hashCode();
		}
	}
	
	public PathFinder(){
		open = new ArrayList<PathNode>();
		closed = new ArrayList<PathNode>();
	}
	
	public PathFinder(T start, T target){
		open = new ArrayList<PathNode>();
		closed = new ArrayList<PathNode>();
		newPath(start, target);
	}

	public void clear(){
		this.open.clear();
		this.closed.clear();
		this.finalNode = null;
		this.target = null;
		path = null;
		running = false;
	}
	
	public void newPath(T start, T target){
		this.target = target;
		PathNode startNode = new PathNode(start, null);
		open.add(startNode);
		running = true;
	}
	
	/**
	 * 
	 * @param steps Number of steps to run the algorithm.  If steps <= 0, run to completion.
	 * @return Whether the path finished generation
	 * @throws PathException on timeout
	 */
	public boolean generatePath(int steps) throws PathException{
		if (steps <= 0){
			long startTime = System.currentTimeMillis();
			while (!calcPath()){ /* Run algorithm to completion*/ 
				if (System.currentTimeMillis() - startTime > 10) throw new PathException("Path timeout");
			}
			path = makePathList();
			return true;
		} else {
			// Give the algorithm another few steps to run.
			// If it finishes early/on time, return true.
			for (int i = 0; i < steps; i++){
				if (calcPath()){
					path = makePathList();
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
	private List<T> makePathList(){
//		System.out.println("Making path list");
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
	
	public List<T> getPath(){
		if (path == null){
			makePathList();
		}
		return path;
	}
	
	public boolean isEmpty(){
		return (path == null || path.isEmpty());
	}
	
	public boolean hasNext(){
		return (path != null && path.size() > 0);
	}
	
	public T currNode(){
		if (path == null || path.isEmpty()){
			return null;
		} else {
			return path.get(0);
		}
	}
	
	public T nextNode(){
		if (hasNext()){
			return path.remove(0);
		} else {
			clear();
			return null;
		}
	}
	
	/**
	 * This could have many different implementations.  To keep it simple,
	 * this first try will use a sorted ArrayList.
	 * @return true if the calculation is finished, false otherwise
	 * @throws PathException 
	 * @throws Exception 
	 */
	private boolean calcPath() throws PathException{
		// Find node with lowest total cost.
		if (open.isEmpty()){
			throw new PathException("Empty open list. " + target.toString());
		}
		PathNode node = open.remove(0);
		if (node.node.equals(target)){
			finalNode = node;
			running = false;
			return true;
		}

		// Add all neighbouring nodes to open
		for (T p : node.node.getReachable()){
			PathNode tmp = new PathNode(p, node);
			int index;
			if ((index = open.indexOf(tmp)) > -1){
				open.get(index).newParent(tmp);
			} else {
				if (closed.indexOf(tmp) > -1){
					continue;
				}
//				System.out.println("new node");
				index = Collections.binarySearch(open, tmp);
				if (index >= 0){
					open.add(index, tmp);
				} else {
					open.add(-(index + 1), tmp);
				}
			}
			
		}
		// Close node
		closed.add(node);
		return false;
	}
	
	public boolean isRunning(){
		return running;
	}
	
}
