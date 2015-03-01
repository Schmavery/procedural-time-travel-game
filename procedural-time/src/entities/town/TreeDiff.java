package entities.town;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import core.Tile;
import entities.town.PathTree.PathEdge;
import entities.town.PathTree.PathNode;

public class TreeDiff {
	private PathTree tree;
	private boolean applied;
	private HashMap<Tile, PathEdge> newMappings;
	private List<Tile> removedMappings;
	private List<PathEdge> newEdges;
	private Map<PathNode, List<PathEdge>> removedEdges;
	
	public TreeDiff(PathTree tree) {
		this.applied = false;
		this.tree = tree;
		this.newMappings = new HashMap<>();
		this.removedMappings = new LinkedList<>();
		this.newEdges = new LinkedList<>();
		this.removedEdges = new HashMap<>();
	}
	
	public void removeEdge(PathEdge e){
		assert(e.parent != null);
		if (!removedEdges.containsKey(e.parent)){
			removedEdges.put(e.parent, new LinkedList<PathEdge>());
		}
		removedEdges.get(e.parent).add(e);
	}

	public void addEdge(PathEdge e){
		newEdges.add(e);
	}
	
	public void removeMapping(Tile key, PathEdge val){
		removedMappings.add(key);
	}
	
	public void addMapping(Tile key, PathEdge val){
		newMappings.put(key, val);
	}
	
	public void apply(){
		if (applied){
			System.out.println("Can't apply diff twice");
			return;
		}
		applied = true;
		// Add new tile mappings
		tree.pathTiles.putAll(newMappings);
		
		// Remove old tile mappings
		for (Tile t: removedMappings){
			tree.pathTiles.remove(t);
		}
		
		// Add/update edges
		for (PathEdge edge : newEdges){
			if (edge.child != null){
				edge.child.parent = edge;
			}
			if (edge.parent != null && !edge.parent.children.contains(edge)){
				edge.parent.children.add(edge);
			}
		}
		
		// Remove children (edges)
		for (Entry<PathNode, List<PathEdge>> entry : removedEdges.entrySet()){
			entry.getKey().children.removeAll(entry.getValue());
		}
	}
}