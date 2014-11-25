package core.display;

import gui.GUtil.SpriteSheetType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SpriteSheet {
	private SpriteSheetType type;
	private String dataPath;
	ArrayList<Image> imgs;
	
	public SpriteSheet(SpriteSheetType type, String dataPath){
		this.type = type;
		this.dataPath = dataPath;
	}
	
	public SpriteSheetType getType(){
		return type;
	}
	
	public String getData(){
		try (BufferedReader br = new BufferedReader(new FileReader(new File(dataPath))))
		{
			StringBuilder sb = new StringBuilder();
			String str = br.readLine();
			while (str != null){
				sb.append(str);
				str = br.readLine();
				
			}
			return sb.toString();
		} catch (FileNotFoundException e){
			System.out.println("No data file exists for this image.");
		} catch (IOException e){
			e.printStackTrace();
		}
		return "";
	}
	
	public Image getImage(int id){
		return imgs.get(id);
	}
}
