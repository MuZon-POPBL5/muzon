package solution3;

import java.util.Random;
import java.util.concurrent.Semaphore;


public class Taker extends Thread {
	long id;
	boolean done;
	Buffer buffer;
	ItemDeposit account;
	Semaphore mutex;
	
	public Taker (long id, ItemDeposit account, Buffer buffer, Semaphore mutex){
		this.id = id;
		this.buffer = buffer;
		this.account = account;
		this.mutex = mutex;
	}

	public  void run(){
		Random generador = new Random();
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
				//e.printStackTrace();
			}
			
		
	}
}
