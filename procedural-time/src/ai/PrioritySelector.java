package ai;

import java.util.Collections;
import java.util.Comparator;

public class PrioritySelector extends AbstractSelector {

	int current = 0;
	
	@Override
	public NodeStatus execute() {
		NodeStatus status = NodeStatus.SUCCESS;
		for (;current < getSequence().size(); current++){
			status = executeChildNode(current);
			
			if (status != NodeStatus.SUCCESS){
				break;
			}
		}
		
		return status;
	}

	@Override
	public void reset() {
		current = 0;
	}
	
	@Override
	protected void addNode(Node n){
		getSequence().add(n);
		n.setParent(this);

		Collections.sort(getSequence(), new Comparator<Node>() {
			@Override
			public int compare(Node n1, Node n2) {
				return n1.getPriority() - n2.getPriority();
			}
		});
	}

}
