package solution2;

import java.util.ArrayList;
import java.util.List;

/**
 * @file ItemDeposit.java
 * @brief This class creates a item deposit for the robot. 
 * @author Ander Palacios APalacios
 * @version v2.0.0
 * @date 18/01/2022
*/
public class ItemDeposit {
	
	List<Integer> items;
	int numItems;
	int maxItems;
	

	public ItemDeposit (int maxItems){
		this.maxItems = maxItems;
		this.items = new ArrayList<>();
		numItems = 0;
	}
	
	/**
	 * @brief Using this the item deposit adds an item to the robot.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @param long robot, int id
	 * @return boolean
	*/
	public boolean deposit (long robot, int id){
		boolean result = false;
		if(numItems < maxItems) {
			System.out.println("Robot " + robot + " carga el item: " + id);
			items.add(id);
			numItems++;
			result = true;
		}
		return result;
		
	}
	
	/**
	 * @brief Using this the robots item deposit drops an item.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @param long robot, int id
	 * @return void
	*/
	public void withdraw (long robot, int id){
		if(numItems > 0) {
			System.out.println("Robot " + robot + " deja el item: " +id);
			items.remove(0);
			numItems--;
		}
		
	}
	
	/**
	 * @brief This function gets the first item of the deposit stack
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return int
	*/
	public int getFirst() {
		return items.get(0);
	}
	
	/**
	 * @brief This function checks if the deposit is empty
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @return boolean
	*/
	public boolean isEmpty() {
		boolean result = false;
		if(numItems != 0) result = true;
		return result;
	}
	
}