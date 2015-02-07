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
		//spine.add(new SpinePoint(x, y, SpineType.WELL));
		pather = new PathFinder<Tile>();
	}
	
	public void grow(){
		SpinePoint sp;
		int diffX, diffY,w,h;
		int attempts = 0;
		do {
			sp = getRandomWell();
			diffX = rand.nextInt(50);
			diffY = rand.nextInt(50);
			w = rand.nextInt(15)+5;
			h = rand.nextInt(15)+5;
			if (attempts++ > 100) break;
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
				while (pather.generatePath());
			} catch (PathException e) {
				continue;
			}
			pather.getPath();
			break;
		}
		return l;
	}
	
	public void addSpine(SpineType type){
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
	
//	public void addSpine(int gridX, int gridY, SpineType type){
//		SpinePoint newSp = new SpinePoint(gridX, gridY, type);
//		if (findPathToSpine(newSp.getTile(), null){
//			
//		}
//	}
	
	public boolean createHouse(int x, int y, int width, int height){
		House h = new House(new Rectangle(x, y, width, height));
		int currX, currY;
		if (!checkHouse(h)) return false;
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
	 * @param x GridX
	 * @param y GridY
	 * @param width In tiles
	 * @param height In tiles
	 * @return true if the house can be placed at this point
	 */
	public boolean checkHouse(House h){
		HashSet<Tile> exclude = new HashSet<>();
		int currX, currY;
		Tile t;
		for (int i = 0; i < h.getRect().getWidth(); i++){
			for (int j = 0; j < h.getRect().getHeight(); j++){
				currX = h.getRect().getX()+i;
				currY = h.getRect().getY()+j;
				t = Game.getMap().getGridTile(currX, currY);
				if (!t.isWalkable()) return false;
				
				// Is an outer wall
				if (i == 0 || j == 0 || j == h.getRect().getHeight()-1 
						|| i == h.getRect().getWidth()-1){
					exclude.add(t); // Exclude from later pathfinding
					if (rand.nextInt(5) == 0 && i != 0 && i != h.getRect().getWidth()-1){
						h.addDoor(t);
					}
				}
						
			}
		}
		
		//Check for connectivity to spine, add result to spine;
		for (Tile door : h.getDoors()){
			findPathToSpine(door, exclude);
		}
		
		return true;
	}
}
