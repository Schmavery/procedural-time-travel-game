package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class GFont
{
	private Texture tex;
	private Map<Integer, GChar> chars;
	private Map<String, String> properties;
	
	public GFont(String path){
		properties = new HashMap<>();
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String s = null;
			while((s = br.readLine()) != null) {
				parseLine(s);
			}
			fr.close();
			System.out.println("Loaded font file from '" + path + "'.");
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void parseLine(String line){
		loadProperties(line);
		if (line.indexOf("page") == 0){
			loadSpriteSheet(properties.get("file"));
		}else if (line.indexOf("chars") == 0){
			chars = new HashMap<>(getPropAsInt("count"));
		} else if (line.indexOf("char") == 0){
			chars.put(getPropAsInt("id"), makeChar());
		}
	}
	
	private void loadProperties(String line){
		for (String w : splitLine(line)){
			String[] splitW = w.split("=");
			if (splitW.length == 2){
				properties.put(splitW[0], splitW[1].replace("\"", ""));
			}
		}
	}
	
	private String[] splitLine(String line){
		return line.split("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	}

	public int getPropAsInt(String key){
		return Integer.valueOf(properties.get(key));
	}
	
	private GChar makeChar(){
		return new GChar(getPropAsInt("id"),
						getPropAsInt("x"),
						getPropAsInt("y"),
						getPropAsInt("width"),
						getPropAsInt("height"),
						getPropAsInt("xoffset"),
						getPropAsInt("yoffset"),
						getPropAsInt("xadvance")); 
	}
	
	private void loadSpriteSheet(String path){
		try
		{
			tex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + path)), GL11.GL_NEAREST);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public int drawChar(char c, int xPos, int yPos){
		return getChar(c).draw(xPos, yPos, tex.getTextureHeight());
	}
	
	public int stringLength(String str){
		int len = 0;
		for (int i = 0; i < str.length(); i++){
			len += getChar(str.charAt(i)).getxAdvance();
		}
		return len;
	}
	
	public GChar getChar(char c){
		if (chars.containsKey((int) c)){
			return chars.get((int) c);
		} else {
			System.out.println("Unprintable character " + c);
			return chars.get((int) ' ');
		}
	}
	
	public Texture getTex(){
		return tex;
	}
	
}
