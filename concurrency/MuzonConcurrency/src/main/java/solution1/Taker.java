package solution1;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * @file Taker.java
 * @brief This class creates a Thread to simulate a take request for the robot. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/
public class Taker extends Thread {
	final int MAXTIMES = 2;
	Buffer buffer;
	ItemDeposit account;
	Random generador = new SecureRandom();
	Semaphore fullBuffer, emptyBuffer;
	
	public Taker (ItemDeposit account, Buffer buffer, Semaphore fullBuffer, Semaphore emptyBuffer){
		this.buffer = buffer;
		this.account = account;
		this.fullBuffer = fullBuffer;
		this.emptyBuffer = emptyBuffer;
	}
	/**
	 * @brief The thread assigns two take orders to the robot
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	*/
	public  void run(){
		int item = 0;
		for (int i= 0; i < MAXTIMES; i++){
			try {
				emptyBuffer.acquire();
				buffer.put(new Runnable () {
					
					@Override
					public void run() {
						account.deposit(generador.nextInt(1000));
						fullBuffer.release();
					}
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
		}
	}
}
