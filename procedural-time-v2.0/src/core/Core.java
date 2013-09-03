package core;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.*;
import org.lwjgl.*;
//import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

 
public class Core {
	public static int SCREEN_HEIGHT = 480;
	public static int SCREEN_WIDTH = 640;
	
    public Core(){
        try {
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.setTitle("Input Demo");
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
        glEnable(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
 
        while (!Display.isCloseRequested()) {
        	// Render
        	glClear(GL_COLOR_BUFFER_BIT);
        	update();
        	//if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Keyboard.isKeyDown(Keyboard.KEY_Q))
        	//	break;
        	
        	//int mouse_x = Mouse.getX();
        	//int mouse_y = SCREEN_HEIGHT - Mouse.getY() - 1;
        	
            Display.update();
            Display.sync(60);
        }
 
        Display.destroy();
        System.exit(0);
    }

	private void update() {}
    

}