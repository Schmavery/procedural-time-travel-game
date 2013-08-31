package core;

import java.awt.Image;

public class TileTypes {
	private static byte nextIndex = 0;
	private TileType[] tiles;
	
	public TileTypes(int size){
		tiles = new TileType[size];
	}

	public void addTileType(Image tileImg, boolean walkable, String tileName, char shortcut){
		tiles[nextIndex] = new TileType(tileImg, nextIndex, walkable, tileName, shortcut);
		nextIndex++;
	}
	
	public void addTileType(Image tileImg, long timeTime, boolean walkable, String tileName, char shortcut){
		tiles[nextIndex] = new TileType(tileImg, timeTime, nextIndex, walkable, tileName, shortcut);
		nextIndex++;
	}
	
	///////////////////////////////
	public class TileType{
		private Animation anim;
		private Image currImg;
		private byte tileIndex;
		private boolean walkable;
		private boolean isAnim = false;
		private String tileName;
		private char shortcut;
		
		public TileType(Image tileImg, byte index, boolean walkable, String tileName, char shortcut){
			//plain, no animation
			this.currImg = tileImg;
			this.tileIndex = index;
			this.walkable = walkable;
			this.tileName = tileName;
			this.shortcut = shortcut;
			this.isAnim = false;
		}
		
		public TileType(Image tileImg, long tileTime, byte index, boolean walkable, String tileName, char shortcut){
			//tile with animation
			anim = new Animation();
			anim.addScene(tileImg, tileTime);
			this.tileIndex = index;
			this.walkable = walkable;
			this.tileName = tileName;
			this.shortcut = shortcut;
			this.isAnim = true;
		}
		
		public String getTileName(){return tileName;}
		public byte getTileindex(){return tileIndex;}
		public char getShortcut(){return shortcut;}
		public boolean isWalkable(){return walkable;}
		public boolean isAnim(){return isAnim;}
		public Image getTileImg(){
			if (anim != null){
				return anim.getImage();
			} else {
				return currImg;
			}
		}
		public void update(long timePassed){
			if (anim != null){
				anim.updateScene(timePassed);
			}
		}
		public void addScene(Image newImg, long length){
			if (anim != null){
				anim.addScene(newImg, length);
			}
		}
		
	}
	///////////////////////////////////
	public TileType getTileByName(String tileName){
		for (int i = 0; i < tiles.length; i++)
			if (tiles[i].getTileName().equals(tileName)){
				return tiles[i];
			}
		return null;
	}
	
	public boolean isWalkable(int index){
		return tiles[index].isWalkable();
	}
	
	
	public int getTileIndexByName(String tileName){
		for (int i = 0; i < tiles.length; i++)
			if (tiles[i].getTileName().equals(tileName)){
				return tiles[i].getTileindex();
			}
		return 0;
	}
	
	public TileType getTileByIndex(int tileIndex){
		return tiles[tileIndex];
	}
	
	public TileType getTileByShortcut(char shortcut){
		for (int i = 0; i < tiles.length; i++)
			if (tiles[i].getShortcut()==shortcut){
				return tiles[i];
			}
		return null;
	}
	
	public byte getIndexByShortcut(char shortcut){
		for (int i = 0; i < tiles.length; i++){
			if (tiles[i].getShortcut()==shortcut){
				return tiles[i].getTileindex();
			}
		}
		return 0;
	}
	
	public Image getTileImgByIndex(int index) {
		return tiles[index].getTileImg();
	}
	
	public void update(long timePassed){
		for (int i = 0; i < tiles.length; i++){
			if (tiles[i] != null){
				tiles[i].update(timePassed);
			}
		}
	}
	
	public void addSceneByIndex(Image newImg, long length, int index){
		tiles[index].addScene(newImg, length);
	}
}
