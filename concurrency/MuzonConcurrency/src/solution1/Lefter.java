package solution1;

import java.util.concurrent.Semaphore;

public class Lefter extends Thread {
	final int MAXTIMES = 2;
	Buffer buffer;
	ItemDeposit account;
	Semaphore fullBuffer, emptyBuffer;
	
	
	public Lefter (ItemDeposit account,Buffer buffer, Semaphore fullBuffer, Semaphore emptyBuffer){
		this.buffer = buffer;
		this.account = account;
		this.fullBuffer = fullBuffer;
		this.emptyBuffer = emptyBuffer;
		
	}

	public void run(){
		for (int i= 0; i < MAXTIMES; i++){
			try {
				if(account.isEmpty()) {
					buffer.put(()->account.withdraw(account.getFirst()));
				}
				else {
					fullBuffer.acquire();
					i--;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
