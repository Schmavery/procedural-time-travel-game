package entities.interfaces;


public interface Entity{
	public static enum SpecialType {PERSON, HOUSE, FOLIAGE, NORMAL, PATH};
	public void draw(float x, float y);
	public void draw(float x, float y, float w, float h);
	public float getX();
	public float getY();
	public int getDrawPriority();
	public SpecialType getSpecialType();
	public void setDebug(boolean on);
}
