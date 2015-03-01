package entities.town;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.lwjgl.util.Rectangle;

import core.Game;
import core.RandomManager;
import core.Tile;
import core.path.NeighbourFunction;
import core.path.PathException;
import core.path.PathFinder;
import core.path.TargetFunction;
import entities.concrete.Door;
import entities.concrete.Floor;
import entities.concrete.HousePiece;
import entities.concrete.Path;
import entities.interfaces.Entity.SpecialType;
import entities.town.SpinePoint.SpineType;

public class Town {
	public static enum GrowthStage {INIT, DENSE};
	
	private SpinePoint well;
	private PathFinder<Tile> pather;
	private LinkedList<House> houses;
	private Random rand;
	
	PathTree pathTree;
	
	class PathTarget implements TargetFunction<Tile>{
		List<Tile> exclude = new LinkedList<>();
		public void setExclude(List<Tile> exclude){
			this.exclude = exclude;
		}
		@Override
		public boolean isTarget(Tile target) {
			return pathTree.isFreeOf(target, exclude);
		}
	}
//	PathTarget pathTarget = new PathTarget();
	
	TargetFunction<Tile> pathTarget = new TargetFunction<Tile>() {
		@Override
		public boolean isTarget(Tile target) {
			return pathTree.hasPath(target);
		}
	}; 
	
	NeighbourFunction<Tile> neighourFn = new NeighbourFunction<Tile>() {
		private void addTile(List<Tile> list, int x, int y){
			if (x >= 0
					&& x < Game.getMap().getSize()
					&& y >= 0
					&& y < Game.getMap().getSize()
					&& Game.getMap().getGridTile(x, y).isWalkable()
					&& !Game.getMap().getGridTile(x, y).hasSpecialType(SpecialType.HOUSE)){
				list.add(Game.getMap().getGridTile(x, y));
			}
		}
		@Override
		public List<Tile> getNeighbours(Tile node) {
			List<Tile> reachable = new ArrayList<>(4);
			addTile(reachable, node.getGridX() - 1, node.getGridY());
			addTile(reachable, node.getGridX()    , node.getGridY() - 1);
			addTile(reachable, node.getGridX() + 1, node.getGridY());
			addTile(reachable, node.getGridX()    , node.getGridY() + 1);
			return reachable;
		}
	};
	
	public Town(int x, int y){
		rand = new Random(RandomManager.getSeed("Town"+x+":"+y));
		well = new SpinePoint(x, y, SpineType.WELL);
		pather = new PathFinder<Tile>();
		pathTree = new PathTree(Game.getMap().getGridTile(x, y));
	}
	
	public void grow(){
		SpinePoint sp;
		int diffX, diffY,w,h;
		int attempts = 0;
		do {
			diffX = rand.nextInt(100)-50;
			diffY = rand.nextInt(100)-50;
			w = rand.nextInt(15)+5;
			h = rand.nextInt(15)+5;
			
			// Upgrade current spine point to dense?
			if (++attempts > 10) {
				break;
			}
		}while (!createHouse(well.getX() + diffX, well.getY() + diffY, w, h));
	}
	
	public List<Tile> findPathToSpine(Tile start, Set<Tile> exclude){
		List <Tile> l = null;
		pather.newPath(start, well.getTile(), pathTarget, neighourFn, exclude);
		try {
			while (!pather.generatePath());
		} catch (PathException e) {
			pather.clear();
			return null;
		}
		l = pather.getPath();
		return l;
	}
	
	public boolean createHouse(int x, int y, int width, int height){
		House h = new House(new Rectangle(x, y, width, height));
		int currX, currY;
		if (!checkHouse(h)) {
			return false;
		}
		for (int i = 0; i < h.getRect().getWidth(); i++){
			for (int j = 0; j < h.getRect().getHeight(); j++){
				currX = h.getRect().getX()+i;
				currY = h.getRect().getY()+j;
				
				if (j != 0) (new Floor(0, 0)).place(currX, currY);
				
				if (i == 0 || j == 0 || j == h.getRect().getHeight()-1 || i == h.getRect().getWidth()-1) {
					if (h.getDoors().contains(Game.getMap().getGridTile(currX, currY))){
						(new Door(0, 0)).place(currX, currY);
					} else {
						(new HousePiece(0, 0)).place(currX, currY);
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if a house can be placed here.  This has side effects of filling
	 * the house with more precise data about its placement (doors and spine path).
	 * 
	 * Factors in correct house placement:
	 * 	- A house must be entirely on "walkable" terrain
	 *  - A house cannot be inside another house
	 *  - A house cannot be directly adjacent to another house
	 * 
	 * @param h A house initialized with a position rectangle
	 * @return true if the house can be placed at this point
	 */
	public boolean checkHouse(House h){
		// Check for overlap with spine points
		if (h.getRect().contains(well.getX(), well.getY())) {
			System.out.println("Overlaps with well");
			return false;
		}
		HashSet<Tile> exclude = new HashSet<>();
		int currX, currY;
		Tile t;
		
		// Check one larger than house to prevent adjacency.
		for (int i = -1; i < h.getRect().getWidth() + 1; i++){
			for (int j = -1; j < h.getRect().getHeight() + 1; j++){
				currX = h.getRect().getX()+i;
				currY = h.getRect().getY()+j;
				t = Game.getMap().getGridTile(currX, currY);
				// Rejection check
				if (t == null) return false;
				if (t.hasSpecialType(SpecialType.PATH)) return false;
				// If inside defined house boundaries
				if (i > -1 && j > -1 && i < h.getRect().getWidth() && j < h.getRect().getHeight())
					if (!t.isWalkable()) return false;
				
				if (t.hasSpecialType(SpecialType.HOUSE)) return false;
				
				// Is an outer wall
				if ((i != -1 && j != -1 && j != h.getRect().getHeight() && i != h.getRect().getWidth()) && 
					(i == 0 || j == 0 || j == h.getRect().getHeight()-1 || i == h.getRect().getWidth()-1)){
					exclude.add(t); // Exclude from later pathfinding
					if (rand.nextInt(10) == 0 && ((i > 0  && i < h.getRect().getWidth()-1)
							|| (j > 0  && j < h.getRect().getHeight()-1))){
						h.addDoor(t);
					}
				}
			}
		}
		
		if (h.getDoors().size() == 0) return false;

		// Check for connectivity to spine, (add result to spine path... TODO)
		List<Tile> path = null;
		for (Tile door : h.getDoors()){
			path = findPathToSpine(door, exclude);
			if (path != null){
				break;
			}
		}
		
		if (path == null) {
			System.out.println("No path");
			return false;
		}

		pathTree.addPath(path);
		
		for (Tile pathTile : path){
			if (!pathTile.hasSpecialType(SpecialType.PATH)){
				Path p = new Path(0,0);
				if (!p.place(pathTile.getGridX(), pathTile.getGridY())){
					System.out.println("Couldn't place path");
				}
			}
		}
		
		return true;
	}
}
