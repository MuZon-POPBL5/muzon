package solution3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @file Main.java
 * @brief This class simulates the robot behavior on a shelf. 
 * @author Ander Palacios APalacios
 * @version v3.0.0
 * @date 18/01/2022
*/
public class Main {
	
	
	final int ROBOTCAPACITY = 10;
	final int ACTIONSCAPACITY = 10;
	final int NUMSHELVES = 20;
	ItemDeposit items;
	Buffer buffer;
	List<Robot> executorList;
	Robot executor;
	Coordinator coordinator;
	public Main(){
		executorList = new ArrayList<>();
		
		
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
			executor = new Robot(d, buffer, items);
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
		coordinator = new Coordinator(executorList, ROBOTCAPACITY, NUMSHELVES);
		coordinator.coordinate();
		
		
	}
	/**
	 * @brief wait until threads have finished
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/
	public void waitEndOfThreads() throws InterruptedException{
		coordinator.waitToFinish();
		
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
