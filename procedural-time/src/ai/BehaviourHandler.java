package ai;

public class BehaviourHandler<T>{

	private Node rootNode;
	private T owner;
	
	public BehaviourHandler(){
		this(null);
	}
	
	public BehaviourHandler(Node tree){
		rootNode = tree;
	}
	
	public void offerEvent(){
		// TODO: Handle events.
	}
	
	public T getOwner(){
		return owner;
	}
}
