package gui;

import org.lwjgl.util.Rectangle;

public class GGridLayout implements ILayoutManager{

	private int rows, cols;
	private int vgap, hgap;
	
	public GGridLayout(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}
	
	public GGridLayout(int rows, int cols, int hgap, int vgap) {
		this.rows = rows;
		this.cols = cols;
		this.hgap = hgap;
		this.vgap = vgap;
	}

	@Override
	public void addLayoutComponent(IElement comp, String flag) {
		 // Don't do anything extra.
	}

	@Override
	public void layoutContainer(IContainer parent) {
		int width = (parent.getWidth() - (cols + 1)*hgap) / cols;
		int height = (parent.getHeight() - (rows + 1)*vgap) / rows;
		for (int i = 0; i < parent.getChildCount(); i++){
			parent.getChild(i).setRect(new Rectangle(
					(i%cols)*(width + hgap) + hgap + parent.getX(),
					(i/cols)*(height + vgap) + vgap + parent.getY(),
					width,
					height
					));;
		}
	}

	@Override
	public void removeLayoutComponent(IContainer parent) {
		 // Don't do anything extra.
	}
}
