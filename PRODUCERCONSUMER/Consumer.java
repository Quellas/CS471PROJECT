package PRODUCERCONSUMER;

import java.util.concurrent.BlockingQueue;

public class  Consumer implements Runnable {
    private BlockingQueue<Integer> buffer;
    private int maxItems;

    public Consumer(BlockingQueue<Integer> buffer, int maxItems) {
        this.buffer = buffer;
        this.maxItems = maxItems;
    }

    public void run() {
        for (int i = 0; i < maxItems; i++) {
            try {
                int item = buffer.take();
                // Consume the item
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
