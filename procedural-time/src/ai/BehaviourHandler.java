package ai;

public class BehaviourHandler<T>{

	private Node<T> rootNode;
	private T owner;
	
	public BehaviourHandler(){
		this(null);
	}
	
	public BehaviourHandler(Node<T> tree){
		rootNode = tree;
	}
	
	public void offerEvent(){
		// TODO: Handle events.
	}
	
	public void run(){
		// TODO: Execute tree
		rootNode.execute(owner);
	}
	
	public T getOwner(){
		return owner;
	}
}
