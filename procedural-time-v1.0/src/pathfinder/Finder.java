package pathfinder;

import java.util.LinkedList;

import core.TileMap;

public class Finder {
	private TileMap tileMap;
	
	public Finder(TileMap tileMap){
		this.tileMap = tileMap;
	}
	
	public int[] find(int startX, int startY, int destX, int destY){
		LinkedList<PathNode> openNodes = new LinkedList<PathNode>();
		LinkedList<PathNode> closedNodes = new LinkedList<PathNode>();
		
		int[] xMod = {1,0,-1,0};
		int[] yMod = {0,1,0,-1};
		
		PathNode destNode = new PathNode(destX, destY, null, null);
		PathNode startNode = new PathNode(startX, startY, null, destNode);
		openNodes.add(startNode);
		
		//start looping through some stuff.
		for(int i = 0; i<1000; i++){
			PathNode smallNode = (openNodes).get(0);
			for (int j = 0; j < openNodes.size(); j++){
				if (cost(openNodes.get(j)) < cost(smallNode)){
					smallNode = openNodes.get(j);
				}
			}
			
			// add some stuff to open...
			for (int j = 0; j < xMod.length; j++){
				int newXCoord = smallNode.getX() + xMod[j];
				int newYCoord = smallNode.getY() + yMod[j];
				if (tileMap.isWalkable(newXCoord, newYCoord)){
					openNodes.add(new PathNode(newXCoord, newYCoord, smallNode, destNode));
				}
			}	
			
			// add some stuff to closed... 
			closeNode(smallNode, openNodes, closedNodes);
			// check if we're at the destination...
			if (smallNode.getX() == destNode.getX() && smallNode.getY() == destNode.getY()){
				return getPath(closedNodes, startNode, destNode);
			}
			
		}
		
		return null;
	}
	
	private int[] getPath(LinkedList<PathNode> closedNodes, PathNode start, PathNode dest){
		LinkedList<Integer> pathList = new LinkedList<Integer>();
		
		PathNode currNode = dest;
		while(currNode != start){
			int deltaX = currNode.getX() - currNode.parent.getX(); 
			int deltaY = currNode.getY() - currNode.parent.getY(); 
			int dir = 0;
			if (deltaX == 0 && deltaY == -1){
				dir = 1;
			} else if (deltaX == 1 && deltaY == 0){
				dir = 2;
			} else if (deltaX == 0 && deltaY == 1){
				dir = 3;
			} else if (deltaX == -1 && deltaY == 0){
				dir = 4;
			} 
			
			pathList.addFirst(dir);
			
			currNode = currNode.parent;
		}
		
		int[] path = new int[pathList.size()];
		for (int i = 0; i < path.length; i++){
			path[i] = pathList.removeFirst();
		}
		
		return path;
	}
	
	private void closeNode(PathNode closeThisNode, LinkedList<PathNode> openNodes, LinkedList<PathNode> closedNodes){
		for (int i = 0; i < openNodes.size(); i++){
			if (openNodes.get(i) == closeThisNode){
				closedNodes.add(openNodes.get(i));
				openNodes.remove(i);
				break;
			}
		}
	}
	
	private int cost(PathNode node){
		int cost = node.getHeur() + node.getPathLength();
		return cost;
	}
}
