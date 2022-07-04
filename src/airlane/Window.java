package airlane;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class Window extends JFrame implements ActionListener
{		
	private JButton btnAddWriter;
	private JButton btnAddReader;
	private JButton btnStop;
	private JTextArea log;
	private JScrollPane logScroll;
	private SeatPlanPanel seatPlan;

	private ExecutorService pool; 
	private SynchronizedFlight flight;

	private int reader = 1;
	private int writer = 1;
	
	public Window()
	{		
		btnAddWriter = new JButton();
		btnAddReader = new JButton();
		btnStop = new JButton();
		log = new JTextArea();
		logScroll = new JScrollPane(this.log);

		pool = Executors.newCachedThreadPool();
		flight = new SynchronizedFlight(this.log, this);
		
		JButtonInit();
		JTextAreaInit();
		seatPlanInit();
		JFrameInit();
	}
	
	private void JButtonInit()
	{

		/************** btnAddWriter **************/
		btnAddWriter.setBounds(1048, 50, 150, 40);
		btnAddWriter.setText("Add Writer");
		btnAddWriter.addActionListener(this);
		add(btnAddWriter);

		/************** btnAddReader **************/
		btnAddReader.setBounds(1048, 100, 150, 40);
		btnAddReader.setText("Add Reader");
		btnAddReader.addActionListener(this);
		add(btnAddReader);

		/************** btnRestart **************/
		btnStop.setBounds(1048, 150, 150, 40);
		btnStop.setText("Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(this);
		add(btnStop);
	}

	private void seatPlanInit() {
		seatPlan = new SeatPlanPanel(this.flight.seatCount());
		seatPlan.setBounds(10, 10, 990, 250);
		add(seatPlan);
	}
	
	private void JTextAreaInit()
	{
		/************ log ************/
		logScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logScroll.setBounds(10, 260, 990, 250);
		log.setFont(new Font("Consolas", Font.PLAIN, 14));
		log.setBackground(Color.BLACK);
		log.setForeground(Color.GREEN);
		log.setEditable(false);
		add(logScroll);
	}
	
	private void JFrameInit()
	{
		setTitle("Airlane");
		setSize(1280, 565);
		setResizable(false);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/*** Events ***/
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == btnAddWriter) {
			addWriter();
		}
		else if (e.getSource() == btnAddReader) {
			addReader();
		}		
		else if (e.getSource() == btnStop) {
			stop();
		}
	}
	
	private void addWriter() {
		if (pool.isShutdown()) {
			pool = Executors.newCachedThreadPool(); 
		}
		pool.execute(new WriterThread(flight, "Writer-" + writer++));
		if (!btnStop.isEnabled()) {
			btnStop.setEnabled(true);
		}
	}

	private void addReader() {
		if (pool.isShutdown()) {
			pool = Executors.newCachedThreadPool(); 
		}
		pool.execute(new ReaderThread(flight, "Reader-" + reader++));
		if (!btnStop.isEnabled()) {
			btnStop.setEnabled(true);
		}
	}

	private void stop() {
		pool.shutdownNow();
		flight.cancelAll();
		btnStop.setEnabled(false);
	}

	public void updateSeatPlan(int seatNo, int book) {
		if (book == 0) {
			seatPlan.bookSeat(seatNo);
		} else {
			seatPlan.cancelSeat(seatNo);
		}
	}
	
	public static void main(String[] args)
	{
		new Window();
	}
}