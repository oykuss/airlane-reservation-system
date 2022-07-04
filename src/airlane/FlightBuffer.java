package airlane;

public interface FlightBuffer {

    public void queryReservation(String name) throws InterruptedException;
    public void cancelReservation(String name, int seatNo, long customerId) throws InterruptedException;
    public void cancelAll();
    public void makeReservation(String name, int seatNo, long customerId) throws InterruptedException;
    public int seatCount();
}
