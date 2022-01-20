package muzonTestsV2;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import solution2.Buffer;
import solution2.ItemDeposit;
import solution2.Lefter;

/**
 * @file BufferTest.java
 * @brief This class is used to test the item deposit. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/


public class BufferTest {
	
	
	/**
	 * @brief Using this test, the buffer will get full and then empty and will check everything has gone correctly.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	 * @throws InterruptedException when something cant be added or removed from the buffer
	*/	
	
	
	
	@Test
	public void testBufferWorking() {

			Semaphore fullBuffer = new Semaphore(0);
			Semaphore emptyBuffer = new Semaphore(5);
			Buffer b = new Buffer(5);
			ItemDeposit i = new ItemDeposit(5);
			for(int d = 0; d < 5; d++) {
				try {
					b.put(new Lefter(0, i, b, fullBuffer, emptyBuffer));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int d = 0; d < 5; d++) {
				try {
					b.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			assertEquals(b.empty(), true);
			
		
	}
	

	
}
