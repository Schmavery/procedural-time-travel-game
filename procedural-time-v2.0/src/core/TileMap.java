package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import core.Tile.Type;

public class TileMap implements Serializable{

	private static final long serialVersionUID = 1L;
	private Tile[][] tileMap;
	private int size;
	private Random rand;
	private int seed;
	
	// Corner IDs for different terrain
	private int[] grassID = {0,64,65};
	private int[] dirtID = {77,78,79};
	private int[] sandID = {1,2,3,4,5};
	private int[] waterID = {1,2,3,4,5};
	
	public TileMap(int size){
		seed = (int) System.currentTimeMillis();
		System.out.println("Seed: "+seed);
		tileMap = new Tile[size][size];
		this.size = size;
		this.rand = new Random(seed);

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
	
	private void generateTerrain(){
		PerlinNoise.setSeed(seed);
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				tileMap[x][y] = new Tile(genTileType(x, y), 1, x, y);
			}
		}
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				tileMap[x][y].setTextureID(genAnimID(x, y));
			}
		}
	}
	
	private Type genTileType(int x, int y){
		double scaleX = 1.1/50;
		double scaleY = 10;
		double noise = scaleY*PerlinNoise.noise(scaleX*x, scaleX*y);
		if (noise > 0.3){
			return Type.GRASS;
		} else {
			return Type.DIRT;
		}
	}
	
	private int genAnimID(int x, int y){
		int bit = calcBitmask(x, y);
		if (tileMap[x][y].getType() == Type.GRASS){
			if (bit == 0) {return grassID[rand.nextInt(grassID.length)];}
			return bit;
		}else {
			return dirtID[rand.nextInt(dirtID.length)];
		}
	}
	
	public Type getTexID(int x, int y){
		return tileMap[x][y].getType();
	}
	
}
