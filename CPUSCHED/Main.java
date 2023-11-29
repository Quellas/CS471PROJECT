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
        int[] statistics = new int[4]; // To hold processesExecuted, totalBurstTime, totalWaitingTime, totalTurnaroundTime

        try {
            Scanner scanner = new Scanner(new File("datafile1.txt"));
            scanner.nextLine(); // Skip the header line

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 1) {
                parts = line.trim().split("\\t");
                }
    
                if (parts.length == 3) { // Ensure there are exactly three fields in the line
                    int arrivalTime = Integer.parseInt(parts[0]);
                    int burstTime = Integer.parseInt(parts[1]);
                    int priority = Integer.parseInt(parts[2]);
            
                    Process process = new Process(0, arrivalTime, burstTime, priority);
                    processes.add(process);
                } else {
                    System.err.println("Invalid data format in file: " + line);
                }
        }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        
    runFIFO(new LinkedList<>(processes), statistics);
    int processesExecutedSJF = statistics[0];
    int totalBurstTimeSJF = statistics[1];
    runSJF(new LinkedList<>(processes), statistics);
    int processesExecutedPriority = statistics[0];
    int totalBurstTimePriority = statistics[1];
    runPriority(new LinkedList<>(processes), statistics);

    int totalProcesses = statistics[0];
    double throughputFIFO = (double) totalProcesses / statistics[1];
    double throughputSJF = (double) totalProcesses / totalBurstTimeSJF;
    double throughputPriority = (double) totalProcesses / totalBurstTimePriority;
    double avgWaitingTimeFIFO = (double) statistics[2] / totalProcesses;
    double avgTurnaroundTimeFIFO = (double) statistics[3] / totalProcesses;
    double avgResponseTimeFIFO = avgWaitingTimeFIFO;

    // Use or store these variables for later use
    System.out.println("Processes Executed SJF: " + processesExecutedSJF);
    System.out.println("Processes Executed Priority: " + processesExecutedPriority);
    System.out.println("Throughput SJF: " + throughputSJF);
    System.out.println("Throughput Priority: " + throughputPriority);

    // Save results to output file
    saveResultsToFile(totalProcesses, statistics[1], throughputFIFO, avgWaitingTimeFIFO, avgTurnaroundTimeFIFO, avgResponseTimeFIFO);
        
    }

    public static void runFIFO(Queue<Process> processes, int[] statistics) {
        int processesExecuted = 0;
        int totalBurstTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int currentTime = 0;

        while (!processes.isEmpty() && processesExecuted < 500) {
            Process currentProcess = processes.poll();
            if (currentProcess.arrivalTime <= currentTime) {
                totalWaitingTime += currentTime - currentProcess.arrivalTime;
                currentTime += currentProcess.burstTime;
                totalBurstTime += currentProcess.burstTime;
                totalTurnaroundTime += currentTime - currentProcess.arrivalTime;
                processesExecuted++;
            }
        }

        // Update statistics array with the values at the end of FIFO scheduling
        statistics[0] = processesExecuted;
        statistics[1] = totalBurstTime;
        statistics[2] = totalWaitingTime;
        statistics[3] = totalTurnaroundTime;
    }
    
    public static void runSJF(Queue<Process> processes, int[] statistics) {
        int processesExecuted = 0;
        int totalBurstTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int currentTime = 0;

        List<Process> arrivalProcesses = new ArrayList<>(processes);

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

        // Update statistics array with the values at the end of SJF scheduling
        statistics[0] = processesExecuted;
        statistics[1] = totalBurstTime;
        statistics[2] = totalWaitingTime;
        statistics[3] = totalTurnaroundTime;
    }

    public static void runPriority(Queue<Process> processes, int[] statistics) {
        int processesExecuted = 0;
        int totalBurstTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int currentTime = 0;

        List<Process> arrivalProcesses = new ArrayList<>(processes);
        PriorityQueue<Process> readyQueue = new PriorityQueue<>((p1, p2) -> p1.priority - p2.priority);

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

        // Update statistics array with the values at the end of Priority scheduling
        statistics[0] = processesExecuted;
        statistics[1] = totalBurstTime;
        statistics[2] = totalWaitingTime;
        statistics[3] = totalTurnaroundTime;
    }

    // Function to save results to output file
    public static void saveResultsToFile(int totalProcesses, int totalBurstTime, double throughput,
                                         double avgWaitingTime, double avgTurnaroundTime, double avgResponseTime) {
        try {
            String filePath = "SampleOutput.txt"; // Relative path
            System.out.println("Creating file at: " + filePath); // Debug print
            PrintWriter writer = new PrintWriter(filePath);
            writer.println("Statistics for the Run:");
            writer.println("Number of processes: " + totalProcesses);
            writer.println("Total elapsed time: " + totalBurstTime);
            writer.println("Throughput: " + throughput);
            writer.println("Average waiting time: " + avgWaitingTime);
            writer.println("Average turnaround time: " + avgTurnaroundTime);
            writer.println("Average response time: " + avgResponseTime);
            writer.close();
            System.out.println("File creation successful."); // Debug print
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
