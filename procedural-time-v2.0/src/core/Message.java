package core;

import java.util.LinkedList;

import entities.AbstractEntity;

public class Message {
	private float x, y;
	private String text;
	private AbstractEntity sender;
	private long time;
	private int volume;
	private boolean broadcast;
	
	public Message(float x, float y, String text, AbstractEntity sender){
		this(x, y, text, sender, 3);
	}
	
	public Message(float x, float y, String text, AbstractEntity sender, int volume){
		this.x = x;
		this.y = y;
		this.text = text;
		this.time = System.currentTimeMillis();
		this.sender = sender;
		this.volume = volume;
		this.broadcast = false;
	}

	public void broadcast(){
		this.broadcast = true;
	}
	
	public float getX(){return x;}
	public float getY(){return y;}
	public String getText(){return text;}
	public long getTime(){return time;}
	public AbstractEntity getSender(){return sender;}
	public int getVolume(){return volume;}
	public boolean isBroadcast(){return broadcast;}
	
}