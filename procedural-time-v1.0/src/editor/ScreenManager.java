package editor;

import java.awt.Canvas;
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ScreenManager extends Canvas implements KeyListener, MouseListener{
	private BufferStrategy strategy;
	private String GAME_TITLE;
	private Graphics2D g2d;
	private Dimension SCREEN_SIZE = new Dimension(800,600);
	private JFrame container;
	private JPanel panel;
	//public boolean[] keys_pressed = new boolean[256];
	
	/**
	 * Construct our game and set it running.
	 */
	public ScreenManager(String game_title) {
		// create a frame to contain our game
		GAME_TITLE = game_title;
		container = new JFrame(GAME_TITLE);
		container.setPreferredSize(SCREEN_SIZE);
		// get hold the content of the frame and set up the resolution of the game
		panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(SCREEN_SIZE);
		panel.setLayout(null);
		panel.setSize(SCREEN_SIZE);
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,600);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		//addKeyListener(this);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		//addKeyListener(new KeyInputHandler());
		
		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		//Create graphics2d object
		g2d = (Graphics2D) strategy.getDrawGraphics();
	}
	
	//KeyListener
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	//MouseListener
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public Graphics2D getGraphics(){
		return g2d;
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	public Dimension getScreenSize(){
		return SCREEN_SIZE;
	}
	
	public void updateGraphics(){
		//g2d.dispose();
		strategy.show();
	}

	

}

