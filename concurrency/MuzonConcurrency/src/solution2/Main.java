package solution2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @file Main.java
 * @brief This class simulates the robot behavior on a shelf. 
 * @author Ander Palacios APalacios
 * @version v2.0.0
 * @date 18/01/2022
*/
public class Main {
	
	
	final int MAXTHREADS = 10;
	final int ROBOTCAPACITY = 10;
	final int ACTIONSCAPACITY = 10;
	final int NUMSHELVES = 20;
	ItemDeposit items;
	Buffer buffer;
	Taker saverList [];
	Lefter spenderList[];
	List<Robot> executorList;
	Robot executor;
	Semaphore fullBuffer, emptyBuffer;
	public Main(){
		saverList = new Taker[MAXTHREADS];
		spenderList = new Lefter[MAXTHREADS];
		executorList = new ArrayList<>();
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
		for(int d= 0; d < NUMSHELVES; d++){
			buffer = new Buffer(ACTIONSCAPACITY);
			items = new ItemDeposit (ROBOTCAPACITY);
			executor = new Robot(d, buffer);
			
			executorList.add(executor);
			System.out.println("Robot " + d + " creado");
		}
		
	}
	/**
	 * @brief start the threads
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/
	public void startThreads(){
		for(Robot executor : executorList) {
			for (int i= 0; i<MAXTHREADS; i++){
				spenderList[i]= new Lefter(executor.getId(), items,buffer, fullBuffer, emptyBuffer);
				spenderList[i].start();
				saverList[i]= new Taker(executor.getId(), items,buffer, fullBuffer, emptyBuffer);
				saverList[i].start();
			}
			executor.start();
		}
		
	}
	/**
	 * @brief wait until threads have finished
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/
	public void waitEndOfThreads() throws InterruptedException{
		for(Robot executor : executorList) {
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
