package entities;

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
import entities.interfaces.Entity;
import entities.interfaces.Hittable;
import entities.interfaces.Holdable;
import entities.interfaces.Talkable;
import entities.interfaces.Weapon;
import gui.GUtil;
import gui.GUtil.SpriteSheet;

public class Humanoid extends AbstractMovingEntity implements Hittable,
		Talkable {
	
	public static enum Gender {
		MALE, FEMALE, DWARF, OTHER
	}

	public static enum EntityAction {
		SWING, USE, STOW, RETREIVE, TALK, PICKUP, DEFAULT
	}

	public static Fist fist = new Fist();

	private List<Message> messages;
	String name;
	private int health;
	private int maxHealth;
	Holdable[] inventory;
	Holdable heldItem;

	// private Gender gender;

	public Humanoid(float x, float y, Gender gender, String name) {
		super(x, y);

		health = 10;
		inventory = new Holdable[3];
		heldItem = fist;
		// this.gender = gender;
//		this.tileMap = tileMap;
		this.facing = Facing.SOUTH;
		movingAnims = new Animation[4];
		standingAnims = new Animation[4];
		frame = new EntityFrame(15, 10);
		tilePather = new PathFinder<Tile>();
		speed = 0.2f;
		messages = new ArrayList<>(10);
		Game.getMap().getWorldTile(frame.getCenterX(x), frame.getCenterY(y))
				.addEntity(this);

		// this.name = NameGen.genName(this.gender);
		this.name = name;
	}

	public void update(long deltaTime) {
		if (!tilePather.isEmpty()) {
			dx = (tilePather.currNode().getLeft()
					+ (Game.SCALE * Game.TILE_SIZE / 2) - getCenterX())
					/ deltaTime;
			dy = (tilePather.currNode().getTop()
					+ (Game.SCALE * Game.TILE_SIZE / 2) - getCenterY())
					/ deltaTime;
			if (frame.isContained(tilePather.currNode(), x, y)) {
				tilePather.nextNode();
			}

		}

		moving = (dx != 0 || dy != 0);

		if (moving) {
			Tile tile = Game.getMap().getWorldTile(frame.getCenterX(x),
					frame.getCenterY(y));
			dx = deltaTime * dx;
			dy = deltaTime * dy;
			float speed = deltaTime * this.speed;
			// Handle Speed //
			if (dx != 0 && dy != 0) {
				float hyp = 0.85f * speed;
				dx = Math.max(Math.min(hyp, dx), -hyp);
				dy = Math.max(Math.min(hyp, dy), -hyp);
			} else {
				dx = Math.max(Math.min(speed, dx), -speed);
				dy = Math.max(Math.min(speed, dy), -speed);
			}

			// Handle Facing //
			if (dy < 0) {
				facing = Facing.NORTH;
			} else if (dy > 0) {
				facing = Facing.SOUTH;
			} else if (dx > 0) {
				facing = Facing.EAST;
			} else if (dx < 0) {
				facing = Facing.WEST;
			}

			x += dx;
			collided = false;
			if (dx != 0 && frame.isColliding(Game.getMap(), x, y)) {
				collided = true;
				x -= dx;
			}
			y += dy;
			if (dy != 0 && frame.isColliding(Game.getMap(), x, y)) {
				collided = true;
				y -= dy;
			}
			dx = 0;
			dy = 0;
			if (tile != null) {
				Tile newTile = Game.getMap().getWorldTile(frame.getCenterX(x),
						frame.getCenterY(y));
				if (tile != null && newTile != null
						&& !tile.isSameNode(newTile)) {
					tile.removeEntity(this);
					newTile.addEntity(this);
				}
			}
			movingAnims[facing.ordinal()].update(deltaTime);
		}

		if (tilePather.isRunning()) {
			try {
				tilePather.generatePath(100);
			} catch (PathException e) {
				System.out.println(e.getMessage());
				tilePather.clear();
			}
		}

		pathGen();
		processMessages();

	}

	private void processMessages() {
		long currTime = System.currentTimeMillis();
		int delay = 200;
		for (int i = 0; i < messages.size(); i++) {
			long age = currTime - messages.get(i).getTime();
			if (age > delay && !messages.get(i).isBroadcast()) {
				broadcast(messages.get(i));
			}
			if (age > messages.get(i).getText().length() * 100 + delay) {
				messages.remove(i);
			}
		}
	}

	@Override
	public void tell(Message m) {
		if (m.getText().toLowerCase().indexOf("hey") > -1) {
			say("What's up? I'm " + name + "!");
		}
	}

	public void say(String text) {
		Message m = new Message(x, y, text, this);
		messages.add(m);
	}

	private void broadcast(Message m) {
		m.broadcast();
		for (Tile tile : Game.getMap().getLocale(m.getVolume(), getTileX(),
				getTileY())) {
			for (Entity h : tile.getEntities()) {
				if (h instanceof Talkable && !h.equals(this))
					((Talkable) h).tell(m);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void draw(float x, float y) {
		GUtil.drawSprite(getSpriteSheet(), getX() + x, getY() + y, getTexX(),
				getTexY(), Game.SCALE * Game.TILE_SIZE, Game.SCALE
						* Game.TILE_SIZE, 16);
		if (!messages.isEmpty()) {
			Message m = messages.get(messages.size() - 1);
			Rectangle rect = new Rectangle(
					(int) (m.getSender().getX() + x - (GUtil.textLength(m
							.getText()) - 16) / 2), (int) (m.getSender().getY()
							+ y - 60), (GUtil.textLength(m.getText())) + 32, 50);
			GUtil.drawBubble(rect, new Color(200, 200, 175));
			GUtil.drawText(rect.getX() + 16, rect.getY() + 16,
					ReadableColor.BLACK, m.getText());
		}
	}

	@Override
	protected SpriteSheet getSpriteSheet() {
		return SpriteSheet.PEOPLE;
	}

	@Override
	public void hit(Weapon w, Humanoid wielder) {
		health -= w.getDamage();
	}

	/**
	 * Gives the item to this humanoid.
	 * 
	 * @param item
	 *            Item to be given.
	 * @return Returns false if
	 */
	public boolean getItem(Holdable item) {
		System.out.println("Got item");
		if (heldItem != fist) {
			if (inventoryFull()) {
				return false;
			} else {
				stow();
				heldItem = item;
				return true;
			}
		} else {
			heldItem = item;
			return true;
		}
	}
	
	public void drop(){
		if (heldItem == fist){
			return;
		} else {
			heldItem.addToMap(x, y);
			heldItem = fist;
		}
	}

	public void retreive(int invIndex) {
		if (invIndex >= 0) {
			if (inventory[invIndex] == null){
				System.out.println("Fist!");
				getItem(fist);
			} else {
				Holdable item = inventory[invIndex];
				inventory[invIndex] = null;
				getItem(item);
			}
		}
	}

	public void stow() {
		if (inventoryFull()) {
			return;
		} else {
			inventory[inventoryFreeSpot()] = heldItem;
			heldItem = fist;
		}
	}
	
	public void swing(){
		heldItem.swing(this);
	}
	
	public void use(){
		heldItem.use(this);
	}

	public boolean inventoryFull() {
		return inventoryFreeSpot() == -1;
	}

	private int inventoryFreeSpot() {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public void setHealth(int h) {
		health = Math.min(maxHealth, h);
	}

	public int getHealth() {
		return health;
	}
	
	public void drawStatus(float x, float y){
		GUtil.drawRect(new Rectangle((int) x, (int) y, 260, 95), ReadableColor.DKGREY);
		GUtil.drawRect(new Rectangle((int) x + 10, (int) y+10, 75, 75), ReadableColor.GREY);
		if (heldItem != null){
			GUtil.drawSprite(SpriteSheet.ITEMS, x + 15, y+15, heldItem.getTexX(), heldItem.getTexY(), 70, 70, 16);
		}
		
		GUtil.drawText((int) x + 100, (int) y + 60, ReadableColor.WHITE, name);
		
		for (int i = 0; i < inventory.length; i++){
			GUtil.drawRect(new Rectangle((int) x+55*i + 90, (int) y+10, 50, 50), ReadableColor.GREY);
			if (inventory[i] != null)
				GUtil.drawSprite(SpriteSheet.ITEMS, x+55*i + 95, y+15, inventory[i].getTexX(), 
						inventory[i].getTexY(), 40, 40, 16);
		}
	}

}