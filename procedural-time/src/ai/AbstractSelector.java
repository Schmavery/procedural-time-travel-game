package ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSelector extends Node{
	private List<Node> sequence;
	private List<Integer> running;
	
	public AbstractSelector(){
		this(null);
	}
	
	public AbstractSelector(Node parent) {
		super(parent);
		sequence = new ArrayList<>();
		running = new LinkedList<>();
	}
	
	protected void addNode(Node n){
		sequence.add(n);
		n.setParent(this);
	}
	protected List<Node> getSequence(){
		return sequence;
	}
	
	protected NodeStatus executeChildNode(int index){
		NodeStatus status = sequence.get(index).execute();
		switch (status){
		case RUNNING:
			if (!running.contains(index)){
				running.add(index);
			}
			break;
		case SUCCESS:
			if (running.contains(index)){
				running.remove(index);
			}
			break;
		case FAIL:
			if (running.contains(index)){
				running.remove(index);
			}
		}
		return status;
	}
}
