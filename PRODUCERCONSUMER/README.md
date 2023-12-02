# Producer-Consumer Problem
This producer-consumer problem is developed in Java JDK 8 and can be compiled and run in any Java-supported IDE.

### Functionalities:
**Input data follows the program prompt:**
The program prompts the user to input the sleep time, the number of producers, and the number of consumers.

**Producer Thread (Producer):**
This thread alternates between sleeping for 1 second and producing a random integer (between 0 and 100) into the buffer using the insertItem() method.

**Consumer Thread (Consumer):**
Similar to the producer, it alternates between sleeping for 1 second and attempting to consume an item from the buffer using the removeItem() method.

**insertItem(int item):**
Acquires semaphores to ensure synchronization, then inserts the produced item into the buffer, updates positions, and releases semaphores.

**removeItem(int[] item):**
Acquires semaphores to ensure synchronization, then removes the consumed item from the buffer, updates positions, and releases semaphores.

**Save the output in files:**
Upon completion of execution, the program will save the output for each sleep time in a separate file. It will generate file names such as 'output_sleep_xxxxms.csv', where xxxx specifies the sleep time entered.

## Usage:

To use this code:  
1.Execute the program and input values for sleep time, the number of producers, and the number of consumers as prompted.  
2.Examine the resulting `output_sleep_xxxxms.csv` file, which includes details on the produced and consumed item numbers and counts.

## Running the Code:

Compile and execute the code using a Java development environment or command line.

### Sample Output:

Upon successful execution, the program generates an output file (`output_sleep_xxxxms.csv`) containing all the outputs, including produced and consumed item numbers, overall turnaround time, and counts of producers and consumers.

Feel free to modify the code or input data for different overall turnaround time.
