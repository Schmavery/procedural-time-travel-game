package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import core.AnimationManager.Animation;
import core.Tile.Type;

public class TileMap implements Serializable{

	private static final long serialVersionUID = 1L;
	private Tile[][] tileMap;
	private int size;
	private Random rand;
	private int seed;
	private AnimationManager animManager;
	
	// Corner IDs for different terrain
	private String[] grassAnimStringsCenter = 
		{"g_c1","g_c2","g_c3"};
	private String[] grassAnimStrings = 
		{"g_w","g_n","g_nw","g_e","g_we","g_ne",
			"g_wne","g_s","g_sw","g_ns","g_wns","g_se","g_wse","g_nes","g_nesw"};
	private String[] sandAnimStrings =
		{"s_c", "s_w", "s_n", "s_nw", "s_e", "s_we", "s_ne", "s_wne",
			"s_s", "s_sw", "s_ns", "s_wns", "s_se", "s_wse", "s_nes", "s_nesw"};
	private String[] waterAnimStrings =
		{"w_c", "w_w", "w_n", "w_nw", "w_e", "w_we", "w_ne", "w_wne",
			"w_s", "w_sw", "w_ns", "w_wns", "w_se", "w_wse", "w_nes", "w_nesw"};
	private String[] dirtAnimStrings = 
		{"d_c0", "d_c1", "d_c2"};

//	private int[] grassID = {0,64,65,66};
//	private int[] dirtID = {77,78,79};
//	private int[] sandID = {1,2,3,4,5};
//	private int[] waterID = {1,2,3,4,5};
	
	
	
	public TileMap(int size, AnimationManager am){
		//seed = (int) System.currentTimeMillis();
		seed = 761052304;
		System.out.println("Seed: "+seed);
		tileMap = new Tile[size][size];
		this.size = size;
		this.rand = new Random(seed);
		animManager = am;

		generateTerrain();
	}
	
	// Returns a square of tiles centered on (x,y), with sidelength (2*range+1)
	// Handles negatives and points close to the edge of the map
	public Tile[] getSurroundingTiles(int range, int pos_x, int pos_y){
		try {	
			range = Math.abs(range);
			int x1 = Math.min(size-1, Math.max(0, pos_x - range));
			int x2 = Math.min(size-1, Math.max(0, pos_x + range));
			int y1 = Math.min(size-1, Math.max(0, pos_y - range));
			int y2 = Math.min(size-1, Math.max(0, pos_y + range));
			Tile[] tiles = new Tile[(x2-x1+1)*(y2-y1+1)];
			int counter = 0;
			for(int x = x1; x <= x2; x++){
				for (int y = y1; y <= y2; y++){
					tiles[counter] = tileMap[x][y];
					counter++;
				}
			}
			return tiles;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	// Doesn't currently work
	public List<Tile> getSurroundingTiles2(int range, int pos_x, int pos_y){
		range = Math.abs(range);
		int x1 = Math.min(size, Math.max(0, pos_x - range));
		int x2 = Math.min(size, Math.max(0, pos_x + range));
		int y1 = Math.min(size, Math.max(0, pos_y - range));
		int y2 = Math.min(size, Math.max(0, pos_y + range));
		List<Tile> l1 = Arrays.asList(tileMap[4]).subList(y1, y2);
		List<Tile> l2 = Arrays.asList(tileMap[5]).subList(y1, y2);
		l1.addAll(l2);
		List<Tile> resultList = new ArrayList<Tile>();
		for(int x = x1; x <= x2; x++){
			resultList.addAll(Arrays.asList(tileMap[x]).subList(y1, y2));
		}
		return null;
	}
	
	// Calculates "bitmask" to determine how the block is surrounded.
	// This deals with the edges of tile regions
	
	public int calcBitmask(int x, int y){
		int total = 0;
		Type centerType = tileMap[x][y].getType();


		if (x - 1 >= 0 && tileMap[x-1][y].getType() != centerType){
			total += 1;
		}
		if (y - 1 >= 0 && tileMap[x][y-1].getType() != centerType){
			total += 2;
		}
		if (x + 1 < size && tileMap[x+1][y].getType() != centerType){
			total += 4;
		}
		if (y + 1 < size && tileMap[x][y+1].getType() != centerType){
			total += 8;
		}
		return total;
	}
	
	// Uses Perlin noise to generate a random terrain
	private void generateTerrain(){
		PerlinNoise.setSeed(seed);
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				tileMap[x][y] = new Tile(genTileType(x, y), calcPerlinVal(x, y), x, y, null);
			}
		}
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				//tileMap[x][y].setTextureID(genAnimID(x, y));
				tileMap[x][y].setAnim(genAnimID(x, y));
			}
		}
	}
	
	// Uses Perlin noise to pick random tile types
	// Helper class for generateTerrain()
	private Type genTileType(int x, int y){
		double pVal = calcPerlinVal(x, y);
		if(pVal > 3){
			return Type.WATER;
		}else if(pVal > 0.3){
			return Type.GRASS;
		} else if (pVal < -3){
			return Type.SAND;
		} else {
			return Type.DIRT;
		}
	}
	
	private double calcPerlinVal(int x, int y){
		double scaleX = 1.1/100;
		double scaleY = 10;
		return scaleY*PerlinNoise.noise(scaleX*x, scaleX*y);
	}
	
	
	private Animation genAnimID(int x, int y){
		int bit = calcBitmask(x, y);
		switch (tileMap[x][y].getType()){
		case GRASS:
			if (bit == 0) {
				switch (rand.nextInt(50)){
				case 1:
				case 2:
					return animManager.getAnim("flower");
				case 3:
					return animManager.getAnim("g_rock1");
				case 4:
					return animManager.getAnim("g_rock2");
				case 5:
					return animManager.getAnim("g_rock3");
				default:
					return animManager.getAnim(grassAnimStringsCenter[rand.nextInt(grassAnimStringsCenter.length)]);			
				}					
			}
			return animManager.getAnim(grassAnimStrings[bit - 1]);
		case DIRT:
			switch (rand.nextInt(50)){
			case 1:
				return animManager.getAnim("d_rock1");
			case 2:
				return animManager.getAnim("d_rock2");
			case 3:
				return animManager.getAnim("d_rock3");

			default:
				return animManager.getAnim(dirtAnimStrings[rand.nextInt(dirtAnimStrings.length)]);
			}
		case SAND:
			return animManager.getAnim(sandAnimStrings[bit]);
		case WATER:
			return animManager.getAnim(waterAnimStrings[bit]);
		default:
			return null;
		}
	}
	
	public Type getTexID(int x, int y){
		return tileMap[x][y].getType();
	}
	
}
