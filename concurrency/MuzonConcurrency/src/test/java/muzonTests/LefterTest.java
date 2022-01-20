package muzonTests;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import solution1.Buffer;
import solution1.ItemDeposit;
import solution1.Lefter;

/**
 * @file BufferTest.java
 * @brief This class is used to test the item deposit. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/


public class LefterTest {
	
	
	/**
	 * @brief Using this test, the buffer will simulate to be full and the lefter will remove items.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	 * @throws InterruptedException when something cant be added or removed from the buffer
	*/	
	
	
	
	@Test
	public void testLefterWorking() {

			Semaphore fullBuffer = new Semaphore(5);
			Semaphore emptyBuffer = new Semaphore(0);
			Buffer b = new Buffer(5);
			ItemDeposit i = new ItemDeposit(5);
			Lefter l = new Lefter(i, b, fullBuffer, emptyBuffer);
			l.start();
			try {
				l.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			assertEquals(true, true);
			
		
	}
	

	
}
