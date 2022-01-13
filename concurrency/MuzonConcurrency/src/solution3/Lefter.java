package solution3;

import java.util.concurrent.Semaphore;

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
				e.printStackTrace();
			}
		
	}
}
