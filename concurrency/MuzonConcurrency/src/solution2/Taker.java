package solution2;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * @file Taker.java
 * @brief This class creates a Thread to simulate a take request for the robot. 
 * @author Ander Palacios APalacios
 * @version v2.0.0
 * @date 18/01/2022
*/
public class Taker extends Thread {
	long id;
	final int MAXTIMES = 2;
	Buffer buffer;
	Random generador;
	ItemDeposit account;
	Semaphore fullBuffer, emptyBuffer;
	
	public Taker (long id, ItemDeposit account, Buffer buffer, Semaphore fullBuffer, Semaphore emptyBuffer){
		this.id = id;
		this.buffer = buffer;
		this.account = account;
		this.fullBuffer = fullBuffer;
		this.emptyBuffer = emptyBuffer;
	}
	/**
	 * @brief The thread assigns a take order to the robot
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	*/
	public  void run(){
		generador = new SecureRandom();
			try {
				emptyBuffer.acquire();
				buffer.put(new Runnable () {
					
					@Override
					public void run() {
						account.deposit(id, generador.nextInt(1000));
						fullBuffer.release();
					}
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
		
	}
}
