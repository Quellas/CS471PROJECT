# A CS471 Project Producer-Consumer Problem

# Author: Quellas Lin
This producer-consumer problem is developed in Java JDK 8 and can be compiled and run in any Java-supported IDE.

## Input files:
Three input files are included in the project folder, which include the same parameters except for the sleep time. 
The sleep time is specified in the file name, such as `input_sleep_1000ms.csv`, `input_sleep_2000ms.csv`, and `input_sleep_3000ms.csv`.

### Functionalities:
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


## Running the Code:
Compile and execute the code using a Java development environment or command line.

### The Output:
Upon successful execution, the program generates an output file (`output_sleep_xxxxms.csv`) containing all the outputs, including produced and consumed item numbers, overall turnaround time, and counts of producers and consumers.
The output file has the following structure:""output_sleep_xxxxms.txt"",where xxxx specifies the sleep time.  
output_sleep_2000ms.txt  
output_sleep_4000ms.txt  
output_sleep_6000ms.txt  

## Result's summary and explanation:
1 As we can see from the output file,the turnaround time is different for different sleep time,the number of producers and consumers.

2 The more producers are,more items are produced,which increase the payload of the buffer,so the turnaround time is longer.  
Similarly,the more consumers are,more items are consumed,which can also affect the turnaround time.  

3 The wait time affect the turnaround time greatly, and choosing a proper wait time can make all processes to complete smoothly.  

