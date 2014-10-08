/****************************************************************
 * Program Assignment #2 (CIS 461, Fall 2014)                   *
 * Submitted By: Mackinley Trudeau, Harry Low, Austin Walker    *
 * SID: 01436482(Harry), 01238165(Mackinley), 01141214(Austin)  *
 * Date: 10/08/2014                                             *
 ****************************************************************/

/**
 * CIS 461: Formal Methods for Software Engineering
 * Museum Demonstration
 * The museum project class: MuseumProj.java
 *
 * @author Haiping Xu
 * Created on Sept. 30, 2014
 **/

import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MuseumProj extends JFrame {
    protected Thread museumControl, westExit, eastEntrance;
    private DisplayCanvas museumDisplay, westDisplay, eastDisplay;
    private JButton openButton, closeButton;
    
    public void init() {
        setTitle("CIS 461 Multi-Threaded Program: Museum");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up Display
        JPanel canvasPanel = new JPanel();
        museumDisplay = new DisplayCanvas("Museum", Color.cyan);
        westDisplay = new DisplayCanvas("Exit", Color.green);
        eastDisplay = new DisplayCanvas("Entrance", Color.green);
        museumDisplay.setSize(150, 100);
        westDisplay.setSize(150, 100);
        eastDisplay.setSize(150, 100);
        canvasPanel.setLayout(new FlowLayout());
        canvasPanel.add(westDisplay);
        canvasPanel.add(museumDisplay);
        canvasPanel.add(eastDisplay);

        westDisplay.setDisplayType(1);   // 1: West Exit
        museumDisplay.setDisplayType(2); // 2: Museum Display
        eastDisplay.setDisplayType(3);   // 3: East Entrance
        
        // Set up Director's Buttons
        JLabel director = new JLabel(" Director: ");

        openButton = new JButton("Open Museum");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Control.open = true;  // signals the controller that museum is open
                museumDisplay.openWestDoor();
                museumDisplay.openEastDoor();
                eastDisplay.arrive(-1);
            }
        });

        closeButton = new JButton("Close Museum");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Control.open = false; // signals the controller that museum is closed
                museumDisplay.closeEastDoor();
            }
        });

        JPanel directorPanel= new JPanel();
        directorPanel.add(director);
        directorPanel.add(openButton);
        directorPanel.add(closeButton);

        getContentPane().add(canvasPanel, BorderLayout.CENTER);
        getContentPane().add(directorPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }
    
    public DisplayCanvas getEastDisplay() {
        return eastDisplay;
    }

    public DisplayCanvas getWestDisplay() {
        return westDisplay;
    }

    public DisplayCanvas getMuseumDisplay() {
        return museumDisplay;
    }
    
    public void simulateArrival() {
        int delay = (int) (Math.random() * 1000);
        try { Thread.sleep(delay); } catch (InterruptedException e) { e.printStackTrace(); }
        for (int j = 0; j < 37; j++) {
            try { Thread.sleep(80); } catch (InterruptedException e) { e.printStackTrace(); }
            if (j == 36 && Control.open) eastDisplay.arrive(-1); // if open, erase the walkingman
            else eastDisplay.arrive(j); // walk towards the door
        }
    }
    
    public void simulateDeparture() {
        int delay = (int) (Math.random() * 3000) + 1000;
        try { Thread.sleep(delay); } catch (InterruptedException e) { e.printStackTrace(); }
        for (int j = 0; j < 37; j++) {
            try { Thread.sleep(80); } catch (InterruptedException e) { e.printStackTrace(); }
            if (j == 36) westDisplay.depart(-1); // erase the walkingman
            else westDisplay.depart(j); // walk away from the door
        }
    }
    

    //  ====>>>>> Complete the methods below this line! <<<<<====
    
    // Note: you are free to add any new data fields and/or new methods below this line (if needed)
    
    
    public static void main(String[] args) {
        // the main thread serves as the director ...
        MuseumProj museum = new MuseumProj();
        museum.init();

        Control museumControl = new Control(museum);
        EastEntrance east = new EastEntrance(museum);
        WestExit west = new WestExit(museum);
        
        museum.museumControl = new Thread(museumControl);
        museum.eastEntrance = new Thread(east);
        museum.westExit = new Thread(west);

        museum.museumControl.start();
        museum.eastEntrance.start();
        museum.westExit.start();
    }
}


class Control implements Runnable {
    protected final static int MAX = 20;
    protected static volatile boolean open, allowEnter, allowLeave;
    protected static volatile int arrivedCount, museumCount, leftCount;
    private MuseumProj museum;
    private DisplayCanvas display;

    public Control(MuseumProj museum) {
        this.museum = museum;
        display = museum.getMuseumDisplay();
    }

    public void run() {
        Control.arrivedCount = Control.MAX;

        while (Control.museumCount <= Control.MAX) {
            if (Control.open) {
                Control.allowEnter = true; 
                Control.allowLeave = true;
            }
            else {
                Control.allowEnter = false; 
                Control.allowLeave = true;
            }

            display.setValue(museumCount);
        }
    }
}


class EastEntrance implements Runnable {
    protected static volatile boolean arrival;
    private MuseumProj museum;
    private DisplayCanvas display;

    public EastEntrance(MuseumProj museum) {
        this.museum = museum;
        display = museum.getEastDisplay();  
    }

    public void run() {
        while (Control.arrivedCount > 0) {
            EastEntrance.arrival = false;

            if (Control.open) {
                museum.simulateArrival();

                while (!Control.allowEnter);
                Control.arrivedCount--;
                Control.museumCount++;
                EastEntrance.arrival = true;
                display.setValue(Control.arrivedCount);
            }
        }
    }
}

class WestExit implements Runnable {
    protected static volatile boolean departure;
    private MuseumProj museum;
    private DisplayCanvas display;

    public WestExit(MuseumProj museum) {
        this.museum = museum;
        display = museum.getWestDisplay();
    }

    public void run() {
        while (Control.leftCount <= Control.MAX) {
            WestExit.departure = false;

            if (Control.museumCount > 0 && Control.allowLeave) {
                museum.simulateDeparture();
                Control.museumCount--;
                Control.leftCount++;
                WestExit.departure = true;
                display.setValue(Control.leftCount);
            }
        }
    }
}
