package entities.town;

import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.util.Point;

import core.Game;
import core.RandomManager;
import core.Tile;
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
		// TODO: check connectivity, generate appropriate locations.
		spine.add(new SpinePoint(x, y, SpineType.OUTER));
		spine.add(new SpinePoint(x, y, SpineType.WELL));
		pather = new PathFinder<Tile>();
	}
	
	// TODO: Trying Futures
	public void findPathToSpine(Tile start){
		for (SpinePoint pt : spine){
			pather.newPath(start, pt.getTile());
			pather.getPath();
			pather.clear();
		}
	}
	
	// TODO: Option for WELL or OUTER, with appropriate generated locations
	// and checking for connectivity.
	public void addSpine(int gridX, int gridY){
		spine.add(new SpinePoint(gridX, gridY, SpineType.OUTER));
	}
	
	public boolean createHouse(int x, int y, int width, int height){
		if (!checkHouse(x, y, width, height)) return false;
		
		House h = new House(x, y, width, height);
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				if (j != 0)
					(new Floor(0, 0)).place(x+i, y+j);
				if (i == 0 || j == 0 || j == height-1 || i == width-1){
					if (rand.nextInt(5) == 0 && i != 0 && i != width-1){
						(new Door(0, 0)).place(x+i, y+j);
						h.addDoor(new Point(x+i, y+j));
					}
					else
						(new HousePiece(0, 0)).place(x+i, y+j);
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if a house can be placed here
	 * @param x GridX
	 * @param y GridY
	 * @param width In tiles
	 * @param height In tiles
	 * @return true if the house can be placed at this point
	 */
	public static boolean checkHouse(int x, int y, int width, int height){
		//TODO: Check for connectivity to spine
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				if (!Game.getMap().getGridTile(x+i, y+j).isWalkable()) return false;
			}
		}
		return true;
	}
}
