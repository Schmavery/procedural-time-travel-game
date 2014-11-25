package core.display;

import java.util.ArrayList;

public class Animation2 extends Sprite{

	String name;
	int pause;
	// TODO: Make this hold Images
	ArrayList<Image> frameIds;
	
	public Animation2(){
		frameIds = new ArrayList<>();
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	public void addFrame(Image img){
		frameIds.add(img);
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
	public void draw(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
}
