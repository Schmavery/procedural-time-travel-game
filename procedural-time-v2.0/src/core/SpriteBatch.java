package core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector4f;

/**
 * Made with the help of http://www.java-gaming.org/topics/basic-spritebatcher-with-lwjgl/27730/view.html
 */
public class SpriteBatch {
	private static float[] empty = new float[8];
	private static Vector4f empty1 = new Vector4f(0,0,0,0);
	
	private float[] vertArray;
	private byte[] colorArray;
	private float[] texArray;
	private int draws;
	private int maxDraws = 1000;
	private int vertIndex;
	private int colIndex;
	private int texIndex;
	private int currentTex;
	private FloatBuffer vertBuff, texBuff;
	private ByteBuffer colBuff;
	
	static{
		empty[0] = 0;
		empty[1] = 0;
		empty[2] = 1;
		empty[3] = 0;
		empty[4] = 1;
		empty[5] = 1;
		empty[6] = 0;
		empty[7] = 1;
	}
	
	public SpriteBatch()
	{
		this(1000);
	}
	
	public SpriteBatch(int size)
	{
		vertArray = new float[size*2*4];
		vertBuff = BufferUtils.createFloatBuffer(vertArray.length);
		colorArray = new byte[size*4*4];
		colBuff = BufferUtils.createByteBuffer(colorArray.length);
		texArray = new float[size*2*4];
		texBuff = BufferUtils.createFloatBuffer(texArray.length);
		vertIndex = 0;
		colIndex = 0;
		texIndex = 0;
		maxDraws = size;
		draws = 0; 
	}
	
	public void begin()
	{
		glEnableClientState(GL11.GL_VERTEX_ARRAY);
		glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL11.GL_COLOR_ARRAY);
	}
	
	public void end()
	{
		render();
	        
		glDisableClientState(GL11.GL_VERTEX_ARRAY);
		glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL11.GL_COLOR_ARRAY);
	}
	
	private byte getColor(float f)
	{
		return (byte) (f*255);
	}
	private void render()
	{
		glBindTexture(GL11.GL_TEXTURE_2D, currentTex);
		vertBuff.put(vertArray);
		vertBuff.flip();
		colBuff.put(colorArray);
		colBuff.flip();
		texBuff.put(texArray);
		texBuff.flip();
        glVertexPointer(2, 0, vertBuff);
		glColorPointer(4,true, 0, colBuff);
        glTexCoordPointer(2, 0, texBuff);
        glDrawArrays(GL_QUADS, 0, draws*4);
        vertBuff.clear();
        colBuff.clear();
        texBuff.clear();
        vertIndex = 0;
		colIndex = 0;
		texIndex = 0;
		draws = 0; 
	}
	
	public void setTexture(int tex)
	{
		if(tex != currentTex)
		{
			render();
			currentTex = tex; 
		}
	}
	
	public void draw(int texID, float x, float y, float sizex, float sizey )
	{
		draw(texID, empty, x, y, sizex, sizey, empty1);
	}

	public void draw(int texID, float x, float y, float sizex, float sizey,float rotation, Vector4f col )
	{
		draw(texID, empty, x, y, sizex, sizey, col);
	}
	
	public void draw(int texID, float[] region, float x, float y, float width, float height, ReadableColor c){
		draw(texID, region, x, y, width, height, 
				new Vector4f(c.getRedByte()/255f, c.getGreenByte()/255f, c.getBlueByte()/255f, c.getAlphaByte()/255f));
	}
	
	public void draw(int texID, float[] region, float x, float y, float width, float height, Vector4f col )
	{
		if(texID != currentTex)
		{
			render();
			currentTex = texID; 
		}
		if(draws == maxDraws)
		{
			render();
		}	
		
		vertArray[vertIndex] 	= x;
		texArray[texIndex] 		= region[0];
		vertArray[vertIndex+1] 	= y;
		texArray[texIndex+1] 	= region[1];
		
		vertArray[vertIndex+2] 	= x + width;
		texArray[texIndex+2] 	= region[2];
		vertArray[vertIndex+3] 	= y;
		texArray[texIndex+3] 	= region[3];
		
		vertArray[vertIndex+4] 	= x + width;
		texArray[texIndex+4] 	= region[4];
		vertArray[vertIndex+5] 	= y + height;
		texArray[texIndex+5] 	= region[5];
		
		vertArray[vertIndex+6] 	= x;
		texArray[texIndex+6] 	= region[6];
		vertArray[vertIndex+7] 	= y + height;
		texArray[texIndex+7] 	= region[7];
		
		colorArray[colIndex]  	= getColor(col.x);
		colorArray[colIndex+1] 	= getColor(col.y);
		colorArray[colIndex+2] 	= getColor(col.z);
		colorArray[colIndex+3] 	= getColor(col.w);
		
		colorArray[colIndex+4] 	=  getColor(col.x);
		colorArray[colIndex+5] 	=  getColor(col.y);
		colorArray[colIndex+6] 	=  getColor(col.z);
		colorArray[colIndex+7] 	=  getColor(col.w);
		
		colorArray[colIndex+8] 	=  getColor(col.x);
		colorArray[colIndex+9] 	=  getColor(col.y);
		colorArray[colIndex+10] =  getColor(col.z);
		colorArray[colIndex+11] =  getColor(col.w);
		
		colorArray[colIndex+12] =  getColor(col.x);
		colorArray[colIndex+13] =  getColor(col.y);
		colorArray[colIndex+14] =  getColor(col.z);
		colorArray[colIndex+15] =  getColor(col.w);
		
		
		vertIndex+=8;
		texIndex+=8;
		colIndex += 16;
		draws++; 
	}
	
}