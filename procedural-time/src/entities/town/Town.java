package entities.town;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.util.Rectangle;

import core.Game;
import core.RandomManager;
import core.Tile;
import entities.concrete.Door;
import entities.concrete.Floor;
import entities.concrete.HousePiece;
import entities.interfaces.Entity.SpecialType;
import entities.town.SpinePoint.SpineType;

public class Town {
	public static enum GrowthStage {INIT, DENSE};
	private GrowthStage stage;
	private SpinePoint well;
	private LinkedList<House> houses;
	private Random rand;
	private int densityCount;
	PathTree pathTree;
	
	public Town(int x, int y){
		stage = GrowthStage.INIT;
		rand = new Random(RandomManager.getSeed("Town"+x+":"+y));
		well = new SpinePoint(x, y, SpineType.WELL);
		pathTree = new PathTree(Game.getMap().getGridTile(x, y));
	}
	
	public void grow(){
		int diffX, diffY,w,h;
		int attempts = 0;
		if (stage.equals(GrowthStage.DENSE)) return;
		if (densityCount > 50) {
			System.out.println("Achieved dense town");
			stage = GrowthStage.DENSE;
			return;
		}
		do {
			diffX = rand.nextInt(100)-50;
			diffY = rand.nextInt(100)-50;
			w = rand.nextInt(15)+5;
			h = rand.nextInt(15)+5;
			
			// Upgrade current spine point to dense?
			if (++attempts > 100) {
				if (stage.equals(GrowthStage.INIT)) densityCount++;
				return;
			}
		}while (!createHouse(well.getX() + diffX, well.getY() + diffY, w, h));
		densityCount = 0;
	}
	
	
	
	public boolean createHouse(int x, int y, int width, int height){
		House h = new House(new Rectangle(x, y, width, height));
		int currX, currY;
		TreeDiff diff = checkHouse(h);
		if (diff == null) {
			return false;
		}
		diff.apply();
		
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
	 * @return null if the house can be placed at this point.  Otherwise, the necessary tree diff.
	 */
	public TreeDiff checkHouse(House h){
		// Check for overlap with spine points
		if (h.getRect().contains(well.getX(), well.getY())) {
//			System.out.println("Overlaps with well");
			return null;
		}
		HashSet<Tile> exclude = new HashSet<>();
		int currX, currY;
		Tile t;
		boolean pathBlocked = false;
		
		// Check one larger than house to prevent direct adjacency.
		for (int i = -1; i < h.getRect().getWidth() + 1; i++){
			for (int j = -1; j < h.getRect().getHeight() + 1; j++){
				currX = h.getRect().getX()+i;
				currY = h.getRect().getY()+j;
				t = Game.getMap().getGridTile(currX, currY);
				
				// Rejection checks
				if (t == null) return null;
				if (t.hasSpecialType(SpecialType.PATH)) pathBlocked = true;
				// If inside defined house boundaries
				if (i > -1 && j > -1 && i < h.getRect().getWidth() && j < h.getRect().getHeight())
					if (!t.isWalkable()) return null;
				
				if (t.hasSpecialType(SpecialType.HOUSE)) return null;
				
				
				// Is an outer wall
				if (i != -1 && j != -1 && j != h.getRect().getHeight() && i != h.getRect().getWidth()){
					exclude.add(t); // Exclude from later pathfinding
					if (i == 0 || j == 0 || j == h.getRect().getHeight()-1 || i == h.getRect().getWidth()-1){
						if (rand.nextInt(10) == 0 && ((i > 0  && i < h.getRect().getWidth()-1)
								|| (j > 0  && j < h.getRect().getHeight()-1))){
							h.addDoor(t);
						}
					}
				}
			}
		}
		
		if (h.getDoors().size() == 0) return null;
		if (pathBlocked && stage.equals(GrowthStage.INIT)) return null;
		else if (pathBlocked && stage.equals(GrowthStage.DENSE)){
			TreeDiff rewriteDiff = pathTree.checkRewrite(exclude);
			if (rewriteDiff == null) return null;
			rewriteDiff.apply();
			TreeDiff houseDiff = pathTree.checkAddHouse(h, exclude);
			rewriteDiff.revert();
			if (houseDiff == null) return null;
			rewriteDiff.compose(houseDiff);
			return rewriteDiff;
		}
		return pathTree.checkAddHouse(h, exclude);
	}
	
	public LinkedList<House> getHouses(){
		return houses;
	}
}
