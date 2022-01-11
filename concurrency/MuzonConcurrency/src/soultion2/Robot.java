package soultion2;

public class Robot extends Thread {
	
	int id;
	Buffer buffer;
	
	public long getId() {
		return id;
	}

	public Robot (int id, Buffer buffer) {
		this.id = id;
		this.buffer = buffer;
	}

	@Override
	public void run() {
		Runnable action;
		try {
			while (!this.isInterrupted()) {
				action = buffer.get();
				sleep(10);
				action.run();
			}
		}catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
	
}
