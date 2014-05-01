package ai;

import java.util.LinkedList;
import java.util.List;

public class CompositeGoal extends AbstractGoal{

	List<AbstractGoal> subgoals;
	
	public CompositeGoal() {
		subgoals = new LinkedList<>();
	}
	
	public void addSubgoal(AbstractGoal goal){
		
	}
	
	@Override
	public void activate() {
		for(AbstractGoal goal : subgoals){
			goal.terminate();
		}
		subgoals.clear();
	}

	@Override
	public GoalStatus process() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage() {
		// TODO Auto-generated method stub
		return false;
	}
}