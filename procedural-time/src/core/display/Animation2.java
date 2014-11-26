package core.display;

import gui.GUtil.SpriteSheetType;

import java.util.ArrayList;

public class Animation2 extends Sprite{

	String name;
	int pause;
	ArrayList<Image> frameIds;
	
	public Animation2(SpriteSheetType ssType){
		super(ssType);
		frameIds = new ArrayList<>();
	}
	
	public long getAnimTime(){
		return pause*frameIds.size();
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
	
	@Override
	public int getPause(){
		return pause;
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
	public void drawModel(float x, float y, int index) {
		// TODO Auto-generated method stub
		
	}
	
}
