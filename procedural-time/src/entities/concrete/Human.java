package entities.concrete;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.Rectangle;

import core.ActionFactory;
import core.ActionFactory.Action;
import core.ActionFactory.ActionType;
import core.Game;
import core.Message;
import core.Tile;
import core.display.SpriteManager;
import core.path.PathFinder;
import entities.abstr.AbstractMovingEntity;
import entities.components.EntityFrame;
import entities.components.HealthTracker;
import entities.interfaces.Entity;
import entities.interfaces.Hittable;
import entities.interfaces.Holdable;
import entities.interfaces.Talkable;
import entities.interfaces.Weapon;
import gui.GUtil;
import gui.GUtil.SpriteSheetType;

public class Human extends AbstractMovingEntity implements Hittable,
		Talkable {
	
	public static enum Gender {
		MALE, FEMALE
	}

	public static Fist fist = new Fist();

	private List<Message> messages;
	String name;
	private HealthTracker health;
	Holdable[] inventory;
	Holdable heldItem;
	Action currentAction;
	

	// private Gender gender;

	public Human(float x, float y, Gender gender, String name) {
		super(x, y);

		// this.gender = gender;
		health = new HealthTracker(20);
		inventory = new Holdable[3];
		heldItem = fist;
		this.facing = Facing.SOUTH;
		frame = new EntityFrame(4, 6, 3);
		tilePather = new PathFinder<Tile>();
		speed = 0.1f*Game.SCALE;
		messages = new ArrayList<>(10);
		Game.getMap().getWorldTile(frame.getCenterX(x), frame.getCenterY(y))
				.addEntity(this);

		this.name = name;
		setSpecialType(SpecialType.PERSON);
	}

	public void update(long deltaTime) {
		if (frame.isColliding(Game.getMap(), x, y)){
			warpToClosestClearTile();
			//TODO: This doesn't always work
		}
		
		// TODO: This "getPlayer()" is too hard-coded
		if (!this.isMoving() && !this.isDead() && !this.equals(Game.getPlayer())){
//			if (rand.nextInt(100) == 1){
//				int destX = this.getTileX() + (rand.nextInt(10) - 5);
//				int destY = this.getTileY() + (rand.nextInt(10) - 5);
//				this.walkTo(destX, destY);
//			}
			if (rand.nextInt(5000) == 1){
				this.say("Hey.");
			}
		}
		
		
		processMessages();
		if (isDead()){
			return;
		}
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
			if (debug) speed *= 2;
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

		if (currentAction != null){
			if (!currentAction.update(deltaTime)){
				endAction(currentAction);
				currentAction = null;
			}
		}
		pathGen();
		
	}

	protected void processMessages() {
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
		if (isDead()){
			return;
		} else if (m.getText().toLowerCase().indexOf("hey") > -1) {
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

	@Override
	public void hit(Weapon w, Human wielder) {
		if (this != wielder){
			health.damage(w.getDamage());
		}
	}

	/**
	 * Gives the item to this humanoid.
	 * 
	 * @param item Item to be given.
	 * @return Returns false if
	 */
	public boolean getItem(Holdable item) {
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
	
	public void dropCurrentItem(){
		if (heldItem != fist){
			heldItem.addToMap(getX() + rand.nextInt(20) - 10, getY() + rand.nextInt(20) - 10);
			removeCurrentItem();
		}
	}
	
	public void removeCurrentItem(){
		if (heldItem != fist){
			heldItem = fist;
		}
	}

	private void retreive(int invIndex) {
		if (invIndex >= 0) {
			Holdable item = null;
			if (inventory[invIndex] == null){
				item = fist;
			} else {
				item = inventory[invIndex];
			}
			if (heldItem != fist){
				inventory[invIndex] = heldItem;
			} else {
				inventory[invIndex] = null;
			}
			heldItem = item;
		}
	}

	private void stow() {
		if (inventoryFull()) {
			return;
		} else {
			inventory[nextFreeInventorySpace()] = heldItem;
			heldItem = fist;
		}
	}
	
	public void doAction(ActionType aType){
		doAction(aType, 0);
	}
	
	public void doAction(ActionType aType, int index){
		if (currentAction != null){
			return;
		} else {
			switch (aType) {
				case DROP:
					currentAction = ActionFactory.drop();
					dropCurrentItem();
					break;
				case RETREIVE:
					currentAction = ActionFactory.retreive();
					retreive(index);
					break;
				case SWING:
					currentAction = ActionFactory.swing();
					heldItem.swing(this);
					break;
				case USE:
					currentAction = ActionFactory.use();
					heldItem.use(this);
					break;
				case DIE:
					System.out.println("DEATH ACTION");
					currentAction = ActionFactory.die();
					die();
			}
		}
	}
	
	public void endAction(Action act){
		// TODO
	}

	public boolean inventoryFull() {
		return nextFreeInventorySpace() == -1;
	}

	private int nextFreeInventorySpace() {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public Point getPlacePoint(){
		int centerOffset = (int)(0.5*Game.SCALE*Game.TILE_SIZE);
		int[] xMods = {0, 1, 0, -1};
		int[] yMods = {-1, 0, 1, 0};
		return new Point((int) (getX() + centerOffset + 2*centerOffset*xMods[facing.ordinal()]),
				(int) (getY() + centerOffset + 2*centerOffset*yMods[facing.ordinal()]));
	}
	
	public void die(){
		switch (rand.nextInt(3)) {
		case 0:
			say("Euurghh...");
			break;
		case 1:
			say("I will have my vengeance!");
			break;
		case 2:
			say("Ouch!");
			break;
		default:
			break;
		}
		
		//Drop Items
		while (heldItem != fist){
			dropCurrentItem();
		}
	}
	
	public void setHealth(int h) {
		if (health.isDead()) return;
		if (health.set(h)) die();
	}

	public int getHealth() {
		return health.get();
	}
	
	public boolean isDead() {
		return (health.isDead());
	}
	
	@Override
	public void draw(float x, float y) {
		if (isDead()){
			// TODO: Remove this hacky nonsense
			SpriteManager.get().getSprite(SpriteSheetType.PEOPLE, "grave").drawModel(x + getX(), y + getY(), 0);
		} else if (currentAction != null){
			float offset = (Game.TILE_SIZE*Game.SCALE) /2;
			currentAction.getAnim(facing).draw(x + getX() + offset, y + getY() + offset);
		} else {
			super.draw(x, y);
		}
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
		
		health.draw(getX()+x, getY()+y);
		if (debug){
			GUtil.drawPixel((int) (getPlacePoint().getX()+x), 
					(int) (getPlacePoint().getY()+y), 2, ReadableColor.BLACK);
			frame.draw(getX() + x, getY() + y);
		}
	}
	
	public void drawStatus(float x, float y){
		GUtil.drawRect(new Rectangle((int) x, (int) y, 260, 95), ReadableColor.DKGREY);
		GUtil.drawRect(new Rectangle((int) x + 10, (int) y+10, 75, 75), ReadableColor.GREY);
		if (heldItem != null){
			heldItem.draw(x+15, y+15, 70, 70);
		}
		
		GUtil.drawText((int) x + 100, (int) y + 60, ReadableColor.WHITE, name);
		
		for (int i = 0; i < inventory.length; i++){
			GUtil.drawRect(new Rectangle((int) x+55*i + 90, (int) y+10, 50, 50), ReadableColor.GREY);
			if (inventory[i] != null)
				inventory[i].draw( x+55*i + 95, y+15, 40, 40);
		}
	}

}