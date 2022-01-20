package muzonTestsV3;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import solution3.Buffer;
import solution3.ItemDeposit;
import solution3.Lefter;

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
	*/	
	
	
	
	@Test
	public void testLefterWorking() {

			Semaphore mutex = new Semaphore(1);
			Buffer b = new Buffer(5);
			ItemDeposit i = new ItemDeposit(5);
			Lefter l = new Lefter(0, i, b, mutex);
			l.start();
			System.out.println(l.isAlive());
			assertEquals(l.isAlive(), true);
			
			
		
	}
	

	
}
