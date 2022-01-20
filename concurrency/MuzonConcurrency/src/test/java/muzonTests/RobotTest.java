package muzonTests;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import solution1.Buffer;
import solution1.ItemDeposit;
import solution1.Lefter;
import solution1.Robot;

/**
 * @file BufferTest.java
 * @brief This class is used to test the item deposit. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/


public class RobotTest {
	
	
	/**
	 * @brief Using this test, the robot will get an order and the test will check it starts correctly.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	 * @throws InterruptedException when something cant be added or removed from the buffer
	*/	
	
	
	
	@Test
	public void testRobotWorking() {

			Semaphore fullBuffer = new Semaphore(0);
			Semaphore emptyBuffer = new Semaphore(5);
			Buffer b = new Buffer(5);
			ItemDeposit i = new ItemDeposit(5);
			Robot r = new Robot(b);
			
			try {
				b.put(new Lefter(i, b, fullBuffer, emptyBuffer));
				r.start();
				assertEquals(r.isAlive(), true);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
	}
	

	
}
