package entities;

import java.util.LinkedList;

import core.AnimationManager.Animation;
import core.Game;
import core.Message;
import core.PathFinder;
import core.Tile;
import core.TileMap;

public class Human {
	public static enum Facing {NORTH, EAST, SOUTH, WEST};
	public static enum Gender {MALE, FEMALE, DWARF, OTHER};


	private TileMap tileMap;
	private float x, y;
	private float dx, dy;
	private float speed;
	private boolean moving;
	private boolean collided;
	private Animation[] movingAnims;
	private Animation[] standingAnims;
	private Facing facing;
	private EntityFrame frame;
	private LinkedList<Message> messages;
	private PathFinder<Tile> tilePather;
	

	String name;
	private Gender gender;
	
	public Human(float x, float y, Gender gender, String name, TileMap tileMap){
		this.x = x;
		this.y = y;
		this.gender = gender;
		this.tileMap = tileMap;
		this.facing = Facing.SOUTH;
		movingAnims = new Animation[4];
		standingAnims = new Animation[4];
		frame = new EntityFrame(15,10);
		tilePather = new PathFinder<Tile>();
		speed = 0.4f;
		messages = new LinkedList<Message>();
		tileMap.getWorldTile(frame.getCenterX(x), frame.getCenterY(y)).addEntity(this);
		
//		this.name = NameGen.genName(this.gender);
		this.name = name;
	}

	public void update(long deltaTime){
		if (!tilePather.isEmpty()){
			dx = (tilePather.currNode().getLeft() + (Game.SCALE*Game.TILE_SIZE/2) - getCenterX())/deltaTime;
			dy = (tilePather.currNode().getTop() + (Game.SCALE*Game.TILE_SIZE/2) - getCenterY())/deltaTime;
			if (frame.isContained(tilePather.currNode(), x, y)){
				tilePather.nextNode();
			}

		}
		
		moving = (dx != 0 || dy != 0);
		
		if (moving){
			Tile tile = tileMap.getWorldTile(frame.getCenterX(x), frame.getCenterY(y));
			dx = deltaTime*dx;
			dy = deltaTime*dy;
			float speed = deltaTime*this.speed;
			// Handle Speed //
			if (dx != 0 && dy != 0){
				float hyp = 0.85f*speed; 
				dx = Math.max(Math.min(hyp, dx), -hyp);
				dy = Math.max(Math.min(hyp, dy), -hyp);
			} else {
				dx = Math.max(Math.min(speed, dx), -speed);
				dy = Math.max(Math.min(speed, dy), -speed);
			}
			
			// Handle Facing //	
			if (dy < 0){
				facing = Facing.NORTH;
			} else if (dy > 0){
				facing = Facing.SOUTH;
			} else if (dx > 0){
				facing = Facing.EAST;
			} else if (dx < 0){
				facing = Facing.WEST;
			}
			
			x += dx;
			collided = false;
			if (dx != 0 && frame.isColliding(tileMap, x, y)){
				collided = true;
				x -= dx;
			}
			y += dy;
			if (dy != 0 && frame.isColliding(tileMap, x, y)){
				collided = true;
				y -= dy;
			}
			dx = 0;
			dy = 0;
			
			if (tile != null){
				Tile newTile = tileMap.getWorldTile(frame.getCenterX(x), frame.getCenterY(y));
				if (tile != null && newTile != null && !tile.isSameNode(newTile)){
					tile.removeEntity(this);
					newTile.addEntity(this);
				}
			}
			movingAnims[facing.ordinal()].update(deltaTime);
		}
		
		// Add new messages.
		boolean test = false;
		int range = 50*(int) Game.SCALE;
		for (Message m : Message.getOldMessages()){
			if ((messages.isEmpty() || 
					(m.getTime() >= messages.getLast().getTime() && !m.equals(messages.getLast()))) &&
					!m.getSender().equals(this) &&
					(m.getX() > x - range && m.getX() < x + range) &&
					(m.getY() > y - range && m.getY() < y + range)){
				messages.add(m);
				if (m.getText().equals("hey")){
					test = true;
				}
			}
		}
		if (test) {Message.say(x, y, "What's up? I'm "+name+"!", this);}
		
		
		
	}
	
	public void move(float dx, float dy){
		this.dx += dx;
		this.dy += dy;
	}

	
	public void move(Facing f){
		facing = f;
		moving = true;
	}
	
	public void walkTo(int tileX, int tileY){
		if (tileMap.getTile(tileX, tileY) == null || !tileMap.getTile(tileX, tileY).isWalkable()){
			System.out.println(tileX + ", " + tileY);
			return;
		} else {
			tilePather.clear();
			tilePather.newPath(	tileMap.getWorldTile(getCenterX(), getCenterY()),
					tileMap.getTile(tileX, tileY));
			tilePather.generatePath(0);
			for (Tile t : tilePather.getPath()){
				System.out.println(t.getX() + ", " + t.getY());
			}
		}
	}
	
	public float getCenterX(){
		return frame.getCenterX(x);
	}
	public float getCenterY(){
		return frame.getCenterY(y);
	}
	
	
	public void setMovingAnims( Animation anim_n, Animation anim_e, Animation anim_s, Animation anim_w ){
		this.movingAnims[0] = anim_n.cloneAnim();
		this.movingAnims[1] = anim_e.cloneAnim();
		this.movingAnims[2] = anim_s.cloneAnim();
		this.movingAnims[3] = anim_w.cloneAnim();
	}

	public void setStandingAnims( Animation anim_n, Animation anim_e, Animation anim_s, Animation anim_w ){
		this.standingAnims[0] = anim_n.cloneAnim();
		this.standingAnims[1] = anim_e.cloneAnim();
		this.standingAnims[2] = anim_s.cloneAnim();
		this.standingAnims[3] = anim_w.cloneAnim();
	}
	
	public float getX(){return x;}
	public float getY(){return y;}
	public String getName(){return name;}
	
	public int getTexX(){ 
		if (moving){
			return movingAnims[facing.ordinal()].getDispX();
		}
		return standingAnims[facing.ordinal()].getDispX();
	}	
	public int getTexY(){
		if (moving)
			return movingAnims[facing.ordinal()].getDispY();
		return standingAnims[facing.ordinal()].getDispY();
	}	
	
}
