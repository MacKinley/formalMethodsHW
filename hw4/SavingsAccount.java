  /****************************************************************
   * SavingsAccount.java (CIS 461, Fall 2014)                   *
   * Submitted By: Mackinley Trudeau, Harry Low, Austin Walker    *
   * SID: 01436482(Harry), 01238165(Mackinley), 01141214(Austin)  *
   * Date: 11/17/2014                                             *
   ****************************************************************/
import java.util.Random;

public class SavingsAccount {

    public final static SavingsAccount Instance = new SavingsAccount();
    private final int MaxAmount = 2;
    private final int MaxBalance = 10;
    private final int NumCustomers = 2;
    private final int ExchangeTimes = 10; // Run until each customer has made this many withdrawals or deposits

    private void init() {
        BankAccount sharedAccount = new BankAccount();

        // Initialize all the customers
        Thread[] customers = new Thread[NumCustomers];
        for (int i=0; i<NumCustomers; i++) {
            customers[i] = new Thread(new Customer(i+1, sharedAccount));
        }

        // Start the customer threads
        for (int i=0; i<NumCustomers; i++) {
            customers[i].start();
        }
    }

    public static void main(String[] args) {
        SavingsAccount ds = SavingsAccount.Instance;
        ds.init();
    }

    private class BankAccount {
        public int balance;

        public BankAccount() {
            balance = 0;
            System.out.println("Creating BankAccount shared object...");
            System.out.format("MaxAmount = %d MaxBalance = %d\n", MaxAmount, MaxBalance);
        }

        public synchronized boolean deposit(int id, int amt) {
            while (balance+amt >= MaxBalance) {
                return false;
            }
            balance += amt;
            System.out.format("Customer[%d] deposited $%d...  [new balance = %d]\n", id, amt, balance);
            return true;
        }

        public synchronized boolean withdraw(int id, int amt)  {
            while (balance-amt <= 0) {
                return false;
            }
            balance -= amt;
            System.out.format("Customer[%d] withdrew $%d...  [new balance = %d]\n", id, amt, balance);
            return true;
        }
    }

    private class Customer implements Runnable {
        private int id; // Keep track of the customers
        private BankAccount account;

        public Customer(int num, BankAccount a) {
            id = num;
            account = a;
            System.out.format("Creating Customer[%d]\n", id);
        }

        public void run() {
            Random rand = new Random();

            int withdrawOrDeposit, amount, i=0;
            boolean success;

            while(i<ExchangeTimes) { 
                try{
                    Thread.sleep(rand.nextInt(1000));
                }catch(Exception e){}

                withdrawOrDeposit = rand.nextInt(2);

                amount = rand.nextInt(MaxAmount) + 1;

                if (withdrawOrDeposit == 0)
                    success = account.deposit(id, amount);
                else
                    success = account.withdraw(id, amount);
                
                if(success)
                    i++;
            }
        }
    }
}

