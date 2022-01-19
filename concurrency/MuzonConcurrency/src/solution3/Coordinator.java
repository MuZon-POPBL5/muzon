package solution3;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
/**
 * @file Coordinator.java
 * @brief This class creates a coordinator robot that assigns orders to other robots. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/
public class Coordinator {
	
	final int MAXTHREADS = 400;
	//int actionsCapacity;
	int robotCapacity;
	int numShelves;
	List<Robot> robots;
	List<Taker> saverList;
	List<Lefter> lefterList;
	Semaphore mutexBuffer;
	Semaphore mutex;
	Semaphore mutexBufferList[];
	Random generador;

	public Coordinator(List<Robot> robots, int robotCapacity, int numShelves) {
		this.robotCapacity = robotCapacity;
		this.numShelves = numShelves;
		this.robots = robots;
		saverList = new ArrayList<>();
		lefterList = new ArrayList<>();
		mutexBufferList = new Semaphore[numShelves];
		mutex = new Semaphore(1);
		for (Robot r : robots) {
			mutexBuffer = new Semaphore(1);
			mutexBufferList[(int) r.getId()] = mutexBuffer;
		}
		
		
	}


	/**
	 * @brief Using this the coordinator generates tasks and assign them randomly to a robot.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/	
	public void coordinate() {
		generador = new SecureRandom();
		int robotId;
		boolean first;
		Robot robot = null;
		for (Robot r: robots) {
			r.start();
		}
		for (int i = 0; i < MAXTHREADS; i++) {
			first = false;
			robotId = generador.nextInt(robots.size());
			for(Robot r: robots) {
				if(r.getId()  == robotId) robot = r;
			}
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			if(robot.getNumActions() == 0) {
				first = true;
				robot.addAction();
				Taker taker= new Taker(robotId, robot.getItems(), robot.getBuffer(), mutexBufferList[(int) robot.getId()]);
				saverList.add(taker);
				taker.start();
				System.out.println("Pedido " + i + " obtener producto asignado a robot " + robot.getId());
			}
			else if(robot.getNumActions() == robot.getBuffer().getCapacity()-1){
				first = true;
				robot.addAction();
				Lefter lefter = new Lefter(robotId, robot.getItems(), robot.getBuffer(), mutexBufferList[(int) robot.getId()]);
				lefterList.add(lefter);
				lefter.start();
				System.out.println("Pedido " + i + " depositar producto asignado a robot " + robot.getId());	
			}
				
			if(generador.nextBoolean() && !first) {
				robot.addAction();
				Lefter lefter = new Lefter(robotId, robot.getItems(), robot.getBuffer(), mutexBufferList[(int) robot.getId()]);
				lefterList.add(lefter);
				lefter.start();
				System.out.println("Pedido " + i + " depositar producto asignado a robot " + robot.getId());			
			}
			else if(!first){
				robot.addAction();
				Taker taker= new Taker(robotId, robot.getItems(), robot.getBuffer(), mutexBufferList[(int) robot.getId()]);
				saverList.add(taker);
				taker.start();
				System.out.println("Pedido " + i + " obtener producto asignado a robot " + robot.getId());
			}
			mutex.release();
		}
	}
	
	/**
	 * @brief the coordinator waits until every robot has finished their tasks
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/	
	public void waitToFinish() throws InterruptedException {
		
		
		
		for(Taker s: saverList) {
			s.join();
		}
		
		for(Lefter l: lefterList) {
			l.join();
		}
		for(Robot r : robots) {
			r.interrupt();
			r.join();
			
		}
	}
}
