package editor;
import java.awt.Image;
import java.util.ArrayList;


public class Animation {

	private ArrayList<OneScene> scenes;
	private int sceneIndex;
	private long movieTime;
	private long totalTime;	
	
	public Animation(){
		scenes = new ArrayList<OneScene>();
		totalTime = 0;
		start();
	}
	
	// add scene to ArrayList and add time for each scene.
	public synchronized void addScene(Image pic, long time){
		totalTime += time;
		scenes.add(new OneScene(pic, totalTime));
	}
	
	//start animation from beginning
	public synchronized void start(){
		movieTime = 0;
		sceneIndex = 0;
	}
	
	// change scenes
	public synchronized void updateScene(long timePassed){
		if(scenes.size()>1){
			movieTime += timePassed;
			if(movieTime >= totalTime){
				movieTime = 0;
				sceneIndex = 0;
			}
			
			while(movieTime > getScene(sceneIndex).endTime){
				sceneIndex++;
			}
		}
	}
	
	public synchronized Image getImage(){
		if(scenes.size()==0){
			return null;
		}else{
			return getScene(sceneIndex).pic;
		}
	}
	
	//getScene
	
	private OneScene getScene(int x){
		return (OneScene)scenes.get(x);
	}
	
	// PRIVATE INNER CLASS 
	
	public class OneScene{
		Image pic;
		long endTime;
		
		public OneScene(Image pic, long endTime){
			this.pic = pic;
			this.endTime = endTime;
		}
	}
	
	
}
