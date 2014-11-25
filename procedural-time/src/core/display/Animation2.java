package core.display;

import java.util.ArrayList;

public class Animation2 extends Sprite{

	String name;
	int pause;
	ArrayList<Image> frameIds;
	
	public Animation2(SpriteSheet ss){
		super(ss);
		frameIds = new ArrayList<>();
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	public void addFrame(Image img){
		this.frameIds.add(img);
	}
	
	public void setPause(int pause){
		this.pause = pause;
	}

	@Override
	public int getTexX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTexY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void drawModel(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
}
