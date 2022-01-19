package solution3;

import java.util.concurrent.Semaphore;
/**
 * @file Robot.java
 * @brief This class creates a robot that executes the orders. 
 * @author Ander Palacios APalacios
 * @version v3.0.0
 * @date 18/01/2022
*/
public class Robot extends Thread {
	
	int id;
	int numActions;
	Buffer buffer;
	ItemDeposit items;
	Semaphore mutex;
	
	/**
	 * @brief get the id of the robot
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return long
	*/
	public long getId() {
		return id;
	}

	/**
	 * @brief get the robot if the id is valid
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return Robot
	*/
	public Robot getRobot(int id) {
		if(id == this.id) {
			return this;
		}
		else return null;
	}
	
	public Robot (int id, Buffer buffer, ItemDeposit items) {
		this.id = id;
		this.buffer = buffer;
		this.items = items;
		this.numActions = 0;
		mutex = new Semaphore(1);
	}

	/**
	 * @brief increment the actions counter
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/
	public void addAction() {
		this.numActions++;
	}
	
	/**
	 * @brief get the actions counter
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return int
	*/
	public int getNumActions() {
		return numActions;
	}

	/**
	 * @brief get the robots buffer
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return Buffer
	*/
	public Buffer getBuffer() {
		return buffer;
	}
	
	/**
	 * @brief get the robots item deposit
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return ItemDeposit
	*/
	public ItemDeposit getItems() {
		return items;
	}

	/**
	 * @brief The thread picks orders from the buffer and executes them
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	*/
	@Override
	public void run() {
		Runnable action;
		try {
			while (!this.isInterrupted()) {
				
				action = buffer.get();
				
				sleep(1);
				action.run();
			}
		}catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
}
