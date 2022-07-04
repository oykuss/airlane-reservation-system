package airlane;

import java.util.concurrent.ThreadLocalRandom;

public class ReaderThread implements Runnable{
    private final FlightBuffer flight;
    private final String name;
    
    public ReaderThread(FlightBuffer flight, String name) {
        this.flight = flight;
        this.name = name;
    }
    @Override
    public void run() {
        final String threadName = name + "(" + Thread.currentThread().getName() + ")";
        boolean running = true;
        while (running) {
            try {
                for (int i=1; i<this.flight.seatCount(); i++) {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(3000));
                    flight.queryReservation(threadName);
                }
            }
            catch (InterruptedException e) {
                running = false;
            }
        } 
    }
}
