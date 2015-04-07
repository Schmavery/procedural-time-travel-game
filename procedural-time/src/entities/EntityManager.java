package entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.interfaces.Entity;

public class EntityManager {
	Set<Entity> master;
	List<Entity> add, remove;
	
	public EntityManager(){
		master = new HashSet<Entity>();
		add = new ArrayList<>();
		remove = new ArrayList<>();
	}
	
	public void update(long deltaTime){
		master.removeAll(remove);
		remove.clear();
		master.addAll(add);
		add.clear();
		for (Entity e : master){
			e.update(deltaTime);
		}
	}
	
	public void addEntity(Entity e){
		add.add(e);
	}
	
	public void removeEntity(Entity e){
		remove.remove(e);
	}
}
