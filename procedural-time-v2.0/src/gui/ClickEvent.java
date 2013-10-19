package gui;

public class ClickEvent {
	public static enum EventType{BUTTON, MENU};
	private String action;
	private String source;
	private EventType type;
	
	public ClickEvent(String action, String source, EventType type){
		this.action = action;
		this.source = source;
		this.type = type;
	}
	
	public String getAction(){
		return action;
	}
	public String getSource(){
		return source;
	}
	public EventType getType(){
		return type;
	}
}
