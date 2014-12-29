package core.display;

/**
 * The only class that should be used to draw sprites.
 */
public class SpriteInstance {
	private Sprite model;
	private long time;
	private boolean repeat;
	
	public SpriteInstance(Sprite model) {
		this(model, true);
	}
	
	public SpriteInstance(Sprite model, boolean repeat) {
		this.model = model;
		this.time = 0;
		this.repeat = repeat;
	}
	
	public void restart(){
		time = 0;
	}
	
	public Sprite getModel(){
		return this.model;
	}
	
	/**
	 * Should only be called for sprites that are 
	 * visible, in order to save processing time.
	 * Repeating animation are considered to have
	 * no end.
	 * @param deltaTime - Time since last update
	 * @return boolean - Whether the animation has reaches its end.
	 */
	public boolean update(long deltaTime){
		time += deltaTime;
		if (time >= model.getAnimTime() && repeat){
			time %= model.getAnimTime();
			return false;
		} else if (!repeat){
			time = model.getAnimTime()-1;
		}
		return true;
	}
	
	public void draw(float x, float y){
		model.drawModel(x, y, (int) (time / model.getPause()));
	}
	
	public void draw(float x, float y, float width, float height){
		model.drawModel(x, y, width, height, (int) (time / model.getPause()));
	}
	
	@Override
	public String toString(){
		if (model instanceof Image){
			return "SpriteModel[Img]:"+((Image) model).getId();
		} else if (model instanceof Animation){
			String nums = "";
			for (Image i : ((Animation) model).getFrames()){
				nums += i.getId()+" ";
			}
			return "SpriteModel[Anim]:"+nums;
		} else {
			return "Unknown SpriteModel type";
		}
	}
}
