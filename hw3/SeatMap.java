/**
 * CIS 461: Formal Methods for Software Engineering
 * Concert Hall Seat Reservation
 * The concert hall project class: SeatMap.java
 *
 * @author Haiping Xu
 * Created on Oct. 9, 2014
 **/


import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class SeatMap extends JComponent {
    private static final int MAP_WIDTH = 496;
    private static final int MAP_HEIGHT = 346;
    private static final int MAP_MARGIN = 3;
    private static final int MAP_BOTTOM = MAP_HEIGHT + 2*MAP_MARGIN;
    private static final int MAP_RIGHT = MAP_WIDTH + 2*MAP_MARGIN;
    private static final Font f1 = new Font("Helvetica", Font.BOLD,12);

    private Dimension preferredSize;
    private int map[][];
    private Image seatMapImage, seatAImage, seatBImage, seatXImage;

    public SeatMap(int map[][]){
        preferredSize = new Dimension(MAP_RIGHT + 1, MAP_BOTTOM + 1);
        this.setSize(preferredSize.width, preferredSize.height);
        this.map= map;
        setBorder(BorderFactory.createMatteBorder(MAP_MARGIN, MAP_MARGIN,
                                                  MAP_MARGIN, MAP_MARGIN,
                                                  Color.LIGHT_GRAY));
        setOpaque(true);
        seatMapImage =
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("images-concerthall/concerthall_seats.jpg"));
        seatAImage =
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("images-concerthall/seat-A.jpg"));
        seatBImage =
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("images-concerthall/seat-B.jpg"));
        seatXImage =
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("images-concerthall/seat-X.jpg"));
        this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.drawImage(seatMapImage, 0, 0, this);

        int rowInit = 60, columnInit = 108;
        for (int i = 1; i <= 10; i++) {
            for (int j=1; j <= 10; j++) {
                switch (map[i][j]) {
                    case 1: 
                         g.drawImage(seatAImage,columnInit+28*(j-1), rowInit+28*(i-1), this);
                         break;
                     case 2: 
                         g.drawImage(seatBImage,columnInit+28*(j-1), rowInit+28*(i-1), this);
                         break;
                     case 3:
                         g.drawImage(seatXImage,columnInit+28*(j-1), rowInit+28*(i-1), this);
                         break;
                     default: break;
                }
            }
        }
        g.setFont(f1);
        g.drawString("Terminal A", 15, 190);
        g.drawString("Terminal B", 415, 190);
        ConcertHallProj ch = ConcertHallProj.Instance;
        if (ch.isTerminalStopped(1)) g.setColor(Color.red);
        else g.setColor(Color.green); 
        g.fillOval(40, 200, 12, 12);
        if (ch.isTerminalStopped(2)) g.setColor(Color.red);
        else g.setColor(Color.green); 
        g.fillOval(440, 200, 12, 12);
    }
}
