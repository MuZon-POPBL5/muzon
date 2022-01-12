package soultion2;

import java.util.ArrayList;
import java.util.List;

public class ItemDeposit {
	
	List<Integer> items;
	int numItems;
	int maxItems;
	

	public ItemDeposit (int maxItems){
		this.maxItems = maxItems;
		this.items = new ArrayList<>();
		numItems = 0;
	}
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
	public void withdraw (long robot, int id){
		if(numItems > 0) {
			System.out.println("Robot " + robot + " deja el item: " +id);
			items.remove(0);
			numItems--;
		}
		
	}
	
	public int getFirst() {
		return items.get(0);
	}
	
	public boolean isEmpty() {
		boolean result = false;
		if(numItems != 0) result = true;
		return result;
	}
	
}
