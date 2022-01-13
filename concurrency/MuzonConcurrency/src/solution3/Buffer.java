package solution3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Buffer {
	
	int capacity;
	int numActions;
	List<Runnable> buffer;
	Semaphore empty, full;
	Semaphore mutex;
	
	public Buffer (int capacity) {
		buffer = new ArrayList<>();
		this.capacity = capacity;
		numActions = 0;
		full = new Semaphore(capacity);
		empty = new Semaphore (0);
		mutex = new Semaphore (1);
	}
	public void put(Runnable action) throws InterruptedException {
		
		
		full.acquire();	
		mutex.acquire();
		buffer.add(action);
		numActions++;
		mutex.release();
		empty.release();
		
		
	}
	public Runnable get() throws InterruptedException {
		Runnable action = null;
		
		empty.acquire();
		mutex.acquire();
		action = buffer.remove(0);
		numActions--;
		mutex.release();
		full.release();

		return action;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getNumActions() {
		return numActions;
	}
	public boolean empty() {
		
		return (buffer.size() == 0);
	}
}
