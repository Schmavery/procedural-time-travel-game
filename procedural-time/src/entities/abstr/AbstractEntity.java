package entities.abstr;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import core.Game;
import core.RandomManager;
import core.Tile;
import core.display.Sprite;
import core.display.SpriteInstance;
import entities.components.EntityFrame;
import entities.interfaces.Entity;

public abstract class AbstractEntity implements Entity
{
	// Note: The order of this enum is important, as the ordinal
	// is used as an array index.
	public static enum Facing {NORTH, EAST, SOUTH, WEST}

	private static int MAX_ID = 0;
	private int id;
	protected float x, y;
	private int drawPriority;
	private SpecialType specialType;
	protected SpriteInstance[] standingAnims;
	protected Facing facing;
	protected EntityFrame frame;
	protected Random rand;
	protected boolean debug;
	

	public AbstractEntity(float x, float y)
	{
		id = MAX_ID++;
		standingAnims = new SpriteInstance[4];
		this.x = x;
		this.y = y;
		warpToClosestClearTile();
		rand = new Random(RandomManager.getSeed(id));
		facing = Facing.NORTH;
		drawPriority = 0;
		debug = false;
		specialType = SpecialType.NORMAL;
	}
	
	public void setSprite(Sprite spr){
		if (spr != null){
			this.standingAnims[0] = spr.getInstance();
		} else {
			System.out.println("Tried to set null sprite");
		}
	}
	
	public void setStandingAnims(Sprite spr_n, Sprite spr_e, Sprite spr_s, Sprite spr_w)
	{
		this.standingAnims[0] = spr_n.getInstance();
		this.standingAnims[1] = spr_e.getInstance();
		this.standingAnims[2] = spr_s.getInstance();
		this.standingAnims[3] = spr_w.getInstance();
	}

	public float getX()	{return x;}

	public float getY()	{return y;}

	public int getTileX() {return (int) (x/(Game.SCALE*Game.TILE_SIZE));}

	public int getTileY() {return (int) (y/(Game.SCALE*Game.TILE_SIZE));}
	
	@Override
	public void setDebug(boolean on) {debug = on;}

	@Override
	public SpecialType getSpecialType(){
		return this.specialType;
	}
	
	public void setSpecialType(SpecialType specialType) {
		this.specialType = specialType;
	}
	
	/**
	 * Default is 0.
	 * Negative is below 0, positive draws on top.
	 * @param p New draw priority
	 */
	protected void setDrawPriority(int p){
		this.drawPriority = p;
	}

	@Override
	public int getDrawPriority(){
		return this.drawPriority;
	}
	
	@Override
	public void draw(float x, float y){
		float offset = (Game.TILE_SIZE*Game.SCALE)/2;
		this.standingAnims[facing.ordinal()].draw(x + getX() + offset, y + getY() + offset);
	}
	
	@Override
	public void draw(float x, float y, float w, float h){
		float offset = (Game.TILE_SIZE*Game.SCALE)/2;
		this.standingAnims[facing.ordinal()].draw(x + getX() + offset, y + getY() + offset, w, h);
	}
	
	protected void warpToClosestClearTile(){
		Tile t;
		// Map being initialized
		if (Game.getMap() == null) return;
			
		if (frame != null)
			t = Game.getMap().getWorldTile(frame.getCenterX(x), frame.getCenterY(y));
		else
			t = Game.getMap().getWorldTile(x, y);
		if (!t.isWalkable()){
			HashSet<Tile> closed = new HashSet<>();
			LinkedList<Tile> open = new LinkedList<>();
			Tile warpTile = null;
			open.add(t);
			while (!open.isEmpty() && warpTile == null){
				t = open.removeLast();
				for (Tile add: t.getReachable()){
					if (closed.contains(add)) continue;
					if (add.isWalkable()){
						warpTile = add;
						break;
					} else {
						open.addFirst(add);
					}
				}
				closed.add(t);
			}
			if (warpTile != null){
				float offset = Game.SCALE*Game.TILE_SIZE;
				x = warpTile.getLeft() + offset;
				y = warpTile.getTop() + offset;
//				System.out.println("warped");
			} else {
				System.out.println("Warp failed :(");
			}
		}
	}
}