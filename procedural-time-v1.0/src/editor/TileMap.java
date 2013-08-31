package editor;

import java.awt.Image;
import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import editor.TileTypes.TileType;

public class TileMap {
	
	Tile[][] map;
	//TileTypes[] types;
	private TileTypes types;
	public int tileSize;
	public int mapWidth = 40;
	public int mapHeight = 20;
	public int scale;
	
	public TileMap(Image[][] imgArray, String fileName, int tileSize, int scale, int width, int height){
		//creates a blank tilemap
		this.tileSize = tileSize;
		this.scale = scale;
		this.mapWidth = width;
		this.mapHeight = height;
		this.types = new TileTypes(50);
		loadTypes(imgArray);
		map = new Tile[mapWidth][mapHeight];
		
		if (fileName == null){

			for (int i = 0; i < mapWidth; i++){
				for (int j = 0; j < mapHeight; j++){
					map[i][j] = new Tile(new Point(i*tileSize, j*tileSize), (byte) 4);
				}
			}
		} else {

			byte[][] byteMap = deserializeMap(fileName);
			
			for (int i = 0; i < mapWidth; i++){
				for (int j = 0; j < mapHeight; j++){
					map[i][j] = new Tile(new Point(i*tileSize, j*tileSize), byteMap[i][j]);
				}
			}
		}
	}
	
	public TileMap(Image[][] imgArray, String fileName, int tileSize, int scale){

		this.tileSize = tileSize;
		this.scale = scale;
		char[][] typeMap = readCharMap(fileName);
		
		types = new TileTypes(50);
		loadTypes(imgArray);
		map = new Tile[mapWidth][mapHeight];		
		//System.out.println(typeMap[0][0]);
		for (int i = 0; i < mapWidth; i++){
			for (int j = 0; j < mapHeight; j++){
				map[i][j] = new Tile(new Point(i*tileSize, j*tileSize), types.getIndexByShortcut(typeMap[i][j]));
			}
		}
	}
	
	public TileMap(Image[][] imgArray, String fileName, int tileSize, int scale, int cat){

		this.tileSize = tileSize;
		this.scale = scale;
		//char[][] typeMap = readCharMap(fileName);
		byte[][] byteMap = deserializeMap("map");
		
		types = new TileTypes(50);
		loadTypes(imgArray);
		map = new Tile[mapWidth][mapHeight];		
		//System.out.println(typeMap[0][0]);
		for (int i = 0; i < mapWidth; i++){
			for (int j = 0; j < mapHeight; j++){
				map[i][j] = new Tile(new Point(i*tileSize, j*tileSize), byteMap[i][j]);
			}
		}
	}
	
	public void loadTypes(Image[][] imgArray){
		types.addTileType(imgArray[0][0], true, "plain_nw", '0');
		types.addTileType(imgArray[0][1], true, "plain_w", '1');
		types.addTileType(imgArray[0][2], true, "plain_sw", '2');
		types.addTileType(imgArray[1][0], true, "plain_n", '3');
		types.addTileType(imgArray[1][1], true, "plain_c", '_');
		types.addTileType(imgArray[1][2], true, "plain_s", '4');
		types.addTileType(imgArray[2][0], true, "plain_ne", '5');
		types.addTileType(imgArray[2][1], true, "plain_e", '6');
		types.addTileType(imgArray[2][2], true, "plain_se", '7');
		
		types.addTileType(imgArray[4][1], 500, true, "grass_flower", '*');
		types.addSceneByIndex(imgArray[9][4], 500, 9);
		types.addTileType(imgArray[7][4], 150, false, "fire", '^');
		types.addSceneByIndex(imgArray[6][4], 150, 10);
		types.addTileType(imgArray[8][8], 400, false, "water", '~');
		types.addSceneByIndex(imgArray[9][8], 400, 11);
		
		types.addTileType(imgArray[3][0], true, "plain_mud_nw", 'q');
		types.addTileType(imgArray[3][1], true, "plain_mud_w", 'a');
		types.addTileType(imgArray[3][2], true, "plain_mud_sw", 'z');
		types.addTileType(imgArray[4][0], true, "plain_mud_n", 'w');
		types.addTileType(imgArray[4][2], true, "plain_mud_s", 'x');
		types.addTileType(imgArray[5][0], true, "plain_mud_ne", 'e');
		types.addTileType(imgArray[5][1], true, "plain_mud_e", 'd');
		types.addTileType(imgArray[5][2], true, "plain_mud_se", 'c');
		
		types.addTileType(imgArray[6][0], true, "mud_1", 'r');
		types.addTileType(imgArray[6][1], true, "mud_2", 'f');
		types.addTileType(imgArray[6][2], true, "mud_3", 'v');
		
		types.addTileType(imgArray[7][0], false, "rock_1", 't');
		types.addTileType(imgArray[7][1], false, "bush_1", 'g');
		types.addTileType(imgArray[7][2], true, "tile_1", 'b');
		
		types.addTileType(imgArray[8][0], true, "dirt_path_1", 'y');
		types.addTileType(imgArray[8][1], true, "plain_path_1", 'h');
		types.addTileType(imgArray[8][2], false, "dirt_hole", 'n');
		types.addTileType(imgArray[9][0], true, "dirt_path_2", 'u');
		types.addTileType(imgArray[9][1], true, "plain_path_2", 'j');
		types.addTileType(imgArray[9][2], true, "tile_2", 'm');
		
		types.addTileType(imgArray[8][3], true, "dirt_path_3", 'i');
		types.addTileType(imgArray[9][3], true, "plain_path_3", 'k');
		
		types.addTileType(imgArray[8][4], true, "plain_grass", '-');
		
		types.addTileType(imgArray[8][5], false, "cliff_s", '+');
		types.addTileType(imgArray[9][5], false, "cliff_sw", '<');
		
		types.addTileType(imgArray[8][6], true, "inner_corner_nw", 'o');
		types.addTileType(imgArray[9][6], true, "inner_corner_ne", 'p');
		types.addTileType(imgArray[8][7], true, "inner_corner_sw", 'l');
		types.addTileType(imgArray[9][7], true, "inner_corner_se", ';');
		
		types.addTileType(imgArray[0][7], false, "roof_nw", '!');
		types.addTileType(imgArray[1][7], false, "roof_n", '!');
		types.addTileType(imgArray[2][7], false, "roof_ne", '!');
		
		types.addTileType(imgArray[0][8], false, "roof_sw", '!');
		types.addTileType(imgArray[1][8], false, "roof_s", '!');
		types.addTileType(imgArray[2][8], false, "roof_se", '!');
		
		types.addTileType(imgArray[0][9], false, "house_w", '!');
		types.addTileType(imgArray[1][9], false, "house_c", '!');
		types.addTileType(imgArray[2][9], false, "house_e", '!');
		
	}
	
	private char[][] readCharMap(String fileName){
		char[][] typeMap = new char[mapWidth][mapHeight];
		
		try{
			// Open the file
			FileInputStream fstream = new FileInputStream(getClass().getResource("../resources/" + fileName).getFile());
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			int i = 0;
			while ((strLine = br.readLine()) != null)   {
			// Print the content on the console
			//System.out.println (strLine);
				for (int j = 0; j < mapWidth; j++){
					typeMap[j][i] = strLine.charAt(j); 
				}
				i++;
			}
			i = 0;
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return typeMap;
	}
	
	public byte[][] getByteMap(){
		byte[][] byteMap = new byte[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++){
			for (int j = 0; j < mapHeight; j++){
				byteMap[i][j] = map[i][j].getType();
			}
		}
		return byteMap;
	}
	
	public void serializeMap(String fileName, byte[][] byteMap){
		try{
		      //use buffering
			OutputStream file = new FileOutputStream( "data/"+ fileName +".ser" );
			OutputStream buffer = new BufferedOutputStream( file );
			ObjectOutput output = new ObjectOutputStream( buffer );
			try{
				output.writeObject(byteMap);
		    }
		    finally{
		        output.close();
		    }
		}  
		    catch(IOException ex){
		    	ex.printStackTrace();
		    }
	}
	
	public byte[][] deserializeMap(String fileName){
		byte[][] newByteMap = null;
		try{
		      //use buffering
		      InputStream file = new FileInputStream( "data/"+fileName+".ser" );
		      InputStream buffer = new BufferedInputStream( file );
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try{
		        //deserialize the List
		        newByteMap = (byte[][]) input.readObject();
		      }
		      finally{
		        input.close();
		      }
		    }
		    catch(ClassNotFoundException ex){
		      ex.printStackTrace();
		    }
		    catch(IOException ex){
		    	ex.printStackTrace();
		    }
		return newByteMap;
	}
	
	
	public Tile getTileFromWorld(Point p){
		return getTileFromWorld(p.x, p.y);
	}
	
	public Tile getTileFromWorld(int x, int y){
		int tileX = x/tileSize;
		int tileY = y/tileSize;
		return map[tileX][tileY];
	}
	
	public boolean getWalkableFromWorld(int x, int y){
		// is the tile at this world co-ordinate walkable?
		Tile tempTile = getTileFromWorld(x, y);
		return types.isWalkable(tempTile.getType());
	}
	
	public TileType getTileType(Tile tile){
		return types.getTileByIndex(tile.getType());
	}
	
	public Tile getTile(int x, int y){
		return map[x][y];
	}
	
	public Image getTileImage(int x, int y){
		return types.getTileImgByIndex(map[x][y].getType());
	}
	
	public boolean isWalkable(int x, int y){
		return types.isWalkable(map[x][y].getType());
	}
	
	public void update(long timePassed){
		types.update(timePassed);
	}
	
	public Image getImageOfType(byte index){
		return types.getTileImgByIndex(index);
	}
}
