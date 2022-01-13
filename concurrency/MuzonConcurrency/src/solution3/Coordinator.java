package solution3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Coordinator {
	
	final int MAXTHREADS = 100;
	//int actionsCapacity;
	int robotCapacity;
	int numShelves;
	List<Robot> robots;
	List<Taker> saverList;
	List<Lefter> lefterList;
	Semaphore mutexBuffer;
	Semaphore mutex;
	Semaphore mutexBufferList[];

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

	public void coordinate() {
		for(Robot executor : robots) {
//			for (int i= 0; i<MAXTHREADS; i++){
//				spenderList[i]= new Lefter(executor.getId(), executor.getItems(), executor.getBuffer(), fullBuffer, emptyBuffer);
//				spenderList[i].start();
//				saverList[i]= new Taker(executor.getId(), executor.getItems(), executor.getBuffer(), fullBuffer, emptyBuffer);
//				saverList[i].start();
//			}
			System.out.println("pedidos asignados a robot " + executor.getId());
			executor.start();
		}
	}
	
	public void coordinate2() {
		Random generador = new Random();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
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
