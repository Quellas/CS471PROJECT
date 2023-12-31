package PRODUCERCONSUMER;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    private static PrintStream fileStream;

    public static void main(String[] args) throws InterruptedException, IOException {

        String fileName="input-6sec-wait.txt";

        InputStream inputStream = ProducerConsumer.class.getResourceAsStream(fileName);

        if (inputStream == null) {
            System.err.println("File not found: " + fileName);
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            // Skip empty lines
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split("\t");
            if (parts.length >= 3) {
                sleepTime = Integer.parseInt(parts[0]);
                producerThreads = Integer.parseInt(parts[1]);
                consumerThreads = Integer.parseInt(parts[2]);
            } else {
                System.err.println("Invalid line format: " + line);
            }

            redirectOutputToFile("output_sleep_" + sleepTime + "ms.txt","PRODUCERCONSUMER");

            // Record start time
            long startTime = System.currentTimeMillis();

            // Create producer threads
            for (int i = 0; i < producerThreads; i++) {
                Thread producerThread  = new Thread(Producer);
                producerThread.start();

            }
            // Create consumer threads
            for (int i = 0; i < consumerThreads; i++) {
                Thread consumerThread= new Thread(Consumer);
                consumerThread.start();
            }

            // Sleep and allow the program to run
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Calculate and print overall turnaround time
            long turnaroundTime = 0l;
            long endTime = System.currentTimeMillis();
            turnaroundTime = endTime - startTime;
            System.out.println("SleepTime/Producers/Consumers: "+sleepTime+ "/"+producerThreads+"/"+consumerThreads+": "+"Overall turnaround time: " + turnaroundTime + " milliseconds");
            closeOutputStream();
        }
        reader.close();
        // Exit the program
        System.exit(0);
    }

    // Producer thread logic
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
    // Consumer thread logic
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

    // Insert item into the buffer
    static int insertItem(int item) throws InterruptedException {
        semFull.acquire();
        mutex.acquire();
        // Insert the item
        count++;
        System.out.println("SleepTime/Producers/Consumers: "+sleepTime+ "/"+producerThreads+"/"+consumerThreads+": "+"Producer produced item: " + item + " count: " + count);
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

    // Remove item from the buffer
     static int removeItem(int[] item) throws InterruptedException {
        semEmpty.acquire();
         mutex.acquire();
        // Remove the item
        count--;
        item[0] = buffer[removePosition];
        System.out.println("SleepTime/Producers/Consumers: "+sleepTime+ "/"+producerThreads+"/"+consumerThreads+": "+"Consumer consumed item: " + item[0] + " count: " + count);
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

    private static void redirectOutputToFile(String filename, String subdirectory){
        try {
            // Create the subdirectory if it doesn't exist
            File dir = new File(subdirectory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Use FileOutputStream with append mode
            FileOutputStream fileOutputStream = new FileOutputStream(subdirectory + File.separator + filename, true);
            fileStream = new PrintStream(fileOutputStream);
            System.setOut(fileStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Call this method to close the file stream after processing each input line
    private static void closeOutputStream() {
        if (fileStream != null) {
            fileStream.close();
        }
    }

}
