import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Robot extends Thread{
	int id;
	List<Item> items;
	List<Order> buffer;
	static final int MAX_BUFFER = 10;
	int order;
	PropertyChangeSupport conector;
	Semaphore lleno, vacio;
	Semaphore candado;
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		conector.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
    	conector.removePropertyChangeListener(listener);
    }

    
    
	public Robot(int id) {
		this.id = id;
		this.items = new ArrayList<>();
		int order = 0;
		buffer =new ArrayList<>();
		lleno = new Semaphore(MAX_BUFFER);
		vacio = new Semaphore(0);
		candado = new Semaphore (1);
		this.conector = new PropertyChangeSupport(this);
	}

	public void pickItem(Item item) {
		items.add(item);
	}
		
	public int validRobot(Shelf s) {
		int valid = 0;
		if (order == 0) valid = 2;
		else {
			for(Order o: buffer) {
				if(o.getShelf().equals(s)) valid = 1;
			}
		}
		return valid;
		
	}
	
	public void move() throws InterruptedException {
		for(Order o: buffer) {				
			if(o.getAction().equals("put")){
				System.out.println("Robot"+ this.id +" goes to delivery zone");
				Thread.sleep(1000);
				System.out.println("Robot"+ this.id +" picks Item " + o.getItemId());
				pickItem(o.getItem());
				Thread.sleep(1000);
				System.out.println("Robot"+ this.id +" has loaded " + o.getItem().getDescription());
				System.out.println("Robot"+ this.id +" goes to shelf" + o.getShelf().getId());
				Thread.sleep(1000);
				for(Item item : items) {
					if(item.getId() == o.getItemId()) {
						System.out.println("Robot " + this.id + " places Item: " + item.description);
						items.remove(item);
						Thread.sleep(1000);
						conector.firePropertyChange("put", -1, o);
					}
				}
			}
			if(o.getAction().equals("get")){
				System.out.println("Robot "+ this.id +" goes to shelf" + o.getShelf().getId());
				Thread.sleep(1000);
				System.out.println("Robot " + this.id + "picks the Item " + o.getItemId());
				conector.firePropertyChange("get", -1, o);
				
				for(Item item : o.getShelf().getItems()) {
					if(item.getId() == o.getItemId()) {
						System.out.println("Robot" + this.id + " takes Item: " + item.description);
						conector.firePropertyChange("get", -1, o);
						items.add(o.getShelf().getItem(o.getItemId()));
						Thread.sleep(1000);
					}
				}
			}
				
		}
	}
	
	public void put(Order o) throws InterruptedException{
		lleno.acquire();
		candado.acquire();
		buffer.add(o);
		candado.release();
		vacio.release();
	}
	public Order get() throws InterruptedException{
		Order order;
		vacio.acquire();
		candado.acquire();
		order = buffer.remove(0);
		candado.release();
		lleno.release();
		return order;
	}
}
