package core.path;

import java.util.List;

public interface NeighbourFunction<T> {
	public List<T> getNeighbours(T node);
}
