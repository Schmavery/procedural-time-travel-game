package entities.town;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import core.Tile;
import entities.concrete.Path;
import entities.interfaces.Entity.SpecialType;

public class TreeDiff {
	private PathTree tree;
	private boolean applied;
	private HashMap<Tile, PathEdge> newMappings;
	private HashMap<Tile, PathEdge> removedMappings;
	private List<PathEdge> newEdges;
	private List<PathEdge> removedEdges;
	
	public TreeDiff(PathTree tree) {
		this.applied = false;
		this.tree = tree;
		this.newMappings = new HashMap<>();
		this.removedMappings = new HashMap<>();
		this.newEdges = new LinkedList<>();
		this.removedEdges = new LinkedList<>();
	}
	
	public void removeEdge(PathEdge e){
		assert(e.parent != null);
		removedEdges.add(e);
	}

	public void addEdge(PathEdge e){
		assert(e.parent != null);
		newEdges.add(e);
	}
	
	public void removeMapping(Tile key, PathEdge val){
		removedMappings.put(key, val);
	}
	
	public void addMapping(Tile key, PathEdge val){
		if (newMappings.containsKey(key)) System.out.println("Key overlap");
		newMappings.put(key, val);
	}
	
	/**
	 * Apply this diff to the PathTree.  Also handles adding and removing path entities.
	 */
	public void apply(){
		if (applied){
			System.out.println("Can't apply diff twice");
			return;
		}
		applied = true;
		
		// Remove old tile mappings
		for (Tile t: removedMappings.keySet()){
			tree.pathTiles.remove(t);
			t.removeEntityBySpecialType(SpecialType.PATH);
		}
			
		// Add new tile mappings
		tree.pathTiles.putAll(newMappings);
		for (Tile t: newMappings.keySet()){
			Path p = new Path(0,0);
			if (!p.place(t.getGridX(), t.getGridY())){
				System.out.println("Couldn't place path");
			}
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
		
		for (PathEdge edge : removedEdges){
			edge.parent.children.remove(edge);
			if (edge.child != null) edge.child.parent = null;
		}
	}
	
	public void revert(){
		if (!applied){
			System.out.println("Cannot revert an unapplied diff");
			return;
		}
		applied = false;
		
		for (Tile t: newMappings.keySet()){
			tree.pathTiles.remove(t);
			t.removeEntityBySpecialType(SpecialType.PATH);
		}
			
		tree.pathTiles.putAll(removedMappings);
		for (Tile t: removedMappings.keySet()){
			Path p = new Path(0,0);
			if (!p.place(t.getGridX(), t.getGridY())){
				System.out.println("Couldn't place path");
			}
		}
		
		// Add/update edges
		for (PathEdge edge : removedEdges){
			if (edge.child != null){
				edge.child.parent = edge;
			}
			if (edge.parent != null && !edge.parent.children.contains(edge)){
				edge.parent.children.add(edge);
			}
		}
		
		for (PathEdge edge : newEdges){
			edge.parent.children.remove(edge);
			if (edge.child != null) edge.child.parent = null;
		}
		
	}
}