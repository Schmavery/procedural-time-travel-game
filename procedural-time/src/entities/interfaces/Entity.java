package entities.interfaces;


public interface Entity{
	public void draw(float x, float y);
	public void draw(float x, float y, float w, float h);
	public float getY();
	public int getDrawPriority();
}
