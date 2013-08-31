package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import entities.Human;

public class GUI {
	private Dimension screenSize;
	private int TILE_SIZE;
	private BufferedImage[][] guiMap;
	private boolean inventoryOpen;
	private int invXOffset;
	private Human person;
	
	
	public GUI(Dimension screenSize, int tileSize, BufferedImage[][] guiMap, Human person){
		this.screenSize = screenSize;
		this.TILE_SIZE = tileSize;
		this.guiMap = guiMap;
		this.person = person;
	}
	
	public void toggleInventory(){
		inventoryOpen = !inventoryOpen;
	}
	
	public void setPerson(Human person){
		this.person = person;
	}
	
	public void update(long delta){
		//inventory manager
				if (inventoryOpen){
					if (invXOffset < 7*TILE_SIZE){
						invXOffset += 8;
					} else {
						invXOffset = 7*TILE_SIZE;
					}
				} else if (!inventoryOpen){
					if (invXOffset > 0){
						invXOffset -= 8;
					} else {
						invXOffset = 0;
					}
				}
				
				
	}
	
	public Image getInventoryImg(BufferedImage[][] guiMap, int tileSize){
		return getInventoryImg(guiMap, tileSize, null);
	}
	
	public Image getInventoryImg(BufferedImage[][] guiMap, int tileSize, int[] items){

		 int length = 9;
		 int width = 5;
		 int itemCount = 0;
		 
		 int type = guiMap[0][0].getType();
		 Image invImg = new BufferedImage((width + 2)*tileSize, (length + 2)*tileSize, type);
		 Graphics g = invImg.getGraphics();
		 
		 for (int i = 0; i <= length + 1; i++){
			 for (int j = 0; j <= width + 1; j++){
				 if(i == 0){
					 if (j == 0){
						 g.drawImage(guiMap[0][1], tileSize*j, tileSize*i, tileSize, tileSize, null);
					 }else{
						 g.drawImage(guiMap[1][1], tileSize*j, tileSize*i, tileSize, tileSize, null);
						 
						 //hearts
						 if (j <= person.hitPts){
							 g.drawImage(guiMap[4][0], tileSize*j, 5 + tileSize*i, tileSize, tileSize, null);
						 } else if (j <= width) {
							 g.drawImage(guiMap[3][0], tileSize*j, 5 + tileSize*i, tileSize, tileSize, null); 
						 }
					 }
				 }else if(i == length + 1){
					 if (j == 0){
						 g.drawImage(guiMap[0][3], tileSize*j, tileSize*i, tileSize, tileSize, null);
					 }else{
						 g.drawImage(guiMap[1][3], tileSize*j, tileSize*i, tileSize, tileSize, null);
					 }
				 }else if (j == 0){
					 g.drawImage(guiMap[0][2], tileSize*j, tileSize*i, tileSize, tileSize, null);
				 } else if (j == width + 1){
					 g.drawImage(guiMap[1][2], tileSize*j, tileSize*i, tileSize, tileSize, null);
				 } else {
					 /////insert items////
					 g.drawImage(guiMap[0][0], tileSize*j, tileSize*i, tileSize, tileSize, null);
					 itemCount++;
					 //System.out.println(itemCount + " -> " + person.getInvSize());
					 if (itemCount <= person.getInvSize()){
						 //System.out.println("cat");
						 g.drawImage(person.getInvArray()[itemCount].getImg(), tileSize*j, tileSize*i, tileSize, tileSize, null);
					 }
				 }
			 } 
		 }
		 
		 g.dispose();
		 return invImg;
	 }
	
	
	public Image getGUI(){
		
		Image guiImg = new BufferedImage(screenSize.width, screenSize.height, 2);
		Graphics g = guiImg.getGraphics();
		
		if (invXOffset > 0){
			g.drawImage(getInventoryImg(guiMap, TILE_SIZE), screenSize.width - invXOffset, 100, null);
		}
		
		for (int i = 0; i <= screenSize.width/TILE_SIZE; i++){
			g.drawImage(guiMap[1][3], i*TILE_SIZE, 0, null);
			g.drawImage(guiMap[1][1], i*TILE_SIZE, screenSize.height - 60, null);
		}
		for (int i = 0; i <= screenSize.height/TILE_SIZE; i++){
			g.drawImage(guiMap[2][2], 0, i*TILE_SIZE, null);
			g.drawImage(guiMap[0][2], (screenSize.width-TILE_SIZE), i*TILE_SIZE, null);
		}
		
		g.drawImage(guiMap[3][1], 0, 0, null);
		g.drawImage(guiMap[3][2], 0, screenSize.height - 60, null);
		g.drawImage(guiMap[4][1], (screenSize.width-TILE_SIZE), 0, null);
		g.drawImage(guiMap[4][2], (screenSize.width-TILE_SIZE), screenSize.height - 60, null);
		
		g.setColor(Color.white);
		Font font1 = new Font("Arial Bold", Font.BOLD, 20);
		g.setFont(font1);
		g.drawString("Name: " + person.name, 20, 20);
		
		return guiImg;
	}
	
}
