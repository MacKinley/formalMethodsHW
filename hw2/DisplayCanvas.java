/**
 * Please do not modify this file!!!
 */

/**
 * CIS 461: Formal Methods for Software Engineering
 * Museum - Multi-Threaded Programming Demonstration
 * The museum project class: DisplayCanvas.java
 *
 * @author Haiping Xu
 * Created on Sept. 30, 2014
 **/

import java.awt.*;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class DisplayCanvas extends Canvas {
    private int value = 0;
    private String title;
    private boolean westDoorClose = false, eastDoorClose = false;
    private int displayType = 0;
    private boolean entering = false, leaving = false;
    private int stepEnter, stepLeave;

    private ImageIcon[] walkingman = new ImageIcon[18];

    private Font f1 = new Font("Helvetica", Font.BOLD, 36);
    private Font f2 = new Font("Times", Font.ITALIC+Font.BOLD, 20);

    public DisplayCanvas(String title, Color c) {
        super();
        this.title = title;
        setBackground(c);

        // put the images fold in package cis461.chap3.museumproj 
        // (i.e., in folder ...\src\cis461\chap3\museumproj)
        for (int i = 0; i < 18; i++) {
            walkingman[i] = new ImageIcon(this.getClass().getResource("images/walkingman-f" + (i+1) +".gif"));
        }
    }

    public void setValue(int val){
        value = val;
        repaint();
    }

    public void setDisplayType(int type) {
        displayType = type; // 1: West Exit; 2: Museum Display; 3: East Entrance
    }

    public void openWestDoor() {
        westDoorClose = false;
        repaint();
    }

    public void closeWestDoor() {
        westDoorClose = true;
        repaint();
    }

    public void openEastDoor() {
        eastDoorClose = false;
        repaint();
    }

    public void closeEastDoor() {
        eastDoorClose = true;
        repaint();
    }

    public void arrive(int step) {
        if (step == -1) entering = false;
        else entering = true;
        stepEnter = step;
        repaint();
    }

    public void depart(int step) {
        if (step == -1) leaving = false;
        else leaving = true;
        stepLeave = step;
        repaint();
    }

    public void update(Graphics g) {
        Graphics offgraphics;  // double buffering
        Image offscreen = createImage(getWidth(), getHeight());
        offgraphics = offscreen.getGraphics();
        offgraphics.setColor(getBackground());
        offgraphics.fillRect(0, 0, getWidth(), getHeight());
        offgraphics.setColor(getForeground());
        paint(offgraphics);
        g.drawImage(offscreen, 0, 0, this);
    }

    public void paint(Graphics g){
        g.setColor(Color.black);

        // Display the title
        g.setFont(f2);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(title);
        int h = fm.getHeight();
        int x = (getSize().width - w)/2;
        int y = h;
        g.drawString(title, x, y);
        g.drawLine(x, y+3, x+w, y+3);

        // Display the value
        g.setFont(f1);
        fm = g.getFontMetrics();
        String s1 = String.valueOf(value);
        w = fm.stringWidth(s1);
        h = fm.getHeight();
        x = (getSize().width - w)/2;
        y = (getSize().height+ h)/2;
        g.drawString(s1, x, y);

        w = (int) getSize().getWidth();
        h = (int) getSize().getHeight();
        g.setColor(Color.red);

        switch (displayType) {
            case 1: westExitAnimation(g, w, h); break;
            case 2: museumAnimation(g, w, h); break;
            case 3: eastEntranceAnimation(g, w, h); break;
            default: break;
        }
    }

    private void westExitAnimation(Graphics g, int w, int h) {
        if (leaving) {
            int index = stepLeave % 18;
            walkingman[index].paintIcon(this, g, w-stepLeave-25, h/2-5);
        }
    }

    private void museumAnimation(Graphics g, int w, int h) {
        if (westDoorClose) {
            g.fillRect(0, 0, 3, h-1);
        } else {
            g.fillRect(0, 0, 3, h/2-19);
            int[] xpoints = {0, 4, 24, 20};
            int[] ypoints = {h/2+20, h/2+20, h-10, h-10};
            g.fillPolygon(xpoints, ypoints, 4);
            g.fillRect(0, h/2+20, 3, h-1);
        }
        if (eastDoorClose) {
            g.fillRect(w-3, 0, w-3, h);
        } else {
            g.fillRect(w-3, 0, 3, h/2-19);
            int[] xpoints = {w-4, w, w-20, w-24};
            int[] ypoints = {h/2+20, h/2+20, h-10, h-10};
            g.fillPolygon(xpoints, ypoints, 4);
            g.fillRect(w-3, h/2+20, 3, h-1);
        }
    }

    private void eastEntranceAnimation(Graphics g, int w, int h) {
        if (entering) {
            int index = stepEnter % 18;
            walkingman[index].paintIcon(this, g, w/2-45-stepEnter, h/2-5);
        }
    }
}
