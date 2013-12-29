package entities;

import gui.GUtil;
import gui.GUtil.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;

import core.AnimationManager.Animation;
import core.Game;
import core.Message;
import core.PathException;
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
	private List<Message> messages;
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
		speed = 0.2f;
		messages = new ArrayList<>(10);
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
		
		if (tilePather.isRunning()){
			try {
				tilePather.generatePath(100);
			} catch (PathException e){
				System.out.println(e.getMessage());
				tilePather.clear();
			}
		}
		
		pathGen();
		processMessages();
		
	}
	
	private void processMessages(){
		long currTime = System.currentTimeMillis();
		int delay = 200;
		for (int i = 0; i < messages.size(); i++){
			long age = currTime - messages.get(i).getTime();
			if (age > delay && !messages.get(i).isBroadcast()){
				broadcast(messages.get(i));
			}
			if (age > messages.get(i).getText().length()*100 + delay){
				messages.remove(i);
			}
		}
//		for (Message m : messages){
//			long age = currTime - m.getTime();
//			if (age > m.getText().length()d*100){
//				messages.remove(m);
//			}
//		}
	}
	
	public void tell(Message m){
		if (m.getText().toLowerCase().indexOf("hey") > -1){
			say("What's up? I'm " + name + "!");
		}
	}

	public void say(String text){
		Message m = new Message(x, y, text, this);
		messages.add(m);
	}
	
	private void broadcast(Message m){
		m.broadcast();
		int tileX = (int) getCenterX()/tileMap.getSize();
		int tileY = (int) getCenterY()/tileMap.getSize();
		for (Tile tile : tileMap.getLocale(m.getVolume(), getTileX(), getTileY())){
			for (Human h : tile.getEntities()){
				if (!h.equals(this))
					h.tell(m);
			}
		}
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
			return;
		} else {
			tilePather.clear();
			tilePather.newPath(	tileMap.getWorldTile(getCenterX(), getCenterY()),
					tileMap.getTile(tileX, tileY));
			pathGen();
		}
	}
	
	public void pathGen(){
		if (tilePather.isRunning()){
			try {
				tilePather.generatePath(100);
			} catch (PathException e){
				System.out.println(e.getMessage());
				tilePather.clear();
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
	public int getTileX(){return (int) (x/(Game.SCALE*Game.TILE_SIZE));}
	public int getTileY(){return (int) (y/(Game.SCALE*Game.TILE_SIZE));}
	public boolean isMoving(){return moving;}
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
	
	public void draw(float x, float y){
		GUtil.drawSprite (SpriteSheet.PEOPLE, getX() + x,
				getY() + y, getTexX(), getTexY(), Game.SCALE*Game.TILE_SIZE, Game.SCALE*Game.TILE_SIZE, 16);
		if (!messages.isEmpty()){
			Message m = messages.get(messages.size() - 1);
			Rectangle rect = new Rectangle(
					(int) (m.getSender().getX() + x - (GUtil.textLength(m.getText()) - 16)/2), 
					(int) (m.getSender().getY() + y - 60),
					(GUtil.textLength(m.getText())) + 32, 50);
			GUtil.drawBubble(rect, new Color(200, 200, 175));
			GUtil.drawText(rect.getX()+16, rect.getY()+16, ReadableColor.BLACK, m.getText());
		}
	}
	
}
