import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Warehouse implements PropertyChangeListener{

	List<Shelf> shelves;
	List<Robot> robots;
	
	public Warehouse() {
		this.shelves = new ArrayList<>();
		this.robots = new ArrayList<>();
	}
	
	public void addShelf(Shelf shelf) {
		this.shelves.add(shelf);
	}
	
	public Shelf getShelf(int id) {
		for(Shelf s: shelves) {
			if(s.getItem(id) != null) {
				return s;
			}
		}
		return null;
	}
	
	public Order createOrder(boolean action, Item item) {
		return new Order(action, getShelf(item.getId()), item);
	}
	
	public void addRobot(Robot robot) {
		robots.add(robot);
		robot.addPropertyChangeListener(this);
	}
	
	public void assignOrder(Order o) throws InterruptedException {
		boolean added = false;
		for(Robot r : robots) {
			if(r.validRobot(o.getShelf()) == 2 && !added) {
				r.put(o);
				added = true;
			}
		}
		if(!added) {
			for(Robot r : robots) {
				if(r.validRobot(o.getShelf()) == 1 && !added) {
					r.put(o);
					added = true;
				}	
			}
		}
		if(!added) {
			for(Robot r : robots) {
				if(!added) {
					r.put(o);
					added = true;
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String key = evt.getPropertyName();
		Order o = (Order) evt.getNewValue();
		Item i;
		for(Shelf s : shelves) {
			if(s.getId() == o.getShelf().getId()) {
				if(key.equals("put")) {
					s.putItem(o.getItem());
					System.out.println("Shelf " + s.getId() +" loaded the Item " + o.getItemId());
				}
				if(key.equals("get")) {
					i = s.getItem(o.getItemId());
					System.out.println("Shelf " + s.getId() +" now doesnt have the Item " + o.getItemId());
				}
			}
				
		}
	}
	
	
}
