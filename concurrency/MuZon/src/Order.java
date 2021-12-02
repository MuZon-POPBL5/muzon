
public class Order {
	
	String action;
	Shelf shelf;
	Item item;
	
	public Order(boolean action, Shelf shelf, Item item) {
		if(action == true) this.action = "put";
		else this.action = "get";
		this.shelf = shelf;
		this.item = item;
	}

	public String getAction() {
		return action;
	}

	public Shelf getShelf() {
		return shelf;
	}

	public int getItemId() {
		return item.getId();
	}

	public Item getItem() {
		return item;
	}
	

}
