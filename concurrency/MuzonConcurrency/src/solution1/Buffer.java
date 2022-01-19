package solution1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


/**
 * @file Buffer.java
 * @brief This class creates a buffer to store the orders assigned to the robot. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/
public class Buffer {
	
	int capacity;
	List<Runnable> buffer;
	Semaphore empty, full;
	Semaphore mutex;
	
	public Buffer (int capacity) {
		buffer = new ArrayList<>();
		this.capacity = capacity;
		full = new Semaphore(capacity);
		empty = new Semaphore (0);
		mutex = new Semaphore (1);
	}
	
	
	/**
	 * @brief Using this the buffer adds a Runnable to the buffer.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @param Runnable action
	 * @return void
	*/	
	public void put(Runnable action) throws InterruptedException {
		
		
		full.acquire();
		mutex.acquire();
		buffer.add(action);
		mutex.release();
		empty.release();
		
	}
	
	/**
	 * @brief Using this the buffer gets a Runnable from the buffer.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return Runnable
	*/	
	public Runnable get() throws InterruptedException {
		Runnable action = null;
		
		empty.acquire();
		mutex.acquire();
		action = buffer.remove(0);
		mutex.release();
		full.release();
		
		
		return action;
	}
	
	/**
	 * @brief Function used to check if the buffer is empty.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return boolean
	*/	
	public boolean empty() {
		
		return (buffer.size() == 0);
	}
}
