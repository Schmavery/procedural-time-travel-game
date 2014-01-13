package gui;

public interface ILayoutManager {
	void addLayoutComponent(IElement comp, String flag);
	void layoutContainer(IContainer parent);
	void removeLayoutComponent(IContainer parent);
}
