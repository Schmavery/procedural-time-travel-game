package gui;

public interface GLayoutManager {
	void addLayoutComponent(GContainer c);
	void layoutContainer(GContainer c);
	void removeLayoutComponent(GComponent comp);
}
