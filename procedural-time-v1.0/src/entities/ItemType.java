package entities;

import java.awt.Image;

public class ItemType {
	private String itemName;
	private int itemId;
	private static int nextId;
	private Image itemImage;
	
	public ItemType(String itemName, Image itemImage){
		this.itemName = itemName;
		this.itemImage = itemImage;
		this.itemId = ItemType.nextId;
		ItemType.nextId++;
	}

	public String getItemName() {
		return itemName;
	}
	public int getItemId() {
		return itemId;
	}
	public Image getItemImage() {
		return itemImage;
	}


}
