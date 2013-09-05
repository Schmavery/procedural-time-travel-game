package core;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.*;
import org.lwjgl.*;
//import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

 
public class Core {
	public static int SCREEN_HEIGHT = 640;
	public static int SCREEN_WIDTH = 640;
	
	private long lastFrame;
	
    public Core(){
    	init();
    	gameLoop();
    }
    	
    public void init(){
        try {
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.setTitle("Procedural Time");
            Display.create();
        } catch (LWJGLException e) {
            System.err.println("Display wasn't initialized correctly.");
            System.exit(1);
        }
        
        // GL init code
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
//		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); <<-- for some reason
//		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); <<-- these don't work
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
    }
    
    private void gameLoop(){
        while (!Display.isCloseRequested()) {
        	// Render
        	glClear(GL_COLOR_BUFFER_BIT);
        	update(getDelta());
        	draw();
        	//if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Keyboard.isKeyDown(Keyboard.KEY_Q))
        	//	break;
        	
        	//int mouse_x = Mouse.getX();
        	//int mouse_y = SCREEN_HEIGHT - Mouse.getY() - 1;
        	
            Display.update();
            Display.sync(60);
        }
 
        exit();
    }

	public void update(long delta) {}
	public void draw() {}
	
	private long getDelta(){
		long currentTime = (Sys.getTime()*1000)/Sys.getTimerResolution();
		long delta = currentTime - lastFrame;
		lastFrame = currentTime;
		return delta;
	}
	
	public void exit(){
		Display.destroy();
        System.exit(0);
	}
    

}