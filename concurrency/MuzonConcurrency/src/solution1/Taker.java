package solution1;

import java.util.Random;
import java.util.concurrent.Semaphore;


public class Taker extends Thread {
	final int MAXTIMES = 2;
	Buffer buffer;
	ItemDeposit account;
	Semaphore fullBuffer, emptyBuffer;
	
	public Taker (ItemDeposit account, Buffer buffer, Semaphore fullBuffer, Semaphore emptyBuffer){
		this.buffer = buffer;
		this.account = account;
		this.fullBuffer = fullBuffer;
		this.emptyBuffer = emptyBuffer;
	}

	public  void run(){
		Random generador = new Random();
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
				e.printStackTrace();
			}
			
		}
	}
}
