package gui;

import java.util.List;

public interface IContainer extends IElement{

	public boolean addChild(IElement e);
	public IElement getChild(String name);
	public IElement getChild(int index);
	public List<IElement> getChildren();
	public int getChildCount();
	public void setLayout(ILayoutManager layout);
	public void layout();
}