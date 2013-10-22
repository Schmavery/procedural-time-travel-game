package core;

import java.util.List;

public interface Pathable<T> {
	/**
	 * Calculates the heuristic distance between this node and the specified node.
	 * @param p - The target node
	 * @return The heuristic distance between this and the target.
	 */
	public int heuristic(T p);
	
	/**
	 * Generates a list of all nodes reachable from the current node.
	 * @return a list of reachable nodes
	 */
	public List<T> getReachable();
	
	/**
	 * Checks if this node is the same as the specified node.
	 * @param p - The node to be checked.
	 * @return true or false as this node is the same or different from the specified node.
	 */
	public boolean isSameNode(T p);
	
	/**
	 * Calculates the cost to move from this node to the specified node.
	 * It is assumed that the specified node is reachable, otherwise behaviour is undefined.
	 * 
	 * @param p - Object being moved to.
	 * @return The cost of moving from this node to the specified node.
	 */
	public int moveCost(T p);
}
