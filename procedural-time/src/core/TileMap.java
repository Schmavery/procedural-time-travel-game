package core;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;

import core.Tile.Type;
import entities.abstr.AbstractEntity.Facing;

/**
 * Class that keeps generates the game tilemap using Perlin Noise.
 * It then keeps a record of all these tiles.
 * @author avery
 *
 */
public class TileMap implements Serializable{

	private static final long serialVersionUID = 1L;
	private Tile[][] tileMap;
	private int size;
	private Random rand;
	private int seed;
	
	// Corner IDs for different terrain
	private String[] grassCenterAnimNames = 
		{"g_c1","g_c2","g_c3"};
	private String[] grassAnimNames = 
		{"g_w","g_n","g_nw","g_e","g_we","g_ne",
			"g_wne","g_s","g_sw","g_ns","g_wns","g_se","g_wse","g_nes","g_nesw"};
	private String[] sandAnimNames =
		{"s_c", "s_w", "s_n", "s_nw", "s_e", "s_we", "s_ne", "s_wne",
			"s_s", "s_sw", "s_ns", "s_wns", "s_se", "s_wse", "s_nes", "s_nesw"};
	private String[] waterAnimNames =
		{"w_c", "w_w", "w_n", "w_nw", "w_e", "w_we", "w_ne", "w_wne",
			"w_s", "w_sw", "w_ns", "w_wns", "w_se", "w_wse", "w_nes", "w_nesw"};
	private String[] dirtAnimNames = 
		{"d_c0", "d_c1", "d_c2"};

	
	public TileMap(int size){
		seed = (int) System.currentTimeMillis();
//		seed = 366907858;
		System.out.println("Seed: "+seed);
		tileMap = new Tile[size][size];
		this.size = size;
		this.rand = new Random(seed);
		generateTerrain();
	}
	
	
	/**
	 * Returns all tiles contained within the square centered on (x,y), with sidelength (2*range+1)
	 * Then you can iterate through the resulting array and perform operations on all nearby tiles.
	 * Handles negatives and points close to the edge of the map
	 * @param range Distance from center
	 * @param centerX X-coordinate of the center of the region
	 * @param centerY Y-coordinate of the center of the region
	 * @return Array of tiles corresponding to the surrounding tiles.
	 */
//	public Tile[] getSurroundingTiles(int range, int pos_x, int pos_y){
//		try {	
//			int absRange = Math.abs(range);
//			int x1 = Math.min(size-1, Math.max(0, pos_x - absRange));
//			int x2 = Math.min(size-1, Math.max(0, pos_x + absRange));
//			int y1 = Math.min(size-1, Math.max(0, pos_y - absRange));
//			int y2 = Math.min(size-1, Math.max(0, pos_y + absRange));
//			Tile[] tiles = new Tile[(x2-x1+1)*(y2-y1+1)];
//			int counter = 0;
//			for(int x = x1; x <= x2; x++){
//				for (int y = y1; y <= y2; y++){
//					tiles[counter] = tileMap[x][y];
//					counter++;
//				}
//			}
//			return tiles;
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	// Doesn't currently work
//	public List<Tile> getSurroundingTiles2(int range, int pos_x, int pos_y){
//		int absRange = Math.abs(range);
//		int x1 = Math.min(size, Math.max(0, pos_x - absRange));
//		int x2 = Math.min(size, Math.max(0, pos_x + absRange));
//		int y1 = Math.min(size, Math.max(0, pos_y - absRange));
//		int y2 = Math.min(size, Math.max(0, pos_y + absRange));
//		List<Tile> l1 = Arrays.asList(tileMap[4]).subList(y1, y2);
//		List<Tile> l2 = Arrays.asList(tileMap[5]).subList(y1, y2);
//		l1.addAll(l2);
//		List<Tile> resultList = new ArrayList<Tile>();
//		for(int x = x1; x <= x2; x++){
//			resultList.addAll(Arrays.asList(tileMap[x]).subList(y1, y2));
//		}
//		return null;
//	}
	
	/**
	 * Calculates "bitmask" to determine how the block is surrounded.
	 * This deals with the edges of tile regions. 
	 */
	private int calcBitmask(int x, int y){
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
	
	/**
	 * Uses Perlin noise to generate a random terrain
	 */
	private void generateTerrain(){
		PerlinNoise.setSeed(seed);
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				tileMap[x][y] = new Tile(genTileType(x, y), calcPerlinVal(x, y), x, y);
			}
		}
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				//tileMap[x][y].setTextureID(genAnimID(x, y));
				tileMap[x][y].setAnim(genAnimID(x, y));
			}
		}
	}
	
	/**
	 * Uses Perlin noise to pick random tile types
	 * Helper class for generateTerrain()
	 */
	private static Type genTileType(int x, int y){
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
	
	private static double calcPerlinVal(int x, int y){
		double scaleX = 1.1/100;
		double scaleY = 10;
		return scaleY*PerlinNoise.noise(scaleX*x, scaleX*y);
	}
	
	/**
	 * Assigns animation ID based on location and tile type.
	 * This uses a simplified marching squares algorithm with
	 * cell index calculated in calcBitmask().
	 * @param x Tile position
	 * @param y Tile position
	 * @return Animation
	 */
	private Animation genAnimID(int x, int y){
		int bit = calcBitmask(x, y);
		switch (tileMap[x][y].getType()){
		case GRASS:
			if (bit == 0) {
				switch (rand.nextInt(50)){
				case 1:
				case 2:
					return AnimationManager.getAnim("flower");
				case 3:
					return AnimationManager.getAnim("g_rock1");
				case 4:
					return AnimationManager.getAnim("g_rock2");
				case 5:
					return AnimationManager.getAnim("g_rock3");
				default:
					return AnimationManager.getAnim(grassCenterAnimNames[rand.nextInt(grassCenterAnimNames.length)]);			
				}					
			}
			return AnimationManager.getAnim(grassAnimNames[bit - 1]);
		case DIRT:
			switch (rand.nextInt(50)){
			case 1:
				return AnimationManager.getAnim("d_rock1");
			case 2:
				return AnimationManager.getAnim("d_rock2");
			case 3:
				return AnimationManager.getAnim("d_rock3");

			default:
				return AnimationManager.getAnim(dirtAnimNames[rand.nextInt(dirtAnimNames.length)]);
			}
		case SAND:
			return AnimationManager.getAnim(sandAnimNames[bit]);
		case WATER:
			return AnimationManager.getAnim(waterAnimNames[bit]);
		default:
			return null;
		}
	}
	
	public Type getTexID(int tileX, int tileY){
		return tileMap[tileX][tileY].getType();
	}
	
	
	public Tile getTile(int tileX, int tileY){
		if ( tileX >= 0 && tileX < size && tileY >= 0 && tileY < size){
			return tileMap[tileX][tileY];
		}
//		System.out.println("Invalid tile index");
		return null;
	}
	
	/**
	 * Returns the tile corresponding to a world position.
	 * @param x The number of pixels right of the origin.
	 * @param y The number of pixels down from the origin.
	 * @return The Tile corresponding to this position.
	 */
	public Tile getWorldTile(float x, float y){
		int xIndex = (int) (x / (Game.TILE_SIZE*Game.SCALE));
		int yIndex = (int) (y / (Game.TILE_SIZE*Game.SCALE));
		return getTile(xIndex, yIndex);
	}
	
	/**
	 * Returns an iterator to tiles contained within the square centered on (x,y), with sidelength (2*range+1)
	 * Handles negatives and points close to the edge of the map
	 * @param radius Distance from center
	 * @param centerX X-coordinate of the center of the region
	 * @param centerY Y-coordinate of the center of the region
	 * @return Iterator to tiles corresponding to the surrounding tiles.
	 */
	public LocaleIterator getLocale(int radius, int centerX, int centerY){
		return new LocaleIterator(centerX, centerY, radius);
	}
	
	/**
	 * Get the size (in tiles) of the map.
	 * @return The length (in tiles) of a map side.
	 */
	public int getSize(){
		return size;
	}
	
	public class LocaleIterator implements Iterator<Tile>, Iterable<Tile>{

		int currX, currY;
		int x1, x2, y1, y2;
		
		public LocaleIterator(int centerX, int centerY, int radius){
//			int absRadius = Math.abs(radius);
			x1 = Math.min(size - 1, Math.max(0, centerX - radius));
			x2 = Math.min(size - 1, Math.max(0, centerX + radius));
			y1 = Math.min(size - 1, Math.max(0, centerY - radius));
			y2 = Math.min(size - 1, Math.max(0, centerY + radius));
			
			currX = x1;
			currY = y1;
		}
		
		@Override
		public boolean hasNext()
		{
			return (currY < y2 || currX < x2);
		}

		@Override
		public Tile next()
		{
			Tile curr = tileMap[currX][currY];
			currX++;
			if (currX > x2){
				currX = x1;
				currY++;
			}
			return curr;
		}

		/**
		 * You can't remove.
		 */
		@Override
		public void remove(){return;}
		@Override
		public Iterator<Tile> iterator(){return this;}
	}
	
	/**
	 * Returns an iterator to tiles contained within the isosceles triangle with the vertex opposite the base
	 * located at (x, y) and opening in the direction of face.
	 * Handles negatives and points close to the edge of the map.
	 * @param centerX X-coordinate of the center of the region.
	 * @param centerY Y-coordinate of the center of the region.
	 * @param length length (height) of the triangle.
	 * @param face Direction in which the triangle should expand.
	 * @return Iterator to tiles corresponding to the surrounding tiles.
	 */
	public VisionIterator getVision(int centerX, int centerY, int length, Facing face){
		return new VisionIterator(centerX, centerY, length, face);
	}
	
	public class VisionIterator implements Iterator<Tile>, Iterable<Tile>{

		int currX, currY;
		int x1, x2, y1, y2;
		
		public VisionIterator(int centerX, int centerY, int length, Facing face){
//			int absRadius = Math.abs(radius);
//			x1 = Math.min(size - 1, Math.max(0, centerX - absRadius));
//			x2 = Math.min(size - 1, Math.max(0, centerX + absRadius));
//			y1 = Math.min(size - 1, Math.max(0, centerY - absRadius));
//			y2 = Math.min(size - 1, Math.max(0, centerY + absRadius));
//			
//			currX = x1;
//			currY = y1;
		}
		
		@Override
		public boolean hasNext()
		{
			return (currY < y2 || currX < x2);
		}

		@Override
		public Tile next()
		{
			Tile curr = tileMap[currX][currY];
			currX++;
			if (currX > x2){
				currX = x1;
				currY++;
			}
			return curr;
		}

		/**
		 * You can't remove.
		 */
		@Override
		public void remove(){return;}
		@Override
		public Iterator<Tile> iterator(){return this;}
	}
	
}
