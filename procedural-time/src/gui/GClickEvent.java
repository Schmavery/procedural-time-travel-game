package gui;

public class GClickEvent {
	public static enum EventType{BUTTON, MENU};
	private String action;
	private IElement source;
	private EventType type;
	
	public GClickEvent(String action, IElement source, EventType type){
		this.action = action;
		this.source = source;
		this.type = type;
	}
	
	public String getAction(){
		return this.action;
	}
	public IElement getSource(){
		return this.source;
	}
	public EventType getType(){
		return this.type;
	}
}
