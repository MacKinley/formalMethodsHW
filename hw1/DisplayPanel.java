/**
 * Please do not modify this file!!!
 */

/**
 * CIS 461: Formal Methods for Software Engineering
 * FM Radio Demonstration
 * The radio project class: DisplayPanel.java
 *
 * @author Haiping Xu
 * Revised on Sept. 15, 2014
 **/

import java.awt.*;
import javax.swing.JPanel;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean on = false;
	private float frequency = 108;
    private String title;

    private Font f1 = new Font("Helvetica",Font.BOLD,36);
    private Font f2 = new Font("Times",Font.ITALIC+Font.BOLD,24);

    public DisplayPanel(String title) {
    	this.title = title;
    	setBackground(Color.green);
    }

    public void setValue(float value){
        this.frequency = value;
        repaint();
    }

    public boolean isOn() {
    	return on;
    }

    public void turnOn() {
    	on = true;
    	repaint();
    }

    public void turnOff() {
    	on = false;
    	repaint();
    }

    public void paintComponent(Graphics g) {
         g.setColor(Color.green);
         g.fillRect(0, 0, getWidth(), getHeight());

         // Display the title
         g.setFont(f2);
         g.setColor(Color.black);
         FontMetrics fm = g.getFontMetrics();
         int fontWidth = fm.stringWidth(title);
         int fontHeight = fm.getHeight();
         int x = (getSize().width - fontWidth)/2;
         int y = fontHeight;
         g.drawString(title, x, y);
         g.drawLine(x, y+3, x+fontWidth, y+3);

         // Display the content
         g.setFont(f1);
         g.setColor(Color.red);
         fm = g.getFontMetrics();
         String content;
         if (!on) content = "----";
         else content = String.valueOf(Math.round(frequency*10)/10.0) + " MHz";
         fontWidth = fm.stringWidth(content);
         fontHeight = fm.getHeight();
         x = (getSize().width - fontWidth)/2;
         y = (getSize().height + fontHeight)/2;
         g.drawString(content, x, y);
     }
}
