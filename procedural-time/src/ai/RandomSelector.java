package ai;

import java.util.Random;

public class RandomSelector extends AbstractSelector {

	Random rand;
	
	public RandomSelector(int seed) {
		this(null, seed);
	}
	
	public RandomSelector(Node parent, int seed){
		super(parent);
		rand = new Random(seed);
	}
	
	@Override
	public Node.NodeStatus execute() {
		return executeChildNode(rand.nextInt(getSequence().size() - 1));
	}

	@Override
	public void reset() {
		// No action necessary
	}

}
