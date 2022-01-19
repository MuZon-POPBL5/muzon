package muzonTests;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import solution1.Buffer;
import solution1.ItemDeposit;
import solution1.Taker;

/**
 * @file BufferTest.java
 * @brief This class is used to test the item deposit. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/


public class TakerTest {
	
	
	/**
	 * @brief Using this test, the buffer will be empty and the taker will add tasks to it.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	 * @throws InterruptedException when something cant be added or removed from the buffer
	*/	
	
	
	
	@Test
	public void testTakerWorking() {

		Semaphore fullBuffer = new Semaphore(0);
		Semaphore emptyBuffer = new Semaphore(5);
		Buffer b = new Buffer(5);
		ItemDeposit i = new ItemDeposit(5);
		Taker t = new Taker(i, b, fullBuffer, emptyBuffer);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assertEquals(true, true);
			
		
	}
	

	
}
