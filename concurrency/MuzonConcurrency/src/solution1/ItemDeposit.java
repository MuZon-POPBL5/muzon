package solution1;


/**
 * @file ItemDeposit.java
 * @brief This class creates a item deposit for the robot. 
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/
public class ItemDeposit {
	
	int[] items;
	int numItems;
	int maxItems;
	
	public ItemDeposit (int maxItems){
		this.maxItems = maxItems;
		this.items = new int[maxItems];
		numItems = 0;
	}
	
	
	/**
	 * @brief Using this the item deposit adds an item.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @param int id
	 * @return boolean
	*/
	public boolean deposit (int id){
		boolean result = false;
		if(numItems < maxItems) {
			System.out.println("Se carga el item: " + id);
			items[numItems] = id;
			numItems++;
			result = true;
		}
		return result;
		
	}
	/**
	 * @brief Using this the item deposit drops an item.
	 * @author Ander Palacios APalacios
	 * @date 18/1/2022
	 * @param int id
	 * @return void
	*/
	public void withdraw (int id){
		if(numItems > 0) {
			System.out.println("Se deja el item: " +id);
			for(int i = 0; i < numItems - 1; i++) {
				items[i] = items[i+1];
			}
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
		return items[0];
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
