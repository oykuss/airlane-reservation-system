package airlane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;



public class Main {

	public static void main(String[] args) {
		ExecutorService pool = Executors.newCachedThreadPool();
		Flight flight = new Flight();
		
		int reader = 1;
		int writer = 1;
		for(int i = 0; i < 5; i++) {
				if(ThreadLocalRandom.current().nextInt(2) == 0) {
					pool.execute(new ReaderThread(flight, "Reader-" + reader++));
				}
				else {
					pool.execute(new WriterThread(flight, "Writer-" + writer++));
				}
		}
		
		pool.shutdown();
		
	}

}
