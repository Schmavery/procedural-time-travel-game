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
	
	/**
	 * Should only be called for sprites that are 
	 * visible, in order to save processing time.
	 * @param deltaTime - Time since last update
	 */
	public void update(long deltaTime){
		time = (time + deltaTime) % model.getAnimTime();
		index = (int) (time / model.getPause());
	}
	
	public void draw(float x, float y){
		model.drawModel(x, y, index);
	}
	
	@Override
	public String toString(){
		if (model instanceof Image){
			return "SpriteModel[Img]:"+((Image) model).getId();
		} else {
			String nums = "";
			for (Image i : ((Animation2) model).getFrames()){
				nums += i.getId()+" ";
			}
			return "SpriteModel[Anim]:"+nums;
		}
	}
}
