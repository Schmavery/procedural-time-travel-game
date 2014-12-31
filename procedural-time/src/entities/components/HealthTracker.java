package entities.components;

import gui.GUtil;
import gui.GUtil.SpriteSheetType;

import org.lwjgl.util.ReadableColor;

public class HealthTracker {
	private int maxHealth;
	private int health;
	
	public HealthTracker(int maxHealth){
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
	
	public boolean isDead(){
		return (health == 0);
	}
	
	public boolean set(int h){
		health = Math.min(maxHealth, Math.max(0, h));
		return isDead();
	}
	
	/**
	 * Use this method to displace the health value
	 * from the current.
	 * @param h Value to be subtracted from the current health.
	 * @return false if the change kills the entity.
	 */
	public boolean damage(int d){
		health = Math.min(maxHealth, Math.max(0, health-d));
		return isDead();
	}
	
	public int get(){
		return health;
	}
	
	public int getMax(){
		return maxHealth;
	}
	
	public void draw(float x, float y){
		if (health < maxHealth && !isDead()){
			int len = 80;
			int amt = (int) ((float) health / maxHealth * len);
			GUtil.drawSprite(SpriteSheetType.GUI, x-16, y-16, 1, 5, len+4, 10, 32, ReadableColor.GREY);
			GUtil.drawSprite(SpriteSheetType.GUI, x-14, y-14, 1, 5, len, 6, 32, ReadableColor.DKGREY);
			GUtil.drawSprite(SpriteSheetType.GUI, x-14, y-14, 1, 5, amt, 6, 32, ReadableColor.GREEN);
		}
	}
}
