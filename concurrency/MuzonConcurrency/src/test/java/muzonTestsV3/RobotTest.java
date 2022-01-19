package muzonTestsV3;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import solution3.Buffer;
import solution3.ItemDeposit;
import solution3.Lefter;
import solution3.Robot;

/**
 * @file BufferTest.java
 * @brief This class is used to test the item deposit. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/


public class RobotTest {
	
	
	/**
	 * @brief Using this test, the buffer will get full and then empty and will check everything has gone correctly.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	 * @throws InterruptedException when the thread is interrupted
	*/	
	
	
	
	@Test
	public void testRobotWorking() {

			Semaphore mutex = new Semaphore(1);
			Buffer b = new Buffer(5);
			ItemDeposit i = new ItemDeposit(5);
			Robot r = new Robot(1, b, i);
			
			try {
				b.put(new Lefter(0, i, b, mutex));
				r.start();
				assertEquals(r.isAlive(), true);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
	}
	

	
}
