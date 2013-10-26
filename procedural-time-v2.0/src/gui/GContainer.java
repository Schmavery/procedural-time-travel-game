package gui;

public interface GContainer {

	public boolean addChild(GComponent e);
	public void setLayout(GLayoutManager layout);
	public GComponent getChild(String name);

}