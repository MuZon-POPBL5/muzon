package muzonTestsV3;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.Test;


import solution3.Buffer;
import solution3.ItemDeposit;
import solution3.Taker;

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
	 * @throws InterruptedException when the thread doesn't end
	*/	
	
	
	
	@Test
	public void testTakerWorking() {

		Semaphore mutex = new Semaphore(1);
		Buffer b = new Buffer(5);
		ItemDeposit i = new ItemDeposit(5);
		Taker t = new Taker(0, i, b, mutex);
		t.start();
		assertEquals(t.isAlive() , true);
		try {
			t.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		
	}
	

	
}
