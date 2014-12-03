  /****************************************************************
   * WarringNeighbors.java (CIS 461, Fall 2014)                   *
   * Submitted By: Mackinley Trudeau, Harry Low, Austin Walker    *
   * SID: 01436482(Harry), 01238165(Mackinley), 01141214(Austin)  *
   * Date: 12/3/2014                                             *
   ****************************************************************/

import java.util.Random;

public class WarringNeighbors {

    public final static WarringNeighbors Instance = new WarringNeighbors();
    private final int MaxPickingTime = 3; // Each neighbor will pick berries for at most 3 seconds
    private int totalBerries = 30; // Used so the simulation stops at some point
    
    private void init() {

        // Initialize the flags
        Flag flag1 = new Flag();
        Flag flag2 = new Flag();

        Thread n1 = new Thread(new Neighbor(1, flag1, flag2));
        Thread n2 = new Thread(new Neighbor(2, flag2, flag1));
                               
        // Start the threads
        n1.start();
        n2.start();
    }


    public static void main(String[] args) {
        WarringNeighbors wn = WarringNeighbors.Instance;
        wn.init();
    }

    private class Flag {
        private boolean up;
        
        public Flag() { up = false; }
        public synchronized void raise() { up = true; }
        public synchronized void lower()  { up = false; }
        public boolean isUp() { return up; }
    }

    private class Neighbor implements Runnable {
        private int id; // Id for the neighbor
        private Flag myFlag;
        private Flag theirFlag;

        public Neighbor(int num, Flag f1, Flag f2) {
            id = num;
            myFlag = f1;
            theirFlag = f2;
            System.out.format("Creating Neighbor[%d] thread\n", id);
        }

        public void pickBerries() {
            Random rand = new Random();
            int pickingTime = rand.nextInt(1000 * MaxPickingTime);
            
            System.out.format("\tNeighbor%d is picking wild berries\n", id);
            try {
                Thread.sleep(pickingTime);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            totalBerries--;
        }

        public void run() {
            while(totalBerries > 0) {
                myFlag.raise();

                if (theirFlag.isUp()) {
                    myFlag.lower();
                }
                else {
                    System.out.format("N%d enters the field\n", id);
                    pickBerries();
                    System.out.format("N%d exits the field\n", id);
                    myFlag.lower();
                }
            }
        }
    }
}
