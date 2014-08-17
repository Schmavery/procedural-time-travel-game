package ai;

import java.util.Random;

public class RandomSelector<T> extends AbstractSelector<T> {

	Random rand;
	
	public RandomSelector(int seed) {
		this(null, seed);
	}
	
	public RandomSelector(Node<? extends T> parent, int seed){
		super(parent);
		rand = new Random(seed);
	}

	@Override
	public void reset() {
		// No action necessary
	}

	@Override
	public ai.Node.NodeStatus execute(T owner) {
		return executeChildNode(rand.nextInt(getSequence().size() - 1), owner);
	}

}
