package entities;


public class Inventory {
	 Item[] itemArray;
	 int length, size;
	 
	 
	 public Inventory(int length){
		 this.length = length;
		 itemArray = new Item[length + 1];
	 }
	 
	 
	 // Changes size of inventory, returns extra item if reducing size of full inventory.
	 public Item[] setLength(int newLength){
		 this.length = newLength;
		 Item[] newArray = new Item[newLength];
		 if (newLength == length){
			 return null;
		 }else if (newLength >= size){
			 for (int i = 0; i < itemArray.length; i++){
				 newArray[i] = itemArray[i];
			 }
			 itemArray = newArray;
			 return null;
		 }else {
			 // Decrease size, return array of removed items.
			 Item[] extraItems = new Item[size - newLength];
			 
			 for (int i = 0; i < itemArray.length; i++){
				 if (i < newArray.length){
					 newArray[i] = itemArray[i];
				 } else {
					 extraItems[i - newLength] = itemArray[i];
				 }
			 }
			 itemArray = newArray;
			 return extraItems;
		 }
	}
	 
	public boolean addItem(Item item){
		if (size + 1 <= length){
			this.size++;
			itemArray[this.size] = item;
			return true;
		}
		return false;
	}
	 
}
