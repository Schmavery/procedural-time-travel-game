package core;

import java.util.LinkedList;

import entities.Human;

public class Message {
	static LinkedList<Message> messages = new LinkedList<Message>();
	static LinkedList<Message> oldMessages = new LinkedList<Message>();
	
	private float x, y;
	private String text;
	private Human sender;
	private long time;
	
	private Message(float x, float y, String text, Human sender, long time){
		this.x = x;
		this.y = y;
		this.text = text;
		this.time = time;
		this.sender = sender;
	}
	
	public float getX(){return x;}
	public float getY(){return y;}
	public String getText(){return text;}
	public long getTime(){return time;}
	public Human getSender(){return sender;}
	
	public static void say(float x, float y, String text, Human sender){
		Message m = new Message(x, y, text, sender, System.currentTimeMillis());
		messages.add(m);
	}
	
	public static void clear(){
		messages.clear();
	}
	
	public static LinkedList<Message> getMessages(){
		return messages;
	}
	public static LinkedList<Message> getOldMessages(){
		return oldMessages;
	}
	
	public static void update(){
		long curTime = System.currentTimeMillis();
		while (!messages.isEmpty() && messages.getFirst().getTime() < curTime - 500){
			oldMessages.add(messages.removeFirst());
		}
//		for (Message m : oldMessages){
//			if (m.getTime() < curTime - (m.getText().length()*200)){
//				oldMessages.remove(m);
//			}
//		}
		while (!oldMessages.isEmpty() && oldMessages.getFirst().getTime() < curTime - 1000){
				oldMessages.removeFirst();
		}
	}
}