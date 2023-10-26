package CPUSCHED;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

class Process {
    int id;
    int arrivalTime;
    int burstTime;
    int priority;

    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class Main {
    public static void main(String[] args) {
        Queue<Process> processes = new LinkedList<>();
        int processesExecuted = 0;
        int totalBurstTime = 0;

        try {
            Scanner scanner = new Scanner(new File("datafile1.txt"));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
    
            if (parts.length == 4) { // Ensure there are enough fields in the line
            int id = Integer.parseInt(parts[0]);
            int arrivalTime = Integer.parseInt(parts[1]);
            int burstTime = Integer.parseInt(parts[2]);
            int priority = Integer.parseInt(parts[3]);
        
            Process process = new Process(id, arrivalTime, burstTime, priority);
            processes.add(process);
        }
                // Parse the line and create Process objects
                // Add the processes to the queue
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Simulate scheduling for each algorithm
        processesExecuted = runFIFO(processes, processesExecuted, totalBurstTime);
        totalBurstTime = runSJF(processes, processesExecuted, totalBurstTime);
        runPriority(processes, processesExecuted, totalBurstTime);

        // Calculate and output statistics
        // Save results to output files
    }

    public static int runFIFO(Queue<Process> processes, int processesExecuted, int totalBurstTime) {
        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0;

        while (!processes.isEmpty() && processesExecuted < 500) {
            Process currentProcess = processes.poll();
            if (currentProcess.arrivalTime <= currentTime) {
                readyQueue.add(currentProcess);
                currentTime += currentProcess.burstTime;
                totalBurstTime += currentProcess.burstTime;
                processesExecuted++;
            }
        }

        // Calculate statistics and print or save the results
        return processesExecuted;
    }

    public static int runSJF(Queue<Process> processes, int processesExecuted, int totalBurstTime) {
        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0;

        while (processesExecuted < 500) {
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime) {
                    readyQueue.add(process);
                }
            }

            if (!readyQueue.isEmpty()) {
                Process shortestJob = readyQueue.poll();
                currentTime += shortestJob.burstTime;
                totalBurstTime += shortestJob.burstTime;
                processesExecuted++;
            }
        }

        // Calculate statistics and print or save the results
        return totalBurstTime;
    }

    public static void runPriority(Queue<Process> processes, int processesExecuted, int totalBurstTime) {
        PriorityQueue<Process> readyQueue = new PriorityQueue<>((p1, p2) -> p1.priority - p2.priority);
        int currentTime = 0;

        while (processesExecuted < 500) {
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime) {
                    readyQueue.add(process);
                }
            }

            if (!readyQueue.isEmpty()) {
                Process highPriorityProcess = readyQueue.poll();
                currentTime += highPriorityProcess.burstTime;
                totalBurstTime += highPriorityProcess.burstTime;
                processesExecuted++;
            }
        }

        // Calculate statistics and print or save the results
    }
}
