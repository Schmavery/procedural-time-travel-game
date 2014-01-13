package entities.interfaces;

import core.Message;

public interface Talkable extends Entity{
	public void tell(Message m);
}
