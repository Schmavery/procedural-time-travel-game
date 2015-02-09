package entities.town;

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
import core.path.PathException;
import core.path.PathFinder;
import entities.concrete.Door;
import entities.concrete.Floor;
import entities.concrete.HousePiece;
import entities.concrete.Path;
import entities.interfaces.Entity.SpecialType;
import entities.town.SpinePoint.SpineType;

public class Town {
	LinkedList<SpinePoint> spine;
	PathFinder<Tile> pather;
	LinkedList<House> houses;
	Random rand;
	
	public Town(int x, int y){
		rand = new Random(RandomManager.getSeed("Town"+x+":"+y));
		spine = new LinkedList<>();
		spine.add(new SpinePoint(x, y, SpineType.WELL));
		pather = new PathFinder<Tile>();
	}
	
	public void grow(){
		SpinePoint sp;
		int diffX, diffY,w,h;
		int attempts = 0;
		do {
			sp = getRandomWell();
			diffX = rand.nextInt(100)-50;
			diffY = rand.nextInt(100)-50;
			w = rand.nextInt(15)+5;
			h = rand.nextInt(15)+5;
			
			// TODO: Add new spine point.
			// Upgrade current spine point to dense?
			if (++attempts > 100) {
				System.out.println("Grow aborted");
				break;
			}
		}while (!createHouse(sp.getX() + diffX, sp.getY() + diffY, w, h));
	}
	
	// TODO: Make better...
	public SpinePoint getRandomWell(){
		Collections.shuffle(spine);
		for (SpinePoint sp : spine){
			if (sp.getType() == SpineType.WELL) return sp;
		}
		return null;
	}
	
	
	public List<Tile> findPathToSpine(Tile start, Set<Tile> exclude){
		List <Tile> l = null;
		for (SpinePoint pt : spine) {
			pather.newPath(start, pt.getTile(), exclude);
			try {
				while (!pather.generatePath());
			} catch (PathException e) {
				continue;
			}
			l = pather.getPath();
			break;
		}
		return l;
	}
	
	public void addSpinePoint(SpineType type){
		int x = 0, y = 0;
		switch (type) {
		case OUTER:
			x = rand.nextInt(100)+100;
			y = rand.nextInt(100)+100;
			break;
		case WELL:
			x = rand.nextInt(100)+100;
			y = rand.nextInt(100)+100;
			break;
		}
		spine.add(new SpinePoint(x, y, type));
	}
	
	public boolean createHouse(int x, int y, int width, int height){
		House h = new House(new Rectangle(x, y, width, height));
		System.out.println("Creating "+h);
		int currX, currY;
		if (!checkHouse(h)) {
			System.out.println("Housecheck failed");
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
				if ((i != -1 && j != -1 || j != h.getRect().getHeight() || i != h.getRect().getWidth()) && 
					(i == 0 || j == 0 || j == h.getRect().getHeight()-1 || i == h.getRect().getWidth()-1)){
					exclude.add(t); // Exclude from later pathfinding
					if (rand.nextInt(5) == 0 && i > 0 && i < h.getRect().getWidth()-1){
						h.addDoor(t);
					}
				}
			}
		}
		
		if (h.getDoors().size() == 0) return false;
		
		// Check for overlap with spine points
		//TODO: check for overlap with spine paths
		for (SpinePoint sp: spine){
			if (h.getRect().contains(sp.getX(), sp.getY())) {
				System.out.println("Overlaps with spine");
				return false;
			}
		}

		
		System.out.println("Checking spine connectivity");
		// Check for connectivity to spine, (add result to spine path... TODO)
		List<Tile> path = null;
		for (Tile door : h.getDoors()){
			path = findPathToSpine(door, exclude);
			if (path != null){
				for (Tile pathTile : path){
					if (!pathTile.hasSpecialType(SpecialType.PATH)){
						Path p = new Path(0,0);
						if (!p.place(pathTile.getGridX(), pathTile.getGridY())){
							System.out.println("Couldn't place path");
						}
					}
				}
				break;
			}
		}
		return (path != null);
	}
}
