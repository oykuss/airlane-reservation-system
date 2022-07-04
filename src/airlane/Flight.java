package airlane;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Flight implements FlightBuffer{

	private final HashMap<Integer,String> seats;
	
	Flight()
	{
		this.seats = new HashMap<>();
		for(int i=1;i<=25;i++) this.seats.put(i,"0");
		
	}
	
	@Override
	public void queryReservation(String name) throws InterruptedException
	{
		String time = "Time: " + LocalTime.now() +"\n";
		System.out.println(time + name + " looks for avaiblable seats.State of the seats are :");
		HashMap<Integer,String> s = this.seats;
		s.forEach((k,v) -> System.out.printf("Seat No " + k + ": " + v+ " "));
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		
		System.out.println();
	}
	
	@Override
	public void makeReservation(String name, int seatNo, long customerId) throws InterruptedException
	{
		String time = "Time: " + LocalTime.now() + "\n";
		System.out.println(time + name + " tries to book the seat " + seatNo + "\n");
		Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
		if (this.seats.containsKey(seatNo)) {
			if(this.seats.get(seatNo) == "0") {
				System.out.println(time + name + " booked seat number " + seatNo + " successfully\n");
				this.seats.put(seatNo,String.valueOf(customerId));
			} 
			else {
				System.out.println(time + name + " could not book seat number " + seatNo + " since it has been already booked\n");
			}
		}
	}
	
	
	@Override
	public void cancelReservation(String name, int seatNo, long customerId) throws InterruptedException
	{
		String time = "Time: " + LocalTime.now() +"\n";
		System.out.println(time + name + " tries to cancel book the seat " + seatNo + "\n");
		Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
		if (this.seats.containsKey(seatNo)) {
			if(this.seats.get(seatNo) == String.valueOf(customerId)) {
				System.out.println(time + name + " canceled seat number " + seatNo + "\n");
				System.out.println();
				this.seats.put(seatNo, "0");
			}
			else {
				System.out.println(time + name + " could not cancel seat number " + seatNo + "\n");
			}
		} 
		
	}

	@Override
	public void cancelAll() {
		for(int i=1;i<=5;i++) this.seats.put(i,"0");
		String time = "Time: " + LocalTime.now() +"\n";
		System.out.println(time + "All reservations on the flight are cancelled!\n");
	}
	
	@Override
	public int seatCount() {
		return this.seats.size();
	}

}
