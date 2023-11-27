package CPUSCHED;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

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
        } else {
            System.err.println("Invalid data format in file: " + line);
            }
        }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        
    // Run scheduling algorithms and calculate statistics
    processesExecuted = runFIFO(processes, processesExecuted, totalBurstTime, totalWaitingTime, totalTurnaroundTime);
    int processesExecutedSJF = processesExecuted; // Store the initial value of processesExecuted for SJF
    totalBurstTime = runSJF(processes, processesExecuted, totalBurstTime, totalWaitingTime, totalTurnaroundTime);
    int processesExecutedPriority = processesExecuted; // Store the initial value of processesExecuted for Priority
    runPriority(processes, processesExecuted, totalBurstTime, totalWaitingTime, totalTurnaroundTime);

    // Calculate statistics
    int totalProcesses = processesExecuted;
    double throughput = (double) totalProcesses / totalBurstTime;
    double avgWaitingTime = (double) totalWaitingTime / totalProcesses;
    double avgTurnaroundTime = (double) totalTurnaroundTime / totalProcesses;
    double avgResponseTime = avgWaitingTime; // Assuming response time is the same as waiting time

    // Calculate CPU utilization for each scheduling algorithm separately
    double cpuUtilizationFIFO = (double) totalBurstTime / totalProcesses;
    double cpuUtilizationSJF = (double) totalBurstTime / processesExecutedSJF;
    double cpuUtilizationPriority = (double) totalBurstTime / processesExecutedPriority;

    // Print or use CPU utilizations for each algorithm
    System.out.println("CPU Utilization for FIFO: " + cpuUtilizationFIFO);
    System.out.println("CPU Utilization for SJF: " + cpuUtilizationSJF);
    System.out.println("CPU Utilization for Priority: " + cpuUtilizationPriority);

    // Save results to output file
    saveResultsToFile(totalProcesses, totalBurstTime, throughput, cpuUtilizationFIFO, avgWaitingTime, avgTurnaroundTime, avgResponseTime);
        
    }

    public static int runFIFO(Queue<Process> processes, int processesExecuted, int totalBurstTime, int totalWaitingTime, int totalTurnaroundTime) {
        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0;

        while (!processes.isEmpty() && processesExecuted < 500) {
            Process currentProcess = processes.poll();
            if (currentProcess.arrivalTime <= currentTime) {
                readyQueue.add(currentProcess);
                totalWaitingTime += currentTime - currentProcess.arrivalTime;
                currentTime += currentProcess.burstTime;
                totalBurstTime += currentProcess.burstTime;
                totalTurnaroundTime += currentTime - currentProcess.arrivalTime;
                processesExecuted++;
            }
        }

        return processesExecuted;
    }
    
    public static int runSJF(Queue<Process> processes, int processesExecuted, int totalBurstTime, int totalWaitingTime, int totalTurnaroundTime) {
    List<Process> arrivalProcesses = new ArrayList<>(processes);
    Queue<Process> readyQueue = new LinkedList<>();
    int currentTime = 0;

    while (processesExecuted < 500) {
        Process shortestJob = null;
        int shortestBurst = Integer.MAX_VALUE;

        for (Process process : arrivalProcesses) {
            if (process.arrivalTime <= currentTime && process.burstTime < shortestBurst) {
                shortestJob = process;
                shortestBurst = process.burstTime;
            }
        }

        if (shortestJob != null) {
            readyQueue.add(shortestJob);
            totalWaitingTime += currentTime - shortestJob.arrivalTime;
            currentTime += shortestJob.burstTime;
            totalBurstTime += shortestJob.burstTime;
            totalTurnaroundTime += currentTime - shortestJob.arrivalTime;
            processesExecuted++;
            arrivalProcesses.remove(shortestJob); // Remove the processed job from the list
        } else {
            currentTime++; // No job available, move to the next time unit
        }
    }

    return processesExecuted;
}

public static void runPriority(Queue<Process> processes, int processesExecuted, int totalBurstTime, int totalWaitingTime, int totalTurnaroundTime) {
    List<Process> arrivalProcesses = new ArrayList<>(processes);
    PriorityQueue<Process> readyQueue = new PriorityQueue<>((p1, p2) -> p1.priority - p2.priority);
    int currentTime = 0;

    while (processesExecuted < 500) {
        for (Process process : arrivalProcesses) {
            if (process.arrivalTime <= currentTime) {
                readyQueue.add(process);
            }
        }

        if (!readyQueue.isEmpty()) {
            Process highPriorityProcess = readyQueue.poll();
            totalWaitingTime += currentTime - highPriorityProcess.arrivalTime;
            currentTime += highPriorityProcess.burstTime;
            totalBurstTime += highPriorityProcess.burstTime;
            totalTurnaroundTime += currentTime - highPriorityProcess.arrivalTime;
            processesExecuted++;
            arrivalProcesses.remove(highPriorityProcess); // Remove the processed job from the list
        } else {
            currentTime++; // No job available, move to the next time unit
        }
    }
}

    // Function to save results to output file
    public static void saveResultsToFile(int totalProcesses, int totalBurstTime, double throughput, double cpuUtilization,
                                         double avgWaitingTime, double avgTurnaroundTime, double avgResponseTime) {                                    
           try {
            // Debug print statements to check values before writing to the file
            System.out.println("Number of processes: " + totalProcesses);
            System.out.println("Total elapsed time: " + totalBurstTime);
            PrintWriter writer = new PrintWriter("SampleOutput.txt");
            writer.println("Statistics for the Run:");
            writer.println("Number of processes: " + totalProcesses);
            writer.println("Total elapsed time: " + totalBurstTime);
            writer.println("Throughput: " + throughput);
            writer.println("CPU utilization: " + cpuUtilization);
            writer.println("Average waiting time: " + avgWaitingTime);
            writer.println("Average turnaround time: " + avgTurnaroundTime);
            writer.println("Average response time: " + avgResponseTime);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
