package a02;

import java.text.DecimalFormat;
import java.util.Arrays;

/* Comp10205 - Sorting Assignment

 Some of the sort code from courseWare textbook - Copyright, All rights reserved.
 Additional code added by C. Mark Yendt in May 2014 August 2019
 ADD Your Authorship and answers to Questions here :


 _________________________________________________

*/

/**
 *  Program will analyze Sorting and Performance Analysis.
 * This is the class header.
 *
 * @date Jan 29, 2022
 * @author DUC LONG NGUYEN (Paul)
 *
 * 1. Identify the type of sorts for each of the methods provided:
 *      aSort: Quick Sort
 * 		bSort: Selection Sort
 * 		cSort: Insertion Sort
 * 		dSort: Merge Sort
 * 		eSort: Bubble Sort
 * 2. List in order (fastest to slowest) my selection of algorithm to use when the array
 *      to be sorted contains 30 elements.
 *          Fastest:	 C Sort -> B Sort -> A Sort -> D Sort -> E Sort 	:Slowest
 * 3. List in order (fastest to slowest) my selection of algorithm to use when the array
 *      to be sorted contains 30000 elements.
 *          Fastest:	 A Sort -> D Sort -> C Sort -> B Sort -> E Sort 	:Slowest
 * 4. List the algorithm and the BIG O notation (time complexity, average case):
 *          aSort: Quick Sort     -> O(log n)   for time complexity and average case: n log n
 *          bSort: Selection Sort -> O(n^2)     for time complexity and average case: n^2
 *      	cSort: Insertion Sort -> O(n^2)     for time complexity and average case: n^2
 *          dSort: Merge Sort     -> O(n log n) for time complexity and average case: n log n
 *  	    eSort: Bubble Sort    -> O(n^2)     for time complexity and average case: n^2
 *      The Big O notation line up with my results for 30000 elements.
 * 5. The algorithm has the best performance of the basic step: B Sort (Selection Sort)
 *      As the result, this does not have any impact on my selection of the fastest algorithm
 *          when sorting an array of 30000 elements.
 *      Because the basic step is based on the time required to sort an array of a size n and
 *          dividing by the number of comparisons required for that algorithn
 *              (If the time required is the slowest, and the number of compares are millions,
 *              so, the basic step for the algorithm is so small.)
 *          However, the selection of the fastest algorithm is just based on the time required
 *          to sort an array.
 *          Therefore, It does not have any impact on the selection of the fastest algorithm.
 *  6. For the standard Arrays.sort method, we do not know the number of compares and
 *      the basic step.
 *      So we just evaluate the performance of standard sort method based on the time required.
 *      For the standard Arrays.sort method.
 *          The algorithm do the performance results most resemble:
 *              - C sort (means Insertion sort, when sorting an array of 30 elements).
 *              - A sort (means Quick sort, when sorting an array of 30000 elements).
 */
public class Assignment2_Start {

    /**
     * The swap method swaps the contents of two elements in an int array.
     *
     * @param array containing the two elements.
     * @param a     The subscript of the first element.
     * @param b     The subscript of the second element.
     */
    private static void swap(int[] array, int a, int b) {
        int temp;
        temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    /**
     * The Quick sort - manages first call
     *
     * @param array an unsorted array that will be sorted upon method completion
     * @return
     */
    public static int aSort(int array[]) {
        return doASort(array, 0, array.length - 1, 0);
    }

    /**
     * The doASort method uses the Quick algorithm to sort
     *
     * @param array The array to sort.
     * @param start The starting subscript of the list to sort
     * @param end   The ending subscript of the list to sort
     * @param count the number of comparisons required
     */
    private static int doASort(int array[], int start, int end, int count) {
        int pivotPoint;
        if (start>=end) return count;
        else {
            int[] pivotAndCount = part(array, start, end, count);
            // Get the pivot point.
            pivotPoint = pivotAndCount[0];
            // Note - only one +/=
            // Sort the first sub list.
            int firstRecursiveCount = pivotAndCount[1];
            int secondRecursiveCount = doASort(array, start, pivotPoint - 1, firstRecursiveCount);

            // Sort the second sub list.
            return doASort(array, pivotPoint + 1, end, secondRecursiveCount);
        }
    }

    /**
     * The partition method selects a pivot value in an array and arranges the
     * array into two sub lists. All the values less than the pivot will be
     * stored in the left sub list and all the values greater than or equal to
     * the pivot will be stored in the right sub list.
     *
     * @param array The array to partition.
     * @param start The starting subscript of the area to partition.
     * @param end   The ending subscript of the area to partition.
     * @param count the number of comparisons required
     * @return The subscript of the pivot value.
     */
    private static int[] part(int array[], int start, int end, int count) {
        int pivotValue;    // To hold the pivot value
        int endOfLeftList; // Last element in the left sub list.
        int mid;           // To hold the mid-point subscript

        // see http://www.cs.cmu.edu/~fp/courses/15122-s11/lectures/08-qsort.pdf
        // for discussion of middle point
        // Find the subscript of the middle element.
        // This will be our pivot value.
        mid = (start + end) / 2;

        // Swap the middle element with the first element.
        // This moves the pivot value to the start of
        // the list.
        swap(array, start, mid);

        // Save the pivot value for comparisons.
        pivotValue = array[start];

        // For now, the end of the left sub list is
        // the first element.
        endOfLeftList = start;

        // Scan the entire list and move any values that
        // are less than the pivot value to the left
        // sub list.
        for (int scan = start + 1; scan <= end; scan++) {
            count++;
            if (array[scan] < pivotValue) {
                endOfLeftList++;
                swap(array, endOfLeftList, scan);
            }
        }

        // Move the pivot value to end of the
        // left sub list.
        swap(array, start, endOfLeftList);

        // Return the subscript of the pivot value.
        return new int[]{endOfLeftList, count};
    }

    /**
     * An implementation of the Selection Sort Algorithm
     *
     * @param array an unsorted array that will be sorted upon method completion
     * @return
     */

    public static int bSort(int[] array) {
        int startScan;   // Starting position of the scan
        int index;       // To hold a subscript value
        int minIndex;    // Element with smallest value in the scan
        int minValue;    // The smallest value found in the scan
        int count = 0;

        // The outer loop iterates once for each element in the
        // array. The startScan variable marks the position where
        // the scan should begin.
        for (startScan = 0; startScan < (array.length - 1); startScan++) {
            // Assume the first element in the scannable area
            // is the smallest value.
            minIndex = startScan;
            minValue = array[startScan];

            // Scan the array, starting at the 2nd element in
            // the scannable area. We are looking for the smallest
            // value in the scannable area.
            for (index = startScan + 1; index < array.length; index++) {
                count++;
                if (array[index] < minValue) {
                    minValue = array[index];
                    minIndex = index;
                }
            }

            // Swap the element with the smallest value
            // with the first element in the scannable area.
            array[minIndex] = array[startScan];
            array[startScan] = minValue;
        }
        return count;
    }

    /**
     * An implementation of the Insertion Sort algorithm
     *
     * @param array an unsorted array that will be sorted upon method completion
     */
    public static int cSort(int[] array) {
        int unsortedValue;  // The first unsorted value
        int scan;           // Used to scan the array
        int count=0;

        // The outer loop steps the index variable through
        // each subscript in the array, starting at 1. The portion of
        // the array containing element 0  by itself is already sorted.
        for (int index = 1; index < array.length; index++) {
            // The first element outside the sorted portion is
            // array[index]. Store the value of this element
            // in unsortedValue.
            unsortedValue = array[index];

            // Start scan at the subscript of the first element
            // outside the sorted part.
            scan = index;

            // Move the first element in the still unsorted part
            // into its proper position within the sorted part.
            count++;
            while (scan > 0 && array[scan - 1] > unsortedValue) {
                array[scan] = array[scan - 1];
                scan--;
            }

            // Insert the unsorted value in its proper position
            // within the sorted subset.
            array[scan] = unsortedValue;
        }
        return count;
    }


    /**
     * completes a Merge sort on an array
     *
     * @param array the unsorted elements - will be sorted on completion
     */
    public static int dSort(int array[]) {
        return doDSort(array, 0, array.length - 1, 0);
    }

    /**
     * private recursive method that splits array until we start at array sizes of 1
     *
     * @param array       starting array
     * @param lowerIndex  lower bound of array to sort
     * @param higherIndex upper bound of array to sort
     * @param count the number of comparisons required
     */
    private static int doDSort(int[] array, int lowerIndex, int higherIndex, int count) {
        if (lowerIndex >= higherIndex) return count;
        else{
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            int firstRecursiveCount = doDSort(array, lowerIndex, middle, count);
            // Below step sorts the right side of the array
            int secondRecursiveCount = doDSort(array, middle + 1, higherIndex, firstRecursiveCount);
            // Now put both parts together
            return part1(array, lowerIndex, middle, higherIndex, secondRecursiveCount);
        }
    }

    /**
     * Puts two smaller sorted arrays into one sorted array
     *
     * @param array       provided in two sorted parts (1,4,9,2,3,11)
     * @param lowerIndex  the location of the first index
     * @param middle      - the middle element
     * @param higherIndex - the upper bound of the sorted elements
     * @param count the number of comparisons required
     */
    private static int part1(int[] array, int lowerIndex, int middle, int higherIndex, int count) {

        int[] mArray = new int[higherIndex - lowerIndex + 1];
        for (int i = lowerIndex; i <= higherIndex; i++) {
            mArray[i - lowerIndex] = array[i];
        }
        // Part A - from lowerIndex to middle
        // Part B - from middle + 1 to higherIndex
        int partAIndex = lowerIndex;
        int partBIndex = middle + 1;
        int m = lowerIndex;
        while (partAIndex <= middle && partBIndex <= higherIndex) {
            // Place items back into Array in order
            // Select the lowestest of the two elements
            count++;
            if (mArray[partAIndex - lowerIndex] <= mArray[partBIndex - lowerIndex]) {
                array[m] = mArray[partAIndex - lowerIndex];
                partAIndex++;
            } else {
                array[m] = mArray[partBIndex - lowerIndex];
                partBIndex++;
            }
            m++;
        }
        // Copy the remainder of PartA array (if required)
        while (partAIndex <= middle) {
            array[m] = mArray[partAIndex - lowerIndex];
            m++;
            partAIndex++;
        }
        return count;
    }

    /**
     * Sorting using the Bubble Sort algorithm
     *
     * @param array an unsorted array that will be sorted upon method completion
     */
    public static int eSort(int[] array) {
        int lastPos;     // Position of last element to compare
        int index;       // Index of an element to compare
        int count = 0;

        // The outer loop positions lastPos at the last element
        // to compare during each pass through the array. Initially
        // lastPos is the index of the last element in the array.
        // During each iteration, it is decreased by one.
        for (lastPos = array.length - 1; lastPos >= 0; lastPos--) {
            // The inner loop steps through the array, comparing
            // each element with its neighbor. All of the elements
            // from index 0 thrugh lastPos are involved in the
            // comparison. If two elements are out of order, they
            // are swapped.
            for (index = 0; index <= lastPos - 1; index++) {
                // Compare an element with its neighbor.
                count++;
                if (array[index] > array[index + 1]) {
                    // Swap the two elements.
                    swap(array, index, index + 1);
                }
            }
        }
        return count;
    }

    /**
     * Print an array to the Console
     *
     * @param A array to be printed
     */
    public static void printArray(int[] A) {
        for (int i = 0; i < A.length; i++) {
            System.out.printf("%5d ", A[i]);
        }
        System.out.println();
    }

    /**
     * Answer some discussion questions in the assignment 02
     * @param array an unsorted array that will be sorted upon method completion
     * @return a string value with the information of the answers.
     * */
    private static String resultDiscussion(int[] array){
        for(int count = 0; count< array.length; count++)
            array[count] = (int) (Math.random()* array.length);
        double[] resultA = printResult(array, 'A');
        double[] resultB = printResult(array, 'B');
        double[] resultC = printResult(array, 'C');
        double[] resultD = printResult(array, 'D');
        double[] resultE = printResult(array, 'E');
        double[] resultS = printResult(array, 'S'); //S: Standard Arrays.sort

        // print List in order
        int[] list =new int[]{(int)resultA[1],(int)resultB[1],(int)resultC[1],(int)resultD[1],(int)resultE[1],(int)resultS[1]};
        String listString = "A"+list[0]+" "+"B"+list[1]+" "+"C"+list[2]+" "+"D"+list[3]+" "+"E"+list[4]+" "+"S"+list[5];
        int[] listToSort = Arrays.copyOf(list, list.length);
        eSort(listToSort);
        String listOrder = "Fastest:\t";
        String performanceResemble = "";
        for (int elementSort : listToSort) {
            char sortMethod = listString.charAt(listString.indexOf(elementSort+"")-1);

            // print performance results most resemble
            if(sortMethod=='S') {
                for (int count = 0; count<list.length;count++)
                    if(listToSort[count]==list[5])
                        performanceResemble = count==0 ? listString.charAt(listString.indexOf(listToSort[count+1]+"")-1)+""
                                : listString.charAt(listString.indexOf(listToSort[count-1]+"")-1)+"";
                continue;
            }
            listOrder+="-> "+sortMethod+" Sort ";
        }

        // print best performance of the basic step
        listOrder += "\t:Slowest\n*) Algorithm has the best performance of the basic step: ";
        double[] listBasicStep =new double[]{resultA[2],resultB[2],resultC[2],resultD[2],resultE[2]};
        listString = "A"+listBasicStep[0]+" "+"B"+listBasicStep[1]+" "+"C"
                    +listBasicStep[2]+" "+"D"+listBasicStep[3]+" "+"E"+listBasicStep[4];
        double[] listBasicStepToSort = Arrays.copyOf(listBasicStep, listBasicStep.length);
        for(int count1=0;count1<listBasicStep.length-1;count1++)
            for(int count2=listBasicStep.length-1;count2>count1;count2--)
                if(listBasicStepToSort[count2]<listBasicStepToSort[count2-1]){
                    double temp=listBasicStepToSort[count2];
                    listBasicStepToSort[count2]=listBasicStepToSort[count2-1];
                    listBasicStepToSort[count2-1]=temp;
                }
        char bestPerformance = listString.charAt(listString.indexOf(listBasicStepToSort[0]+"")-1);
        listOrder += "\t\t\t\t\t\t\t\t\t"+bestPerformance+" Sort.\n" +
                "*) For the standard Arrays.sort method. Algorithm do the performance " +
                "results most resemble: "+performanceResemble+" Sort.";
        return listOrder;
    }

    /**
     * Print the result to the Console, including:
     *  - The Array Size
     *  - Sort method, with:
     *      + 'A': aSort -> Quick sort
     *      + 'B': bSort -> Selection sort
     *      + 'C': cSort -> Insertion sort
     *      + 'D': dSort -> Merge sort
     *      + 'E': eSort -> Bubble sort
     *  - The number of compares
     *  - Time required
     *  - Basic Step
     * @param array an unsorted array that will be sorted upon method completion
     * @param sort the sort method (bubble, selection, insertion, merge or quick sort)
     * */
    public static double[] printResult(int[] array, char sort){
        int[] B = Arrays.copyOf(array, array.length);      // Make sure you do this before each call do a sort method
        long startTime;
        int sortCompares;
        long elapsedTime;
        switch (sort) {
            case 'A':
                startTime = System.nanoTime();
                sortCompares = aSort(B);
                elapsedTime = System.nanoTime() - startTime;
                break;
            case 'B':
                startTime = System.nanoTime();
                sortCompares = bSort(B);
                elapsedTime = System.nanoTime() - startTime;
                break;
            case 'C':
                startTime = System.nanoTime();
                sortCompares = cSort(B);
                elapsedTime = System.nanoTime() - startTime;
                break;
            case 'D':
                startTime = System.nanoTime();
                sortCompares = dSort(B);
                elapsedTime = System.nanoTime() - startTime;
                break;
            case 'E':
                startTime = System.nanoTime();
                sortCompares = eSort(B);
                elapsedTime = System.nanoTime() - startTime;
                break;
            default:
                startTime = System.nanoTime();
                Arrays.sort(B);
                elapsedTime = System.nanoTime() - startTime;
                sortCompares = 1;
                break;
        }
        double basicStep = Double.parseDouble(new DecimalFormat("#.#").format((double)elapsedTime/sortCompares));
        System.out.println((sort=='S' ? "____" : "\t")+(sort=='A' ? B.length : "...")
                +(sort=='S' ? " " : (sort=='A' && B.length>1000 ? "\t " : "\t\t "))
                +(sort=='S' ? "Standard (Arrays.sort) method" : (sort+" method"))+(sort=='S' ? " __" :"\t\t\t")
                +(sort=='S' ? "_" : sortCompares)
                +(sort=='S' ? "_________\t" :(sortCompares<100000000 ? (sortCompares<1000 ? "\t\t" : "\t") : "")+"\t\t\t")
                +elapsedTime+" ns"
                +(sort=='S' ? " \t______________" :(elapsedTime<100000000 ? (elapsedTime<10000 ? "\t\t\t":"\t\t"):"\t"))+"  "
                +(sort=='S' ? "" : (basicStep+" ns")));
        return new double[]{sortCompares, elapsedTime, basicStep};
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("\nAssignment#2 Sorting and Performance Analysis\n" +
                "Completed by Nguyen Duc Long - 000837437\n============================================\n");
        int size = 10;
        int[] A = new int[size];
        // Create random array with elements in the range of 0 to SIZE - 1;
        for (int i = 0; i < size; i++) {
            A[i] = (int) (Math.random() * size);
        }
        System.out.println("__Array Size___Sort_____Number of compares_______Time required______Basic Step__");
        resultDiscussion(A);
        A = new int[30];
        String result30 =resultDiscussion(A);
        A = new int[300];
        resultDiscussion(A);
        A = new int[30000];
        String result30000 = resultDiscussion(A);

        System.out.println("\nIndicate sort: \taSort: Quick Sort\n\t\t\t\tbSort: Selection Sort\t\t" +
                "dSort: Merge Sort\n\t\t\t\tcSort: Insertion Sort\t\teSort: Bubble Sort\n\n" +
                "*) List in order (fastest to slowest) my selection of algorithm to use when the array \n\t" +
                "to be sorted contains 30 elements.  Base this on your results \n\t\t"+result30 +
                "\n\n*) List in order (fastest to slowest) my selection of algorithm to use when the array \n\t" +
                "to be sorted contains 30000 elements.  Base this on your results \n\t\t"+result30000);

    }
}

