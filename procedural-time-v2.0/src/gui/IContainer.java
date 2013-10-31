package gui;

public interface IContainer extends IElement{

	public boolean addChild(IElement e);
	public IElement getChild(String name);
	public IElement getChild(int index);
	public int getChildCount();
	public void setLayout(ILayoutManager layout);
	public void layout();
}