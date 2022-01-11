package soultion2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


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
	public void createThreads(){
		for(int d= 0; d < NUMSHELVES; d++){
			buffer = new Buffer(ACTIONSCAPACITY);
			items = new ItemDeposit (ROBOTCAPACITY);
			executor = new Robot(d, buffer);
			
			executorList.add(executor);
			System.out.println("Robot " + d + " creado");
		}
		
	}
	public void startThreads(){
		for(Robot executor : executorList) {
			for (int i= 0; i<MAXTHREADS; i++){
				spenderList[i]= new Lefter(executor.getId(), items,buffer, fullBuffer, emptyBuffer);
				spenderList[i].start();
				saverList[i]= new Taker(executor.getId(), items,buffer, fullBuffer, emptyBuffer);
				saverList[i].start();
			}
			System.out.println("pedidos asignados a robot " + executor.getId());
			executor.start();
		}
		
	}
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
