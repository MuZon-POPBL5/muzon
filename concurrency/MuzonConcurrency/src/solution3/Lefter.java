package solution3;

import java.util.concurrent.Semaphore;

/**
 * @file Lefter.java
 * @brief This class creates a Thread to simulate a left request for the robot. 
 * @author Ander Palacios APalacios
 * @version v3.0.0
 * @date 18/01/2022
*/
public class Lefter extends Thread {
	long id;
	boolean done;
	Buffer buffer;
	ItemDeposit account;
	Semaphore mutex;
	
	
	public Lefter (long id, ItemDeposit account,Buffer buffer, Semaphore mutex){
		this.id = id;
		this.buffer = buffer;
		this.account = account;
		this.mutex = mutex;
		
	}

	/**
	 * @brief The thread assigns a left order to the robot
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	*/
	public void run(){
		done = false;
			try {
				while(!done) {
					mutex.acquire();
					if(account.isEmpty()) {
						buffer.put(()->account.withdraw(this.id, account.getFirst()));
						done = true;
					}
					mutex.release();
				}
			
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		
	}
}
