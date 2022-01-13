package solution3;

import java.util.ArrayList;
import java.util.List;

public class ItemDeposit {
	
	int[] items;
	int numItems;
	int maxItems;
	

	public ItemDeposit (int maxItems){
		this.maxItems = maxItems;
		this.items = new int[maxItems];
		numItems = 0;
	}
	public boolean deposit (long robot, int id){
		boolean result = false;
		if(numItems < maxItems) {
			System.out.println("Robot " + robot + " carga el item: " + id);
			items[numItems] =id;
			numItems++;
			result = true;
		}
		return result;
		
	}
	public void withdraw (long robot, int id){
		if(numItems > 0) {
			System.out.println("Robot " + robot + " deja el item: " +id);
			for(int i = 0; i < numItems - 1; i++) {
				items[i] = items[i+1];
			}
			numItems--;
		}
		
	}
	
	public int getNumItems() {
		return numItems;
	}
	public int getFirst() {
		return items[0];
	}
	
	public boolean isEmpty() {
		boolean result = false;
		if(numItems != 0) result = true;
		return result;
	}
	
}
