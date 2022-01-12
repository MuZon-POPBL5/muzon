package soultion2;

import java.util.Random;
import java.util.concurrent.Semaphore;


public class Taker extends Thread {
	long id;
	final int MAXTIMES = 2;
	Buffer buffer;
	ItemDeposit account;
	Semaphore fullBuffer, emptyBuffer;
	
	public Taker (long id, ItemDeposit account, Buffer buffer, Semaphore fullBuffer, Semaphore emptyBuffer){
		this.id = id;
		this.buffer = buffer;
		this.account = account;
		this.fullBuffer = fullBuffer;
		this.emptyBuffer = emptyBuffer;
	}

	public  void run(){
		Random generador = new Random();
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
				e.printStackTrace();
			}
			
		
	}
}
