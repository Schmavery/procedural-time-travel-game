package ai;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSelector<T> extends Node<T>{
	private List<Node<? super T>> sequence;
	
	public AbstractSelector(){
		this(null);
	}
	
	public AbstractSelector(Node<? extends T> parent) {
		super(parent);
		sequence = new ArrayList<>();
	}
	
	public void addNode(Node<? super T> n){
		getSequence().add(n);
		n.setParent(this);
	}
	
	protected List<Node<? super T>> getSequence(){
		return sequence;
	}
	
	protected NodeStatus executeChildNode(int index, T owner){
		NodeStatus status = sequence.get(index).execute(owner);
		switch (status){
		case RUNNING:
			running = true;
			break;
		case SUCCESS:
			running = false;
			break;
		case FAIL:
			running = false;
			break;
		}
		return status;
	}
}
