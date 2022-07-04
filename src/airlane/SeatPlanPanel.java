package airlane;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.*;

public class SeatPlanPanel extends JPanel {
    private final int seatCount;
    private final List<JLabel> seatButtons = new ArrayList<JLabel>();
    Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);

    public SeatPlanPanel(int seatCount) {
        this.seatCount = seatCount;

        this.setLayout(new GridLayout(2, 12, 10, 10));
        for (int i = 0; i < this.seatCount; i++) {
            String seatNo = Integer.toString(i + 1);
            JLabel seat = new JLabel(seatNo);
            //seat.setEnabled(false);
            seat.setBackground(Color.GREEN);
            seat.setBorder(border);
            seatButtons.add(seat);
            this.add(seat);
        }
    }

    public void bookSeat(int seatNo) {
        seatButtons.get(seatNo - 1).setBorder(redBorder);
    }

    public void cancelSeat(int seatNo) {
        seatButtons.get(seatNo - 1).setBorder(border);
    }
}
