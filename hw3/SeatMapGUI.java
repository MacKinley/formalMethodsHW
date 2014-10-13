/**
 * CIS 461: Formal Methods for Software Engineering
 * Concert Hall Seat Reservation
 * The concert hall project class: SeatMapGUI.java
 *
 * @author Haiping Xu
 * Created on Oct. 09, 2014
 **/


package cis461.chap4.concerthallproj;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class SeatMapGUI extends JFrame implements ActionListener {
    private SeatMap seatMap;
    private JLabel terminalA, terminalB;
    private JButton reserveA, reserveB, resetButton;
    private JLabel aFailureTimes, bFailureTimes, doubleBookingTimes;

    public SeatMapGUI(int[][] map) {
        seatMap = new SeatMap(map);
        setContentPane(makeContentPane());
        pack();
        setVisible(true);
    }

    private Container makeContentPane() {
        // seat panel
        JPanel seatPane = new JPanel();
        seatPane.add(seatMap);
        
        // put the images fold in package cis461.chap4.concerthallproj 
        // (i.e., in folder ...\src\cis461\chap4\concerthallproj)
        
        // info panel
        JPanel topInfoPane = new JPanel();
        ImageIcon icon = createImageIcon("images-concerthall/seat-A.jpg", "seat A");
        JLabel seatLabel = new JLabel(icon);
        topInfoPane.add(seatLabel);
        JLabel description = new JLabel("(Terminal A)  ");
        topInfoPane.add(description);
        icon = createImageIcon("images-concerthall/seat-B.jpg", "seat B");
        seatLabel = new JLabel(icon);
        topInfoPane.add(seatLabel);
        description = new JLabel("(Terminal B)  ");
        topInfoPane.add(description);
        icon = createImageIcon("images-concerthall/seat-X.jpg", "seat X");
        seatLabel = new JLabel(icon);
        topInfoPane.add(seatLabel);
        description = new JLabel("(Double Booking)");
        topInfoPane.add(description);    
        
        JPanel bottomInfoPane = new JPanel();
        JLabel aFail = new JLabel("Failures (Terminal A): ");
        JLabel bFail = new JLabel("     Failures (Terminal B): ");
        JLabel xFail = new JLabel("     Double Booking: ");
        aFailureTimes = new JLabel("0");
        bFailureTimes = new JLabel("0");
        doubleBookingTimes = new JLabel("0");
        bottomInfoPane.add(aFail);
        bottomInfoPane.add(aFailureTimes);
        bottomInfoPane.add(bFail);
        bottomInfoPane.add(bFailureTimes);
        bottomInfoPane.add(xFail);
        bottomInfoPane.add(doubleBookingTimes);
        
        JPanel infoPane = new JPanel();
        infoPane.setLayout(new BorderLayout());
        infoPane.add(topInfoPane, BorderLayout.NORTH);
        infoPane.add(bottomInfoPane, BorderLayout.SOUTH);
        
        // button panel
        terminalA = new JLabel("Terminal A: ");
        terminalB = new JLabel("  Terminal B: ");
       
        reserveA = new JButton("Start Booking");
        reserveA.setActionCommand("ReserveA");
        reserveA.addActionListener(this);

        reserveB = new JButton("Start Booking");
        reserveB.setActionCommand("ReserveB");
        reserveB.addActionListener(this);
        
        JLabel space = new JLabel("  ");
        resetButton = new JButton("Reset All");
        resetButton.setActionCommand("Reset");
        resetButton.addActionListener(this);
        
        JPanel buttonPane = new JPanel();
        buttonPane.add(terminalA);
        buttonPane.add(reserveA);
        buttonPane.add(terminalB);
        buttonPane.add(reserveB);
        buttonPane.add(space);
        buttonPane.add(resetButton);

        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());
        mainPane.add(seatPane, BorderLayout.NORTH);
        mainPane.add(infoPane, BorderLayout.CENTER);
        mainPane.add(buttonPane, BorderLayout.SOUTH);
        mainPane.setBorder(BorderFactory.createMatteBorder(1,1,2,2,Color.blue));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        return mainPane;
    }
        
    public void updateMap() {
        seatMap.repaint();
    }
    
    public void updateFailureTimes(int terminal, int failureTimes) {
        if (terminal == 1) aFailureTimes.setText(failureTimes+"");
        else if (terminal == 2) bFailureTimes.setText(failureTimes+"");
        else if (terminal == 3) doubleBookingTimes.setText(failureTimes+"");
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        ConcertHallProj ch = ConcertHallProj.Instance;

        if (command.equals("ReserveA")) {
            ch.startTerminal(1);
        } else if (command.equals("ReserveB")) {
            ch.startTerminal(2);
        } else if (command.equals("Reset")) {
            ch.reset();
        }
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}