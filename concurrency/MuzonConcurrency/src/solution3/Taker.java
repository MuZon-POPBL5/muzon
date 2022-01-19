package solution3;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * @file Taker.java
 * @brief This class creates a Thread to simulate a take request for the robot. 
 * @author Ander Palacios APalacios
 * @version v3.0.0
 * @date 18/01/2022
*/
public class Taker extends Thread {
	long id;
	boolean done;
	Buffer buffer;
	ItemDeposit account;
	Random generador;
	Semaphore mutex;
	
	public Taker (long id, ItemDeposit account, Buffer buffer, Semaphore mutex){
		this.id = id;
		this.buffer = buffer;
		this.account = account;
		this.mutex = mutex;
	}
	/**
	 * @brief The thread assigns a take order to the robot
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	*/
	public  void run(){
		generador = new SecureRandom();
			try {
				mutex.acquire();
				buffer.put(new Runnable () {
					
					@Override
					public void run() {
						account.deposit(id, generador.nextInt(1000));
						
						mutex.release();
					}
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
		
	}
}
