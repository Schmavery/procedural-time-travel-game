package entities.town;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import core.Tile;
import entities.concrete.Path;
import entities.interfaces.Entity.SpecialType;

public class TreeDiff {
	private PathTree tree;
	private HashMap<Tile, PathEdge> newMappings;
	private HashMap<Tile, PathEdge> removedMappings;
	private List<PathEdge> newEdges;
	private List<PathEdge> removedEdges;
	private TreeDiff next;
	
	public TreeDiff(PathTree tree) {
		this.tree = tree;
		this.newMappings = new HashMap<>();
		this.removedMappings = new HashMap<>();
		this.newEdges = new LinkedList<>();
		this.removedEdges = new LinkedList<>();
		this.next = null;
	}
	
	public void removeEdge(PathEdge e){
		if (e == null || e.parent == null){
			System.out.println("Bad edge:"+e);
			return;
		}
		if (!removedEdges.contains(e)){
			removedEdges.add(e);
			Tile last = null;
			Tile first = e.path.get(0);
			for (Tile t : e.path){   // Perform this for all but the last tile
				if (last != null) {
					if (t.equals(first) && e.house != null) continue;
					if (last.getGridX() == 500 && last.getGridY() == 510) throw new RuntimeException();
					removeMapping(last, e);
				}
				last = t;
			}
		}
	}

	public void addEdge(PathEdge e){
		if (e == null || e.parent == null){
			System.out.println("Bad edge:"+e);
			return;
		}
		if (!newEdges.contains(e)){
			newEdges.add(e);
			Tile last = null;
			Tile first = e.path.get(0);
			for (Tile t : e.path){   // Perform this for all but the last tile
				if (t.equals(first) && e.house != null) continue;
				if (last != null) {
					addMapping(last, e);
				}
				last = t;
			}
		}
	}
	
	private void removeMapping(Tile key, PathEdge val){
		removedMappings.put(key, val);
	}
	
	private void addMapping(Tile key, PathEdge val){
		newMappings.put(key, val);
	}
	
	/**
	 * Apply this diff to the PathTree.  Also handles adding and removing path entities.
	 */
	public void apply(boolean updateMap){
		// Remove old tile mappings
		for (Tile t: removedMappings.keySet()){
			if (tree.pathTiles.containsKey(t)){
				tree.pathTiles.remove(t);
				if (updateMap) t.removeEntityBySpecialType(SpecialType.PATH);
			}
		}
		
		// Add new tile mappings
		tree.pathTiles.putAll(newMappings);
		if (updateMap){
			for (Tile t: newMappings.keySet()){
				Path p = new Path(0,0);
				if (!p.place(t.getGridX(), t.getGridY())){
					System.out.println("Couldn't place path");
				}
			}
		}
		
		for (PathEdge edge : removedEdges){
			edge.parent.children.remove(edge);
			if (edge.child != null) edge.child.parent = null;
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
		if (next != null) next.apply(updateMap);
	}
	
	public void revert(boolean updateMap){
		if (next != null) next.revert(updateMap);
		
		for (Tile t: newMappings.keySet()){
			if (tree.pathTiles.containsKey(t)){
				tree.pathTiles.remove(t);
				if (updateMap) t.removeEntityBySpecialType(SpecialType.PATH);
			}
		}
			
		tree.pathTiles.putAll(removedMappings);
		if (updateMap){
			for (Tile t: removedMappings.keySet()){
				Path p = new Path(0,0);
				if (!p.place(t.getGridX(), t.getGridY())){
					System.out.println("Couldn't place path");
				}
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
	
	/**
	 * Composes two diffs so that they can be applied together
	 * Note: This can result in long edge lists
	 * @param diff Second diff in the sequence
	 */
	public void compose(TreeDiff diff){
		if (next != null){
			next.compose(diff);
		} else {
			next = diff;
		}
	}
	
	/**
	 * Wipes diff clean of changes.  Keep pathtree reference.
	 */
	public void clear(){
		newMappings.clear();
		removedMappings.clear();
		newEdges.clear();
		removedEdges.clear();
		next = null;
	}
	
	@Override
	public String toString(){
		int removed = 0, added = 0;
		for (TreeDiff td = this; td != null; td = td.next){
			removed += td.removedEdges.size();
			added   += td.newEdges.size();  
		}
		return "TreeDiff:new("+added+"),remove("+removed+")";
	}
}