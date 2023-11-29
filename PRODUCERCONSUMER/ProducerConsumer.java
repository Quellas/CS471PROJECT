package PRODUCERCONSUMER;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
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

    private static volatile boolean stopThreads = false;

    private static  int sleepTime = 2000;
    private static int producerThreads = 16;
    private static int consumerThreads = 4;
    private static CountDownLatch producerLatch = new CountDownLatch(producerThreads);
    private static CountDownLatch consumerLatch = new CountDownLatch(consumerThreads);


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
             System.out.println("Must pass three positive integer parameters");
             return;
         }


         // Create the producer threads and insert into array
         Thread[] producerThreadHandles = new Thread[producerThreads];
         for (int i = 0; i < producerThreads; i++) {
             Thread producerThread = new Thread(Producer);
             producerThreadHandles[i] = producerThread;
             producerThread.start();

         }

         // Create the consumer threads and insert into array
         Thread[] consumerThreadHandles = new Thread[consumerThreads];
         for (int i = 0; i < consumerThreads; i++) {
             Thread consumerThread = new Thread(Consumer);
             consumerThreadHandles[i] = consumerThread;
             consumerThread.start();


         }

         // Sleep and allow the program to run
         try {
             Thread.sleep(sleepTime);
             // Signal threads to stop
             stopThreads = true;
         } catch (InterruptedException e) {
             e.printStackTrace();
         }

         long turnaroundTime = 0l;
         long endTime = System.currentTimeMillis();
         turnaroundTime = endTime - startTime;
         System.out.println("Overall turnaround time: " + turnaroundTime + " milliseconds");

         return;


    }

     static Runnable Producer = () -> {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(2000);
                Random random = new Random();
                int randomProduced = random.nextInt();
               //int item = insertItem(randomProduced);

                if (insertItem(randomProduced) != 0) {
                    System.out.println("Could not insert item");
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            if(stopThreads){
                break;
            }
        }
        producerLatch.countDown();
    };

    private static Runnable Consumer = () -> {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(2000);
                //Random random = new Random();
                int[] randomNum = new int[20];
                if (removeItem(randomNum) != 0) {
                    System.out.println("Could not remove item");
                }
            } catch (InterruptedException e) {
               // e.printStackTrace();
            }
            if(stopThreads){
                break;
            }
        }
        consumerLatch.countDown();
    };

    static int insertItem(int item) throws InterruptedException {
        semFull.acquire();
        mutex.acquire();

        // Insert the item
        count++;
        System.out.println("Producer produced " + item + " count = " + count);
        buffer[insertPosition] = item;
        ++insertPosition;
        if (insertPosition >=5) {
            insertPosition = 0;
        }else if (insertPosition<0||count>5) {
            System.out.println("Error in removing item");
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
        System.out.println("Consumer consumed " + item[0] + " count = " + count);
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
