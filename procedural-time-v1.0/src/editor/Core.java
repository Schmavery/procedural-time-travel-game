package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class Core implements KeyListener, MouseListener, MouseMotionListener{
	
	private ScreenManager sm;
	private boolean running, paused;
	protected boolean[] keys_pressed;
	public Point lastClick = new Point(0,0);
	public Point currMouseLocation = new Point(0,0);
	public boolean tileAdded = true;
	
	public void stop(){
		running = false;
		System.exit(0);
	}
	
	public void run(){
		init();
		gameLoop();
	}
	
	public void init(){
		keys_pressed = new boolean[256];
		sm = new ScreenManager("Chronobe");
		running = true;
		paused = false;
		
		sm.addKeyListener(this);
		sm.addMouseListener(this);
		sm.addMouseMotionListener(this);
	}


	public void togglePause(){
		paused = (! paused);
	}
	
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		long delta;
		long sleepTime;
		long calcTime;
		long sleepConst = 20;
		// keep looping round til the game ends

		while (running) {
			
			// work out how long its been since the last update
			delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			if(keys_pressed[80]){  //"p"
				togglePause();
				keys_pressed[80] = false;
			}
			if (! paused){
				update(delta);
			}
			draw(sm.getScreenSize(), sm.getGraphics(), delta);
			//sm.requestFocus();
			
			// flip the buffer over
			sm.updateGraphics();
			
			calcTime = System.currentTimeMillis() - lastLoopTime;
			if (calcTime < sleepConst) {sleepTime = sleepConst - calcTime;}
			else {sleepTime = 1;}
			// pause for a bit.
			try { Thread.sleep(sleepTime); } catch (Exception e) {}
		}
	}
	
	public abstract void update(long delta);
	public abstract void draw(Dimension dim, Graphics2D g, long delta);

	
	//keylistener
	public void keyPressed(KeyEvent e) {
		//System.out.println("cat");
		if( e.getKeyCode() >= 0 && e.getKeyCode() < 256 ) {
			//System.out.println(e.getKeyCode());
			keys_pressed[e.getKeyCode()] = true;
			}
		e.consume();
		}
	public void keyReleased(KeyEvent e) {
		if( e.getKeyCode() >= 0 && e.getKeyCode() < 256 ) {
			keys_pressed[e.getKeyCode()] = false;
			}
		e.consume();
		}
	public void keyTyped(KeyEvent e) {
		//wawd awe.consume();
	}
	
	//mouselistener
	public void mouseClicked(MouseEvent arg0) {
		lastClick = arg0.getPoint();
		tileAdded = false;
		//System.out.println("catcat" + (int) lastClick.getX());
		
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {currMouseLocation = e.getPoint();}
}
