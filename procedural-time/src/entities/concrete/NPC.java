package entities.concrete;

public class NPC extends Human {
	
	public NPC(float x, float y, Gender gender, String name) {
		super(x, y, gender, name);
	}
	
	@Override
	public void update(long deltaTime){
		super.update(deltaTime);
	}
	
}
