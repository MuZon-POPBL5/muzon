package solution1;

import java.util.concurrent.Semaphore;

/**
 * @file Main.java
 * @brief This class simulates the robot behavior on a shelf. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/
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
		fullBuffer = new Semaphore(0);
		emptyBuffer = new Semaphore(ROBOTCAPACITY);
		
	}
	
	/**
	 * @brief create the threads
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/
	public void createThreads(){
		for (int i= 0; i<MAXTHREADS; i++){
			spenderList[i]= new Lefter(account,buffer, fullBuffer, emptyBuffer);
			saverList[i]= new Taker(account,buffer, fullBuffer, emptyBuffer);
		}
		executor = new Robot(buffer);
	}
	
	/**
	 * @brief start the threads
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/
	public void startThreads(){
		executor.start();
		for (int i= 0; i<MAXTHREADS; i++){
			spenderList[i].start();
			saverList[i].start();
		}
	}
	
	/**
	 * @brief wait until threads have finished
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/
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
	/**
	 * @brief executes the simulation
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/
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
