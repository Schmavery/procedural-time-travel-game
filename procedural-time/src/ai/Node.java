package ai;


public abstract class Node {
	public static enum NodeStatus {RUNNING, SUCCESS, FAIL};
	Node parent;
	int priority;
	boolean running;
	
	public Node(Node p){
		this(p, 0);
	}
	
	public Node(Node parent, int priority){
		this.parent = parent;
		this.priority = priority;
	}
	
	public abstract NodeStatus execute();
	public abstract void reset();

	public Node getParent(){
		return parent;
	}
	public void setParent(Node p){
		this.parent = p;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void setPriority(int p){
		this.priority = p;
	}
	
}
