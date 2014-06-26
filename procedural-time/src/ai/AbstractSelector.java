package ai;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSelector extends Node{
	private List<Node> sequence;
	
	public AbstractSelector(){
		this(null);
	}
	
	public AbstractSelector(Node parent) {
		super(parent);
		sequence = new ArrayList<>();
	}
	
	protected void addNode(Node n){
		getSequence().add(n);
		n.setParent(this);
	}
	protected List<Node> getSequence(){
		return sequence;
	}
	
	protected NodeStatus executeChildNode(int index){
		NodeStatus status = sequence.get(index).execute();
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
