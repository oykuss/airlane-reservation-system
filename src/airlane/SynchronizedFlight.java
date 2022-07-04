package airlane;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JTextArea;

class SynchronizedFlight implements FlightBuffer{
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
	
	private final HashMap<Integer,Long> seats;
	private final JTextArea log;
	private final Window window;
	private int bookedCount = 0;
	
	public SynchronizedFlight(JTextArea log, Window window)
	{
		this.log = log;
		this.window = window;
		this.seats = new HashMap<>();
		for (int i=1;i<=24;i++) {
			this.seats.put(i,0L);
		}
		
	}

	@Override
	public void queryReservation(String name) throws InterruptedException
	{
		lock.readLock().lock();
		try {

			String time = "Time: " + LocalTime.now() +"\n";
			String oldLog = this.log.getText();
			StringBuffer newLog = new StringBuffer();
			newLog.append('\n');
			newLog.append(time + name);
			newLog.append(" looks for avaiblable seats. State of the seats are :\n");

			this.seats.forEach((k,v) -> {
				newLog.append("Seat No " + k + ": " + v + " ");
			});
			newLog.append("\n-------------------------------------------------------------------------------\n");
			newLog.append(oldLog);
			this.log.setText(newLog.toString());
        } finally {
            lock.readLock().unlock();
        }
        
		
	}

	@Override
	public void cancelReservation(String name, int seatNo, long customerId) throws InterruptedException {
		
		lock.writeLock().lock();		
		try {
			String time = "Time: " + LocalTime.now() +"\n";
			String oldLog = this.log.getText();
			StringBuffer newLog = new StringBuffer();
			newLog.append('\n');
			newLog.append(time + name);
			newLog.append(" tries to cancel book the seat " + seatNo + "\n");

			if (this.seats.containsKey(seatNo)) {
				if(this.seats.get(seatNo) == customerId) {
					newLog.append(time + name + " canceled seat number " + seatNo + "\n");
					newLog.append('\n');
					this.seats.put(seatNo, 0L);
					this.window.updateSeatPlan(seatNo, 1);
					bookedCount--;
				}
				else {
					newLog.append(time + name + " could not cancel seat number " + seatNo);
					newLog.append('\n');
				}
			}
			newLog.append(oldLog);
			this.log.setText(newLog.toString());            
        } 
		finally {
            lock.writeLock().unlock();
        }		
	}

	@Override
	public void makeReservation(String name, int seatNo, long customerId) throws InterruptedException {
		
		lock.writeLock().lock();
	
		try {
			String time = "Time: " + LocalTime.now() +"\n";
			String oldLog = this.log.getText();
			StringBuffer newLog = new StringBuffer();
			newLog.append('\n');
			newLog.append(time + name);
			if (bookedCount == this.seatCount()) {
				newLog.append(" no more seats available on the flight currently!");
			} else {
				newLog.append(" tries to book the seat " + seatNo + "\n");
				if (this.seats.containsKey(seatNo)) {
					if(this.seats.get(seatNo) == 0L) {
						newLog.append(time + name + " booked seat number " + seatNo + " successfully\n");
						this.seats.put(seatNo, customerId);
						this.window.updateSeatPlan(seatNo, 0);
						bookedCount++;
					} 
					else {
						newLog.append(time + name + " could not book seat number " + seatNo + " since it has been already booked\n");
					}
				}
			}
			newLog.append(oldLog);
			this.log.setText(newLog.toString());      
        } 
		finally {
            lock.writeLock().unlock();
        }		
	}

	@Override
	public int seatCount() {
		return this.seats.size();
	}

	@Override
	public void cancelAll() {
		for(int i=1;i<=5;i++) this.seats.put(i, 0L);
		String time = "Time: " + LocalTime.now() +"\n";
		String oldLog = this.log.getText();
		StringBuffer newLog = new StringBuffer();
		newLog.append('\n');
		newLog.append(time + "All reservations on the flight are cancelled!\n");
		newLog.append(oldLog);
		this.log.setText(newLog.toString());
		this.log.setCaretPosition(0);
	}

}
