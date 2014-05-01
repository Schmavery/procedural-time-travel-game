package ai;

public abstract class AbstractGoal {
	public static enum GoalStatus {INACTIVE, ACTIVE, COMPLETED, FAILED};
	public abstract void activate();
	public abstract GoalStatus process();
	public abstract void terminate();
	public abstract boolean handleMessage();
}
