package muzonTests;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import solution1.ItemDeposit;


/**
 * @file BufferTest.java
 * @brief This class is used to test the buffer. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/


public class ItemDepositTest {
	
	
	/**
	 * @brief Using this test, the buffer will get full and then empty and will check everything has gone correctly.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	 * @throws InterruptedException when something cant be added or removed from the buffer
	*/	
	
	
	
	@Test
	public void testDepositWorking() {

		ItemDeposit i = new ItemDeposit(5);
		i.deposit(0);
		assertEquals(i.getFirst(), 0); 
	}
	

	@Test
	public void testWithdrawWorking() {

		ItemDeposit i = new ItemDeposit(5);
		i.deposit(0);
		i.withdraw(0);
		assertEquals(i.isEmpty(), false); 
	}
	
}
