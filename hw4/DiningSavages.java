  /****************************************************************
   * DiningSavages.java (CIS 461, Fall 2014)                   *
   * Submitted By: Mackinley Trudeau, Harry Low, Austin Walker    *
   * SID: 01436482(Harry), 01238165(Mackinley), 01141214(Austin)  *
   * Date: 11/17/2014                                             *
   ****************************************************************/

public class DiningSavages {

    public final static DiningSavages Instance = new DiningSavages();
    private final int MaxServings = 3;
    private final int NumberOfSavages = 5;
    private final int ServingsToEat = 5; // Run until each savage has eaten this much

    private void init() {
        Pot sharedPot = new Pot();

        // Initialize all the threads
        Thread cook = new Thread(new Cook(sharedPot));

        Thread[] savages = new Thread[NumberOfSavages];
        for (int i=0; i<NumberOfSavages; i++) {
            savages[i] = new Thread(new Savage(i+1, sharedPot));
        }

        // Start the threads
        cook.start();
        for (int i=0; i<NumberOfSavages; i++) {
            savages[i].start();
        }
    }

    public static void main(String[] args) {
        DiningSavages ds = DiningSavages.Instance;
        ds.init();
    }

    private class Pot {
        public int numServings;

        public Pot() {
            numServings = 0;
            System.out.println("Creating Pot shared object...");
            System.out.format("MaxServings = %d NumberOfSavages = %d\n", MaxServings, NumberOfSavages);
        }

        public synchronized void fillPot() {
            while (numServings != 0) try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            numServings = MaxServings;
            System.out.format("Cook refilled the pot...  [remaining servings = %d]\n", numServings);
            notifyAll();
        }

        public synchronized void eat(int savage)  {
            while (numServings == 0) try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            numServings--;
            System.out.format("Savage[%d] is eating...  [remaining servings = %d]\n", savage, numServings);
            notifyAll();
        }
    }

    private class Savage implements Runnable {
        private int id; // Keep track of our savages
        private Pot sharedPot;

        public Savage(int num, Pot p) {
            id = num;
            sharedPot = p;
            System.out.format("Creating Savage[%d]\n", id);
        }

        public void run() {
            for (int i=0; i< ServingsToEat; i++) {
                sharedPot.eat(id);
            }
        }
    }

    private class Cook implements Runnable {
        private Pot sharedPot;

        public Cook(Pot p) {
            System.out.println("Creating cook...");
            sharedPot = p;
        }

        public void run() {
            while (true) {
                sharedPot.fillPot();
            }
        }
    }
}
