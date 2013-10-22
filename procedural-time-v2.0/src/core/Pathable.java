package core;

import java.util.LinkedList;

public interface Pathable {
	public int heuristic(Pathable p);
	public LinkedList<Pathable> getReachable();
	public boolean isSameNode(Pathable p);
	public int moveCost(Pathable p);
}
