# #CS471 PROJECT - PROBLEM 1

#AUTHOR

WILLIAM GLAVIN

# CPU Scheduling Simulation

This Java program simulates CPU scheduling algorithms using a collection of classes and methods. The program's primary functionalities are:

## Overview:

The code simulates CPU scheduling processes by defining a `Process` class and implementing various scheduling algorithms in the `Main` class.

## Process Class:

The `Process` class defines attributes such as ID, arrival time, burst time, and priority for simulated processes.

## Main Class:

The `Main` class serves as the entry point and orchestrates the execution of different scheduling algorithms.

### Functionalities:

1. **Parsing Data:** Reads process information from a data file (`datafile1.txt`) and populates a queue with simulated processes.
2. **Scheduling Algorithms:**

   - **FIFO (First-In-First-Out):** Executes processes in the order of their arrival and computes statistics such as total burst time, waiting time, and turnaround time.
   - **SJF (Shortest Job First):** Prioritizes processes with the shortest burst time, updating statistics based on process execution.
   - **Priority Scheduling:** Executes processes based on their priority levels, maintaining statistics during execution.
3. **Metrics Calculation:** Computes throughput, average waiting time, average turnaround time, and average response time for each scheduling algorithm.
4. **Saving Results:** Generates an output file (`SampleOutput.txt`) and saves calculated statistics and metrics for future reference or analysis.

## Usage:

To use this code:

1. Ensure a properly formatted data file (`datafile1.txt`) is available for input.
2. Run the program to execute the CPU scheduling simulation.
3. Review the generated `SampleOutput.txt` file containing statistics for the run.

## Running the Code:

Compile and execute the code using a Java development environment or command line.

### Sample Output:

Upon successful execution, the program generates an output file (`SampleOutput.txt`) containing statistics for the simulated run, including total processes, elapsed time, throughput, average waiting time, average turnaround time, and average response time.

Feel free to modify the code or input data for different simulations and analyses.
