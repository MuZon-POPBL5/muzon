package muzonTestsV3;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

import solution3.ItemDeposit;


/**
 * @file BufferTest.java
 * @brief This class is used to test the item deposit. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/


public class ItemDepositTest {
	
	
	/**
	 * @brief Using this test, the deposit will store an item.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/	
	
	
	
	@Test
	public void testDepositWorking() {

		ItemDeposit i = new ItemDeposit(5);
		i.deposit(0,0);
		assertEquals(i.getFirst(), 0); 
	}
	
	/**
	 * @brief Using this test, the deposit will drop an item.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	*/	

	@Test
	public void testWithdrawWorking() {

		ItemDeposit i = new ItemDeposit(5);
		i.deposit(0,0);
		i.withdraw(0,0);
		assertEquals(i.isEmpty(), false); 
	}
	
}
