package core.display;

/**
 * The only class that should be used to draw sprites.
 */
public class SpriteInstance {
	Sprite model;
	long time;
	int index;
	
	public SpriteInstance(Sprite model) {
		this.model = model;
		time = 0;
		index = 0;
	}
	
	public void reset(){
		time = 0;
	}
	
	public void update(long deltaTime){
		time += deltaTime;
		//TODO: reset on overflow, calc id
	}
	
	public void draw(float x, float y){
		model.drawModel(x, y);
		//TODO: frames
	}
}
