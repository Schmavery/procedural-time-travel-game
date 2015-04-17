package entities.town;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.lwjgl.util.Rectangle;

import core.Game;
import core.RandomManager;
import core.Tile;
import core.util.GraphWriter;
import core.util.GraphWriter.GraphType;
import entities.concrete.Door;
import entities.concrete.Floor;
import entities.concrete.HousePiece;
import entities.interfaces.Entity.SpecialType;
import entities.town.House.HouseType;
import entities.town.SpinePoint.SpineType;

public class Town {
	private static final int HOUSE_SIZE = 15, HOUSE_RANGE = 5;
	public static enum GrowthStage {INIT, DENSE};
	public GrowthStage stage;
	private SpinePoint well;
	private LinkedList<House> houses;
	private Random rand;
	private int densityCount;
	PathTree pathTree;
	private int[] numHouseTypes;
	
	public Town(int x, int y){
		stage = GrowthStage.INIT;
		rand = new Random(RandomManager.getSeed("Town1"+x+":"+y));
		well = new SpinePoint(x, y, SpineType.WELL);
		pathTree = new PathTree(Game.getMap().getGridTile(x, y));
		houses = new LinkedList<>();
		numHouseTypes = new int[HouseType.values().length];
	}
	
	public void grow(){
		int diffX, diffY,w,h;
		int attempts = 0;
		if (densityCount > 50 && !stage.equals(GrowthStage.DENSE)) {
			System.out.println("Achieved dense town");
			stage = GrowthStage.DENSE;
		}
		if (houses.size() > 2 && rand.nextInt(10) == 1){
			// House Function Swap
			swapHouses();
		} else {
			// Create New House
			do {
				int range = calcRange();
				double magnitude = Math.pow(rand.nextDouble(), 3)*range;
				double angle = rand.nextDouble()*2*Math.PI;
				diffX = (int) (Math.cos(angle)*magnitude);
				diffY = (int) (Math.sin(angle)*magnitude);
				
				w = rand.nextInt(HOUSE_SIZE) + HOUSE_RANGE;
				h = rand.nextInt(HOUSE_SIZE) + HOUSE_RANGE;
				
				if (++attempts > 100) {
					if (stage.equals(GrowthStage.INIT)) densityCount++;
					return;
				}
			}while (!createHouse(well.getX() + diffX, well.getY() + diffY, w, h));
			densityCount = 0;
		}
		GraphWriter.log(GraphType.CLUSTER, String.valueOf((evaluateCluster())));

	}
	
	private int calcRange(){
		return (int) Math.sqrt(houses.size())*6 + 10;
	}
	
	public float evaluateCluster(){
		float total = 0;
		for (House tmpH : houses){
			total += evaluateHouse(tmpH);
		}
		return (total/houses.size());
	}
	
	private float evaluateHouse(House h){
		float percent = 0;
		Set<House> nearby = getNearbyHouses(h);
		for (House cmpH : nearby){
			if (cmpH.getType().equals(h.getType())) percent++;
		}
		return (percent/nearby.size());
	}
	
	private HouseType getMostFrequentNearbyType(House h){
		Set<House> nearby = getNearbyHouses(h);
		int max = 0;
		HouseType argMax = h.getType();
		int count;
		for (HouseType ht : HouseType.values()){
			count = 0;
			for (House tmpH : nearby){
				if (tmpH.getType().equals(ht)){
					count++;
				}
			}
			if (count > max){
				max = count;
				argMax = ht;
			}
		}
		return argMax;
	}
	
	/**
	 * Returns a set of all houses within dist of House h.
	 * Partially contained houses are considered to be valid.
	 * @param h Center house
	 * @param dist Distance from house
	 * @return Set of houses near h
	 */
	private Set<House> getNearbyHouses(House h){
		int dist = 15;
		Set<House> hSet = new HashSet<>();
		Rectangle r = new Rectangle(h.getRect());
		r.setSize(2*dist, 2*dist);
		r.translate(-dist + (h.getRect().getWidth()/2), -dist + (h.getRect().getHeight()/2));
		for (House tmpH : houses){
			if (r.intersects(tmpH.getRect())) hSet.add(tmpH);
		}
		return hSet;
	}

	
	private void swapHouses(){
		// Sort in ascending order of evaluation.
		Collections.sort(houses, new Comparator<House>() {
			@Override
			public int compare(House h1, House h2) {
				float h1P = evaluateHouse(h1);
				float h2P = evaluateHouse(h2);
				if (h1P < h2P) return -1;
				else if (h1P > h2P) return 1;
				else return 0;
			}
		});
		House worstHouse = houses.get(0);
		HouseType type = getMostFrequentNearbyType(worstHouse);
		List<House> swaps = new ArrayList<>(houses.size());
		for (House swapH : houses){
			if (!swapH.getType().equals(worstHouse.getType())){
				if (swapH.getType().equals(type)){
					swaps.add(swapH);
				}
			}
		}

//		Collections.shuffle(swaps);
		float before = evaluateCluster();
		for (House s : swaps){
			if (swapHouses(s, worstHouse, before)) break;
		}
		
	}
	
	private boolean swapHouses(House h1, House h2, float before){
		HouseType type = h1.getType();
		h1.setType(h2.getType());
		h2.setType(type);
		float after = evaluateCluster();
		if (before >= after){
			// Revert
			h2.setType(h1.getType());
			h1.setType(type);
			return false;
		} else {
			h1.swap(h2);
//			System.out.println("Improved Clustering Score: "+after);
			return true;
		}
	}
	
	private boolean createHouse(int x, int y, int width, int height){
		House h = new House(new Rectangle(x, y, width, height), this,
				HouseType.values()[rand.nextInt(HouseType.values().length)]);
		
		int currX, currY;
		TreeDiff diff = checkHouse(h);
		if (diff == null) {
			return false;
		}
		diff.apply(true);
		
		Door d;
		HousePiece hp;
		Floor f;
		for (int i = 0; i < h.getRect().getWidth(); i++){
			for (int j = 0; j < h.getRect().getHeight(); j++){
				currX = h.getRect().getX()+i;
				currY = h.getRect().getY()+j;
				
				if (j != 0) {
					f = new Floor(0, 0);
					f.setHouse(h);
					f.place(currX, currY);
				}
				
				if (i == 0 || j == 0 || j == h.getRect().getHeight()-1 || i == h.getRect().getWidth()-1) {
					if (h.getDoors().contains(Game.getMap().getGridTile(currX, currY))){
						d = new Door(0, 0);
						d.setHouse(h);
						d.place(currX, currY);
					} else {
						hp = new HousePiece(0, 0);
						hp.setHouse(h);
						hp.place(currX, currY);
					}
				}
			}
		}
		houses.add(h);
		
		// Either set to the the most frequent or the least common
		if (rand.nextBoolean()){
			h.setType(getMostFrequentNearbyType(h));
		} else {
			h.setType(getLeastCommonHouseType());
		}
		h.spawnPerson();
		swapHouses(); // Check if you can recolour another house instead
		
		numHouseTypes[h.getType().ordinal()]++;
		//System.out.println("Added house #"+houses.size()+", new cluster: "+evaluateCluster());
		return true;
	}
	
	private HouseType getMostCommonHouseType(){
		HouseType argMax = null;
		int max = -1;
		for (HouseType ht : HouseType.values()){
			if (numHouseTypes[ht.ordinal()] > max){
				argMax = ht;
				max = numHouseTypes[ht.ordinal()];
			}
		}
		return argMax;
	}
	
	private HouseType getLeastCommonHouseType(){
		HouseType argMin = null;
		int min = Integer.MAX_VALUE;
		for (HouseType ht : HouseType.values()){
			if (numHouseTypes[ht.ordinal()] < min){
				argMin = ht;
				min = numHouseTypes[ht.ordinal()];
			}
		}
		return argMin;
	}
	
	public int getNumType(HouseType ht){
		return numHouseTypes[ht.ordinal()];
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
	private TreeDiff checkHouse(House h){
		// Check for overlap with well (And give it some room)
		for (int i = 0; i < 9; i++){
			if (h.getRect().contains(well.getX() + (i%3-1), well.getY() + (i/3-1))) {
				return null;
			}
		}
		
		HashSet<Tile> exclude = new HashSet<>();
		int currX, currY;
		Tile t;
		boolean pathBlocked = false;
		
		// Check one larger than house to prevent direct adjacency.
		for (int i = 0; i < h.getRect().getWidth(); i++){
			for (int j = 0; j < h.getRect().getHeight(); j++){
				currX = h.getRect().getX()+i;
				currY = h.getRect().getY()+j;
				t = Game.getMap().getGridTile(currX, currY);
				
				// Rejection checks
				if (t == null) return null;
				
				// If inside defined house boundaries
				if (i > -1 && j > -1 && i < h.getRect().getWidth() && j < h.getRect().getHeight()){
					if (t.hasSpecialType(SpecialType.PATH)) pathBlocked =  true;
					if (!t.isWalkable()) return null;
					exclude.add(t); // Exclude from later pathfinding
				}
				
				if (t.hasSpecialType(SpecialType.HOUSE)) return null;
				
				
				// Is an outer wall
				if (i != -1 && j != -1 && j != h.getRect().getHeight() && i != h.getRect().getWidth()){
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
			rewriteDiff.apply(false);
			TreeDiff houseDiff = pathTree.checkAddHouse(h, exclude);
			rewriteDiff.revert(false);
			if (houseDiff == null) return null;
			rewriteDiff.compose(houseDiff);
//			System.out.println("Rewrite Success:"+h);
			return rewriteDiff;
		} else {
			TreeDiff houseDiff = pathTree.checkAddHouse(h, exclude);
			return houseDiff;
		}
	}
	
	public LinkedList<House> getHouses(){
		return houses;
	}
}
