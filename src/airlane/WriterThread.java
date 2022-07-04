package airlane;

import java.util.concurrent.ThreadLocalRandom;

public class WriterThread implements Runnable {
    private final FlightBuffer flight;
    private final String name;
    
    public WriterThread(FlightBuffer flight, String name) {
        this.flight = flight;
        this.name = name;
    }
    
    @Override
    public void run() {
        final ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        final String threadName = name + "(" + Thread.currentThread().getName() + ")";
        final long threadId = Thread.currentThread().getId();
        boolean running = true;
        while(running) {
            try {
                if (localRandom.nextInt(0, 9) < 7) {
                    // make reservation
                    Thread.sleep(localRandom.nextInt(3000));
                    flight.makeReservation(
                        threadName, 
                        localRandom.nextInt(1, 25),
                        threadId);
                } 
                else {
                    // cancel reservation
                    Thread.sleep(localRandom.nextInt(3000));
                    flight.cancelReservation(
                        threadName,
                        localRandom.nextInt(1, 25),
                        threadId);
                }
            }
            catch (InterruptedException e) {
                running = false;
            }
            
        }
    }
}
