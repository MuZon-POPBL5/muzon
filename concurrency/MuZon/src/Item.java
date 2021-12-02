
public class Item {

	
	int id;
	String description;
	
	public void Item(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Item) {
			if (((Item) obj).getId() == this.id) return true;
		}
		return false;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
