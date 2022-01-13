package solution3;

import java.util.concurrent.Semaphore;

public class Robot extends Thread {
	
	int id;
	int numActions;
	Buffer buffer;
	ItemDeposit items;
	Semaphore mutex;
	
	public long getId() {
		return id;
	}

	public Robot getRobot(int id) {
		if(id == this.id) {
			return this;
		}
		else return null;
	}
	
	public Robot (int id, Buffer buffer, ItemDeposit items) {
		this.id = id;
		this.buffer = buffer;
		this.items = items;
		this.numActions = 0;
		mutex = new Semaphore(1);
	}

	public void addAction() {
		this.numActions++;
	}
	
	public int getNumActions() {
		return numActions;
	}

	public Buffer getBuffer() {
		return buffer;
	}
	

	public ItemDeposit getItems() {
		return items;
	}

	@Override
	public void run() {
		Runnable action;
		try {
			while (!this.isInterrupted()) {
				
				action = buffer.get();
				
				sleep(1);
				action.run();
			}
		}catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
	
}
