package soultion2;

import java.util.concurrent.Semaphore;

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

	public void run(){

			try {
				fullBuffer.acquire();
				if(account.isEmpty()) {
					buffer.put(()->account.withdraw(this.id, account.getFirst()));
					emptyBuffer.release();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}
}
