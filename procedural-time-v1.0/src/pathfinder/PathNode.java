package pathfinder;

public class PathNode {
	private int x,y,heur,pathLen;
	PathNode parent;
	int pathWeight=1;
	
	//if parent is provided, calculates algorithm using manhattan dist.  Else, heuristic is 0.
	public PathNode(int x, int y, PathNode parent, PathNode dest){
		this.x = x;
		this.y = y;
		this.parent = parent;
		if(parent!=null){
			this.pathLen = parent.pathLen + pathWeight;
		} else {
			pathLen = 0;
		}
		if(dest != null){
			heur = manhattanHeur(x, y, dest);
		} else {
			heur = 0;
		}
	}
	
	private int manhattanHeur(int x, int y, PathNode dest){
		int heur = (Math.abs(x-dest.getX())*pathWeight + Math.abs(y - dest.getY())*pathWeight);
		return heur;
	}
	
	// getters
	public int getX(){return x;}
	public int getY(){return y;}
	public int getHeur(){return heur;}
	public int getPathLength(){return pathLen;}
	
}
