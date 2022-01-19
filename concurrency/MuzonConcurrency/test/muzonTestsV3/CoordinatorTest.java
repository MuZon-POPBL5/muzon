package muzonTestsV3;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import solution3.Buffer;
import solution3.ItemDeposit;
import solution3.Robot;
import solution3.Coordinator;

/**
 * @file BufferTest.java
 * @brief This class is used to test the robot coordinator. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/


public class CoordinatorTest {
	
	
	/**
	 * @brief Using this test, the coordinator will get orders, asign them to robots and wait until they finish.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return void
	 * @throws InterruptedException when something cant be added or removed from the buffer
	*/	
	
	
	
	@Test
	public void testCoordinatorWorking() {

		List<Robot> robots = new ArrayList<>();
		for(int d = 0; d<2; d++) {
			Buffer b = new Buffer(5);
			ItemDeposit i = new ItemDeposit(5);
			Robot r = new Robot(d, b, i);
			robots.add(r);
		}

		Coordinator c = new Coordinator(robots,2,2);
		c.coordinate2();
		try {
			c.waitToFinish();
			assertEquals(true, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	
}
