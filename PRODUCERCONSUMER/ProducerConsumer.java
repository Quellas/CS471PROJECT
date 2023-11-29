package PRODUCERCONSUMER;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class ProducerConsumer {
    private static final int BUFFER_SIZE = 5;
    private static int[] buffer = new int[BUFFER_SIZE];
    private static Semaphore mutex = new Semaphore(1);
    private static Semaphore semEmpty = new Semaphore(0);
    private static Semaphore semFull = new Semaphore(BUFFER_SIZE);
    private static int insertPosition = 0;
    private static int removePosition = 0;
    private static int count = 0;
    private static  int sleepTime = 0;
    private static int producerThreads = 0;
    private static int consumerThreads = 0;

    public static void main(String[] args) throws InterruptedException {

         Scanner sc = new Scanner(System.in);
         System.out.println("Enter sleep time in milliseconds: ");
         sleepTime = sc.nextInt();
         System.out.println("Enter number of producer threads: ");
         producerThreads = sc.nextInt();
         System.out.println("Enter number of consumer threads: ");
         consumerThreads = sc.nextInt();

         long startTime = System.currentTimeMillis();
         if (sleepTime < 0 || producerThreads < 0 || consumerThreads < 0) {
             System.out.println("Please enter 3 positive integers");
             return;
         }

         for (int i = 0; i < producerThreads; i++) {
             Thread producerThread = new Thread(Producer);
             producerThread.start();
         }

         for (int i = 0; i < consumerThreads; i++) {
             Thread consumerThread = new Thread(Consumer);
             consumerThread.start();
         }

         // Sleep and allow the program to run
         try {
             Thread.sleep(sleepTime);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }

         long turnaroundTime = 0l;
         long endTime = System.currentTimeMillis();
         turnaroundTime = endTime - startTime;
         System.out.println("Overall turnaround time: " + turnaroundTime + " milliseconds");

        System.exit(0);
    }

     static Runnable Producer = () -> {
        while (true) {
            try {
                Thread.sleep(1000);
                Random random = new Random();
                int randomProduced = random.nextInt(100);
                if (insertItem(randomProduced) != 0) {
                    System.out.println("Fail to insert item");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };

    private static Runnable Consumer = () -> {
        while (true) {
            try {
                Thread.sleep(1000);
                int[] randomNum = new int[20];
                if (removeItem(randomNum) != 0) {
                    System.out.println("Fail to remove item");
                }
            } catch (InterruptedException e) {
               e.printStackTrace();
            }

        }
    };

    static int insertItem(int item) throws InterruptedException {
        semFull.acquire();
        mutex.acquire();
        // Insert the item
        count++;
        System.out.println("Producer produced item: " + item + " count: " + count);
        buffer[insertPosition] = item;
        ++insertPosition;
        if (insertPosition >=5) {
            insertPosition = 0;
        }else if (insertPosition<0||count>5) {
            System.out.println("Error in inserting item");
            return -1;
        }
        // Release the mutex and semEmpty to allow remove to run
        mutex.release();
        semEmpty.release();
        return 0;
    }


     static int removeItem(int[] item) throws InterruptedException {
        semEmpty.acquire();
        mutex.acquire();
        // Remove the item
        count--;
        item[0] = buffer[removePosition];
        System.out.println("Consumer consumed item: " + item[0] + " count: " + count);
         ++removePosition;
         if (removePosition >=5) {
            removePosition = 0;
        }else if (removePosition<0||count<0) {
            System.out.println("Error in removing item");
            return -1;
         }
        // Release the mutex and semFull to allow insert to run
        mutex.release();
        semFull.release();
        return 0;
    }
}
