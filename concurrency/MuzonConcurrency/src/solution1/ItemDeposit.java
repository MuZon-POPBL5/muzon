package solution1;

import java.util.concurrent.Semaphore;

public class ItemDeposit {
	
	int[] items;
	int numItems;
	int maxItems;
	

	public ItemDeposit (int maxItems){
		this.maxItems = maxItems;
		this.items = new int[maxItems];
		numItems = 0;
	}
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
	public void withdraw (int id){
		if(numItems > 0) {
			System.out.println("Se deja el item: " +id);
			for(int i = 0; i < numItems - 1; i++) {
				items[i] = items[i+1];
			}
			numItems--;
		}
		
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
