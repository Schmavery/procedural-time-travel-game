package ai;

import java.util.Collections;
import java.util.Comparator;

public class PrioritySelector<T> extends AbstractSelector<T> {

	int current = 0;
	Comparator<Node<? super T>> comp = new Comparator<Node<? super T>>() {
		@Override
		public int compare(Node<? super T> n1, Node<? super T> n2) {
			return n1.getPriority() - n2.getPriority();
		}
	};
	
	@Override
	public NodeStatus execute(T owner) {
		NodeStatus status = NodeStatus.SUCCESS;
		for (;current < getSequence().size(); current++){
			status = executeChildNode(current, owner);
			
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
	public void addNode(Node<? super T> n){
		getSequence().add(n);
		n.setParent(this);

		Collections.sort(getSequence(), comp);
	}

}
