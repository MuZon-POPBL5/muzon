package solution2;

import java.util.concurrent.Semaphore;

/**
 * @file Lefter.java
 * @brief This class creates a Thread to simulate a left request for the robot. 
 * @author Ander Palacios APalacios
 * @version v2.0.0
 * @date 18/01/2022
*/
public class Lefter extends Thread {
	long id;
	final int MAXTIMES = 2;
	Buffer buffer;
	ItemDeposit account;
	Semaphore fullBuffer, emptyBuffer;
	
	
	public Lefter (long id, ItemDeposit account,Buffer buffer, Semaphore fullBuffer, Semaphore emptyBuffer){
		this.id = id;
		this.buffer = buffer;
		this.account = account;
		this.fullBuffer = fullBuffer;
		this.emptyBuffer = emptyBuffer;
		
	}

	/**
	 * @brief The thread assigns a left order to the robot
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	*/
	public void run(){

			try {
				fullBuffer.acquire();
				if(account.isEmpty()) {
					buffer.put(()->account.withdraw(this.id, account.getFirst()));
					emptyBuffer.release();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		
	}
}
