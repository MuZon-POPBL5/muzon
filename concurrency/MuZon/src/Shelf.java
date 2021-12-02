
import java.util.ArrayList;
import java.util.List;


public class Shelf{

	int id;
	List<Item> items;
	static final int MAX_CAPACITY = 10;
	
	
	public Shelf() {
		this.items = new ArrayList<>();
		
	}
	
	
	public List<Item> getItems() {
		return items;
	}


	public boolean putItem(Item item) {
		boolean resultado = false;
		if(this.items.size() < MAX_CAPACITY) {
			this.items.add(item);
			resultado = true;
		}
		return resultado;
	}
	
	
	public Item getItem(int id) {
		for(Item i : items) {
			if (i.getId() == id) {
				items.remove(i);
				return i;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Shelf) {
			if (((Item) obj).getId() == this.id) return true;
		}
		return false;
	}
	
	public int getId() {
		return id;
	}

	
}
