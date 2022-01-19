package solution2;
/**
 * @file Robot.java
 * @brief This class creates a robot that executes the orders. 
 * @author Ander Palacios APalacios
 * @version v2.0.0
 * @date 18/01/2022
*/
public class Robot extends Thread {
	
	int id;
	Buffer buffer;
	
	public long getId() {
		return id;
	}

	public Robot (int id, Buffer buffer) {
		this.id = id;
		this.buffer = buffer;
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
				//sleep(10);
				action.run();
			}
		}catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
}
