package solution1;

import java.util.concurrent.Semaphore;


public class Main {
	
	
	final int MAXTHREADS = 100;
	final int ROBOTCAPACITY = 5;
	final int ACTIONSCAPACITY = 10;
	ItemDeposit account;
	Buffer buffer;
	Taker saverList [];
	Lefter spenderList[];
	Robot executor;
	Semaphore fullBuffer, emptyBuffer;
	public Main(){
		
		account = new ItemDeposit (ROBOTCAPACITY);
		saverList = new Taker[MAXTHREADS];
		spenderList = new Lefter[MAXTHREADS];
		buffer = new Buffer(ACTIONSCAPACITY);
		fullBuffer = new Semaphore(ROBOTCAPACITY);
		emptyBuffer = new Semaphore(0);
		
	}
	public void createThreads(){
		for (int i= 0; i<MAXTHREADS; i++){
			spenderList[i]= new Lefter(account,buffer, fullBuffer, emptyBuffer);
			saverList[i]= new Taker(account,buffer, fullBuffer, emptyBuffer);
		}
		executor = new Robot(buffer);
	}
	public void startThreads(){
		executor.start();
		for (int i= 0; i<MAXTHREADS; i++){
			spenderList[i].start();
			saverList[i].start();
		}
	}
	public void waitEndOfThreads() throws InterruptedException{
		for (int i= 0; i<MAXTHREADS; i++){
			spenderList[i].join();
			saverList[i].join();
		}
		while (!buffer.empty()) {
			Thread.yield();
		};
		
		
		executor.interrupt();
		executor.join();
	}
	public static void main(String[] args) throws InterruptedException {
		long start = System.currentTimeMillis();
		Main exercise = new Main();
		exercise.createThreads();
		exercise.startThreads();
		exercise.waitEndOfThreads();
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;
		System.out.println("El robot ha completado los pedidos en: " + timeElapsed + "ms");
	}

}
