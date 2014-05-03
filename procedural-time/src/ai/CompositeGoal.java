package ai;

import java.util.LinkedList;
import java.util.List;

public class CompositeGoal extends AbstractGoal{

	List<AbstractGoal> subgoals;
	
	public CompositeGoal() {
		subgoals = new LinkedList<>();
	}
	
	public void addSubgoal(AbstractGoal goal){
		subgoals.add(goal);
	}
	
	private GoalStatus processSubgoals(){
		// Remove completed or failed goals
		while (!subgoals.isEmpty() && 
				(subgoals.get(0).getStatus() == GoalStatus.COMPLETED || 
				subgoals.get(0).getStatus() == GoalStatus.FAILED)){
			subgoals.get(0).terminate();
			subgoals.remove(0);
		}
		
		// Continue processing
		if (!subgoals.isEmpty()){
			GoalStatus currStatus = subgoals.get(0).process();
			if (currStatus == GoalStatus.COMPLETED && subgoals.size() > 1){
				return GoalStatus.ACTIVE;
			} else {
				return currStatus;
			}
		} else {
			return GoalStatus.COMPLETED;
		}
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

	@Override
	public GoalStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}
}