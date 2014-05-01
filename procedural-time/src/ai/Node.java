package ai;


public abstract class Node {
	public static enum NodeStatus {RUNNING, SUCCESS, FAIL};
	Node parent;
	int priority;
	
	public Node(Node p){
		this(p, 0);
	}
	
	public Node(Node parent, int priority){
		this.parent = parent;
		this.priority = priority;
	}
	
	public abstract NodeStatus execute();

	public Node getParent(){
		return parent;
	}
	public void setParent(Node p){
		this.parent = p;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public void setPriority(int p){
		this.priority = p;
	}
	
}
