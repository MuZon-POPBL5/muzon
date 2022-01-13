package solution3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Main {
	
	
	final int ROBOTCAPACITY = 10;
	final int ACTIONSCAPACITY = 10;
	final int NUMSHELVES = 10;
	ItemDeposit items;
	Buffer buffer;
	List<Robot> executorList;
	Robot executor;
	Coordinator coordinator;
	public Main(){
		executorList = new ArrayList<>();
		
		
	}
	public void createThreads(){
		for(int d= 0; d < NUMSHELVES; d++){
			buffer = new Buffer(ACTIONSCAPACITY);
			items = new ItemDeposit (ROBOTCAPACITY);
			executor = new Robot(d, buffer, items);
			executorList.add(executor);
			System.out.println("Robot " + d + " creado");
		}
		
	}
	public void startThreads(){
		coordinator = new Coordinator(executorList, ROBOTCAPACITY, NUMSHELVES);
		coordinator.coordinate2();
		
		
	}
	public void waitEndOfThreads() throws InterruptedException{
		coordinator.waitToFinish();
		
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
