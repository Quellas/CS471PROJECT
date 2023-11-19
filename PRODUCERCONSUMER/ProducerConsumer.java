package PRODUCERCONSUMER;

import java.util.Random;
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

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        args = new String[] { "2000", "1", "16" };
        if (args.length != 3) {
            System.out.println("Must pass three positive integer parameters");
            return;
        }

        int sleepTime = 2000;
        int producerThreads = 16;
        int consumerThreads = 4;

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

     int count1=0;
        // Join on threads
        for (Thread producerThread : producerThreadHandles) {
            try {
                producerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count1++;
            if(count1==producerThreads){
                break;

            }
        }

        int count2=0;
        for (Thread consumerThread : consumerThreadHandles) {
            try {
                consumerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(count2==consumerThreads){
                break;

            }
        }

        long endTime = System.currentTimeMillis();
        long turnaroundTime = endTime - startTime;

        System.out.println("Overall turnaround time: " + turnaroundTime + " milliseconds");
    }

    private static Runnable Producer = () -> {
        while (!stopThreads) {
            try {
                Thread.sleep(2000);
                Random random = new Random();
                int randomProduced = random.nextInt();

                if (insertItem(randomProduced) != 0) {
                    System.out.println("Could not insert item");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(stopThreads){
                break;
            }
        }
    };

    private static Runnable Consumer = () -> {
        while (!stopThreads) {
            try {
                Thread.sleep(2000);
                //Random random = new Random();
                int[] randomNum = new int[20];
                if (removeItem(randomNum) != 0) {
                    System.out.println("Could not remove item");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(stopThreads){
                break;
            }
        }
    };

    private static int insertItem(int item) throws InterruptedException {
        semFull.acquire();
        mutex.acquire();

        // Insert the item
        count++;
        System.out.println("Producer produced " + item + " count = " + count);
        buffer[insertPosition] = item;
       // insertPosition = (insertPosition + 1) % BUFFER_SIZE;
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

    private static int removeItem(int[] item) throws InterruptedException {
        semEmpty.acquire();
        mutex.acquire();

        // Remove the item
        count--;
        item[0] = buffer[removePosition];
        System.out.println("Consumer consumed " + item[0] + " count = " + count);
        //removePosition = (removePosition + 1) % BUFFER_SIZE;
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
