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
    protected final static int MAX = 3;
    protected static volatile boolean open, allowEnter, allowLeave;
    protected static volatile int count; // Keep track of how many people are in the museum
    private MuseumProj museum;
    private DisplayCanvas display;

    public Control(MuseumProj museum) {
        this.museum = museum;
        display = museum.getMuseumDisplay();
    }

    public void run() {

        // This can loop constantly.
        // Sets the state of the entrance/exit, and then updates the museum's display.
        while (true) {
            if (Control.open) {
                Control.allowEnter = true; 
                Control.allowLeave = true;
            }
            else {
                Control.allowEnter = false; 
                Control.allowLeave = true;
            }

            display.setValue(Control.count);
        }
    }
}

class EastEntrance implements Runnable {
    protected static volatile boolean arrival;
    private MuseumProj museum;
    private DisplayCanvas display;
    private int numArrivals; // Keep track of how many people are arriving. Starts at Control.MAX.

    public EastEntrance(MuseumProj museum) {
        this.museum = museum;
        display = museum.getEastDisplay();  
        display.setValue(Control.MAX); // Display the initial value
        numArrivals = Control.MAX;
    }

    public void run() {

        // The entrance runs until there are no more arrivals
        while (numArrivals > 0) {
            museum.simulateArrival();

            while (!Control.allowEnter); //Wait until the door opens to let someone in

            numArrivals--;
            Control.count++;
            display.setValue(numArrivals);
        }
    }
}

class WestExit implements Runnable {
    protected static volatile boolean departure;
    private MuseumProj museum;
    private DisplayCanvas display;
    private int numDepartures; // Keep track of how many people have left. Starts at 0.

    public WestExit(MuseumProj museum) {
        this.museum = museum;
        display = museum.getWestDisplay();
        numDepartures = 0;
    }

    public void run() {

        // The exit runs until everyone has left
        while (numDepartures <= Control.MAX) {

            // If 1+ people are in the museum and allowLeave is true, we can have a departure
            if (Control.count > 0 && Control.allowLeave) {
                museum.simulateDeparture();

                Control.count--;
                numDepartures++;
                display.setValue(numDepartures);
            }
        }
    }
}
