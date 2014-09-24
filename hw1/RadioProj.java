/****************************************************************
 * Program Assignment #1 (CIS 461, Fall 2014)                   *
 * Submitted By: Mackinley Trudeau, Harry Low, Austin Walker    *
 * SID: Your new SIDs starting with "00" (Huh?)                 *
 * Date: 9/26/2014                                              *
 ****************************************************************/

/**
 * CIS 461: Formal Methods for Software Engineering
 * FM Radio Demonstration
 * The radio project class: RadioProj.java
 *
 * @author Haiping Xu
 * Revised on Sept. 15, 2014
 **/
import java.lang.Thread;
import java.lang.Math;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RadioProj implements Runnable, ActionListener {
	private final static int freqTop = 108;
	private final static int freqBottom = 88;
	private float frequency = freqTop;
	private double[] lockFrequency = {105.9, 101.8, 98.5, 95.6, 92.1};
	private DisplayPanel display;
	private JButton on;
	private JButton off;
	private JButton scan;
	private JButton reset;

	private volatile Thread searchChannel;

	public void init() {
		JFrame frame = new JFrame("FM Radio");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = frame.getContentPane();

 		display=new DisplayPanel("FM Radio");
		pane.add(display, BorderLayout.CENTER);

		JPanel p = new JPanel();
        on = new JButton("on");
        on.addActionListener(this);
        p.add(on);

        off = new JButton("off");
        off.addActionListener(this);
        p.add(off);

        scan = new JButton("scan");
        scan.addActionListener(this);
        p.add(scan);

        reset = new JButton("reset");
        reset.addActionListener(this);
        p.add(reset);

		pane.add(p, BorderLayout.SOUTH);

		frame.pack();
		frame.setSize(350, 300);
		frame.setVisible(true);
	}

	public static void main(String[] argv) {
		RadioProj radio = new RadioProj();
		radio.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("on")) {
			display.turnOn();
			frequency =  freqTop;
			display.setValue(frequency);
		}
		else if (e.getActionCommand().equals("off")) {
			display.turnOff();
			// searchChannel.stop(); -- deprecated, do not use
			searchChannel = null;
		}
		else if (e.getActionCommand().equals("scan")) {
			if (display.isOn()) scanning();
		}
		else if (e.getActionCommand().equals("reset")) {
			if (display.isOn()) reset();
		};
	}


    //  ====>>>>> Complete the methods below this line! <<<<<====

	private void scanning() {
        Thread scanner = new Thread(this);
        scanner.start();
	}

	private void reset() {
        frequency = freqTop;
        display.setValue(frequency);
	}

	@Override
	public void run() {
        while(true) {
            frequency = frequency-0.1f;
            display.setValue(frequency);
            
            // Pause execution so frequency changes can be seen better
            try {
                Thread.sleep(40);
            } catch(Exception e) {}

            // TODO fix this
            if((int)Math.ceil((double)frequency) == freqBottom) {
                return;
            }

            for(double i:lockFrequency) {
                if(String.format("%.1f",frequency).equals(Double.toString(i))) {
                    return;
                }
            }
        }
	}
}
