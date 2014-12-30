package core;

import entities.abstr.AbstractEntity.Facing;
import entities.concrete.Tree;
import entities.concrete.Tree.TreeType;
import gui.GUtil.SpriteSheetType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import core.Tile.TileType;
import core.display.SpriteInstance;
import core.display.SpriteManager;
import core.util.PerlinNoise;

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
	private HashMap<String, SpriteInstance> tileSpriteInstances;
	
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
		{"d_c1", "d_c2", "d_c3"};
	private String[] miscNames = 
		{"flower", "g_rock1", "g_rock2", "g_rock3", "d_rock1", "d_rock2","d_rock3"};
	
	private String[][] nameArrays =
		{grassCenterAnimNames, grassAnimNames, sandAnimNames, waterAnimNames, dirtAnimNames, miscNames};

	
	public TileMap(int size){
		seed = (int) System.currentTimeMillis();
		seed = 366907858;
		System.out.println("Seed: "+seed);
		tileMap = new Tile[size][size];
		this.size = size;
		this.rand = new Random(seed);
		this.tileSpriteInstances = new HashMap<>();
		
		for (String[] nArray : nameArrays){
			for (String key : nArray){
				SpriteInstance spr = SpriteManager.get().getSprite(SpriteSheetType.MAP, key).getInstance();
				this.tileSpriteInstances.put(key, spr);
			}
		}
		
		generateTerrain();
	}
	
	public void updateAnims(long deltaTime){
		for (SpriteInstance spr : tileSpriteInstances.values()){
			spr.update(deltaTime);
		}
	}
	
	/**
	 * Calculates "bitmask" to determine how the block is surrounded.
	 * This deals with the edges of tile regions. 
	 */
	private int calcBitmask(int x, int y){
		int total = 0;
		TileType centerType = tileMap[x][y].getType();


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
				tileMap[x][y].setSprite(genAnimID(x, y));
			}
		}
		PerlinNoise.setSeed(seed+1);
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				Tile t = tileMap[x][y];
				if (t.getType().equals(TileType.GRASS) && calcPerlinVal(x, y) > 0.25){
					if (rand.nextInt(5) == 0){
						float scale = Game.SCALE*Game.TILE_SIZE;
						t.addEntity(new Tree(x*scale, y*scale, TreeType.ANY));
					}
				} else if (t.getType().equals(TileType.GRASS) && calcPerlinVal(x, y) > 3){
//					if (rand.nextInt(2) == 0){
						float scale = Game.SCALE*Game.TILE_SIZE;
						t.addEntity(new Tree(x*scale, y*scale, TreeType.BIG));
//					}
				} else if (t.getType().equals(TileType.DIRT) && calcPerlinVal(x, y) > 0.25){
					if (rand.nextInt(10) == 0){
						float scale = Game.SCALE*Game.TILE_SIZE;
						t.addEntity(new Tree(x*scale, y*scale, TreeType.SMALL));
					}
				} else if (!t.getType().equals(TileType.WATER) && !t.getType().equals(TileType.SAND)){
					if (rand.nextInt(500) == 0){
						float scale = Game.SCALE*Game.TILE_SIZE;
						t.addEntity(new Tree(x*scale, y*scale, TreeType.ANY));
					}
				}
					
			}
		}
	}
	
	/**
	 * Uses Perlin noise to pick random tile types
	 * Helper class for generateTerrain()
	 */
	private TileType genTileType(int x, int y){
		double pVal = calcPerlinVal(x, y);
		if(pVal > 3){
			return TileType.WATER;
		}else if(pVal > 0.25){
			return TileType.GRASS;
		} else if (pVal < -3.2){
			return TileType.SAND;
		} else {
			return TileType.DIRT;
		}
	}
	
	private double calcPerlinVal(int x, int y){
		double scaleX = 1.1/100;
		double scaleY = 10;
		double ret = scaleY*PerlinNoise.noise(scaleX*x, scaleX*y);
		ret += PerlinNoise.noise((1.1/10)*x, (1.1/10)*y);
		ret += (PerlinNoise.noise(1.1/100*(x+size), 1.1/100*(x+size)))*PerlinNoise.noise((1.1)*x, (1.1)*y);
		return ret;
	}
	
	/**
	 * Assigns animation ID based on location and tile type.
	 * This uses a simplified marching squares algorithm with
	 * cell index calculated in calcBitmask().
	 * @param x Tile position
	 * @param y Tile position
	 * @return Animation
	 */
	private SpriteInstance genAnimID(int x, int y){
		int bit = calcBitmask(x, y);
		switch (tileMap[x][y].getType()){
		case GRASS:
			if (bit == 0) {
				switch (rand.nextInt(50)){
				case 1:
				case 2:
					return tileSpriteInstances.get("flower");
				case 3:
					return tileSpriteInstances.get("g_rock1");
				case 4:
					return tileSpriteInstances.get("g_rock2");
				case 5:
					return tileSpriteInstances.get("g_rock3");
				default:
					return tileSpriteInstances.get(grassCenterAnimNames[rand.nextInt(grassCenterAnimNames.length)]);			
				}					
			}
			return tileSpriteInstances.get(grassAnimNames[bit - 1]);
		case DIRT:
			switch (rand.nextInt(50)){
			case 1:
				return tileSpriteInstances.get("d_rock1");
			case 2:
				return tileSpriteInstances.get("d_rock2");
			case 3:
				return tileSpriteInstances.get("d_rock3");

			default:
				return tileSpriteInstances.get(dirtAnimNames[rand.nextInt(dirtAnimNames.length)]);
			}
		case SAND:
			return tileSpriteInstances.get(sandAnimNames[bit]);
		case WATER:
			return tileSpriteInstances.get(waterAnimNames[bit]);
		default:
			return null;
		}
	}
	
	public TileType getTexID(int tileX, int tileY){
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
	 * Get the size (in tiles) of the map.
	 * @return The length (in tiles) of a map side.
	 */
	public int getSize(){
		return size;
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
