package solution1;

public class Robot extends Thread {
	
	Buffer buffer;
	
	
	public Robot (Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		Runnable action;
		try {
			while (!this.isInterrupted()) {
				action = buffer.get();
				action.run();
			}
		}catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
	
}
