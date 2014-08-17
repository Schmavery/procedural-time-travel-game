package ai;

/**
 * Basic building block of the behaviour tree.
 * Any child can be less specific (superclass) than its parent,
 * but not more specific (subclass).
 */
public abstract class Node<T> {
	public static enum NodeStatus {RUNNING, SUCCESS, FAIL};
	protected Node<? extends T> parent;
	int priority;
	boolean running;
	
	public Node(Node<? extends T> p){
		this(p, 0);
	}
	
	public Node(Node<? extends T> parent, int priority){
		this.parent = parent;
		this.priority = priority;
	}
	
	public abstract NodeStatus execute(T owner);
	public abstract void reset();

	public Node<? extends T> getParent(){
		return parent;
	}
	public void setParent(Node<? extends T> p){
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
