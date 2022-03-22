package a01;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
/**
 *  Program will analyze a set of elevation data collected in a text file. Then, calculate Mountain Peaks and Valleys
 * This is the class header.
 *
 * @date Jan 23, 2022
 * @author DUC LONG NGUYEN (Paul)
 */
public class Assignment01 {
    private static int minPeakHeight = 0;   //Minimum Peak Height
    private static int exclusionRadius = 0; //Exclusion Radius (used for peak detection).
    private static int rows = 0;    //Number of Rows
    private static int columns = 0; //Number of Columns
    private static final String fileName = "src/a01/ELEVATIONS.TXT";    //Name file

    /**
     * The method to read and store all data in a file.
     * @param fileName The name of file
     * @return a two-dimensional array ([row][column]) stored all the data from a file
     */
    private static int[][] arrNumbers(String fileName){
        File file = new File(fileName);
        Scanner fileInput = null;
        int[][] numbers = null;
        try{
            fileInput = new Scanner(file);
        }catch (FileNotFoundException ex){
            System.out.println("Could not find "+fileName);
            System.exit(0);
        }
        try{
            rows = fileInput.nextInt();
            columns = fileInput.nextInt();
            minPeakHeight =fileInput.nextInt();
            exclusionRadius =fileInput.nextInt();
            fileInput.nextLine();           //ignore the first line
            numbers = new int[rows][columns];
            int rowIndex = 0;
            while (fileInput.hasNextLine()){//read each line
                Scanner line = new Scanner(fileInput.nextLine());
                int columnIndex = 0;
                while (line.hasNext())      //get and store each numbers in each lines
                    numbers[rowIndex][columnIndex++]=line.nextInt();
                rowIndex++;
            }
        }catch (Exception ex){
            System.out.println(ex);
            System.exit(0);
        }
        return numbers;
    }

    /**
     *  Processing for Question 01:
     *      Print the lowest elevation value and the number of times it is found in the complete data set.
     * @param arrNumbers  a two-dimensional array ([row][column]) stored all the data from a file
     * @return a string contains the lowest elevation value
     *      and the number of times it is found in the complete data set.
     * */
    private static String minProcess(int[][] arrNumbers) {
        int min = arrNumbers[0][0];
        int countMin = 0;
        for(int[] numbers : arrNumbers)
            for(int number : numbers)
                if(number<min){
                    min=number;
                    countMin=1;
                }
                else if(number==min) countMin++;
        return min+" "+countMin;
    }

    /**
     * Processing for Question 02:
     *      Print all the local peaks where the peak elevation is greater or equal to 111300
     *          using an exclusion radius(n) of 13 in data set
     * - SUB_METHOD:  This method returns a boolean value if this value is in the local peak area or not.
     * @param arrNumbers  a two-dimensional array ([row][column]) stored all the data from a file
     * @param row  the row center
     * @param column  the column center
     * @return a boolean value if this value is in the local peak area or not.
     * */
    private static boolean isLocalPeak(int[][] arrNumbers, int row, int column){
        for (int rowIndex = row - exclusionRadius; rowIndex <= row + exclusionRadius; rowIndex++)
            for (int columnIndex = column - exclusionRadius; columnIndex <= column + exclusionRadius; columnIndex++) {
                if (rowIndex == row && columnIndex == column) continue;
                if (arrNumbers[rowIndex][columnIndex] >= arrNumbers[row][column]) return false;
            }
        return true;
    }
    /**
     *  Processing for Question 02:
     *      Print all the local peaks where the peak elevation is greater or equal to 111300
     *          using an exclusion radius(n) of 13 in data set
     * - MAIN_METHOD: This method to return a two-dimensional array contains all the local peaks
     *      and their position using an exclusion radius(n) of 13
     * @param arrNumbers  a two-dimensional array ([row][column]) stored all the data from a file
     * @return a two-dimensional array contains all the local peaks
     *      and their position using an exclusion radius(n) of 13 in data set
     * */
    private static int[][] arrLocalPeaks(int[][] arrNumbers) {
        int numberOfLocalPeaks = 0;
        for(int countRow = exclusionRadius; countRow<arrNumbers.length-exclusionRadius;countRow++)
            for(int countColumn = exclusionRadius; countColumn<arrNumbers[countRow].length-exclusionRadius;countColumn++)
                if (arrNumbers[countRow][countColumn]>=minPeakHeight)
                    if (isLocalPeak(arrNumbers,countRow,countColumn)) numberOfLocalPeaks++;
        int[][] arrPeaks = new int[numberOfLocalPeaks][3];
        int countPeak = 0;
        for(int countRow = exclusionRadius; countRow<arrNumbers.length-exclusionRadius;countRow++)
            for(int countColumn = exclusionRadius; countColumn<arrNumbers[countRow].length-exclusionRadius;countColumn++)
                if (arrNumbers[countRow][countColumn]>=minPeakHeight)
                    if (isLocalPeak(arrNumbers,countRow,countColumn)){
                        arrPeaks[countPeak][0] = arrNumbers[countRow][countColumn];
                        arrPeaks[countPeak][1] = countRow;
                        arrPeaks[countPeak][2] = countColumn;
                        countPeak++;
                    }
        return arrPeaks;
    }

    /**
     *  Processing for Question 03:
     *      Print the row and column and elevation of the two closest local peaks
     * @param arrPeaks  an array stored peaks and its position
     * @return a three-dimensional array contains the distance of the two closest local peaks, and
     *      the values and the positions of these peaks.
     * */
    private static int[][][] distanceProcess(int[][] arrPeaks) {
        double minDistance = Double.parseDouble((new DecimalFormat("0.00")).format(
                Math.sqrt(Math.pow(arrPeaks[0][1]-arrPeaks[1][1],2) + Math.pow(arrPeaks[0][2]-arrPeaks[1][2],2))));
        int countMinDistance = 1;
        for (int count1 = 0; count1 < arrPeaks.length; count1++)
            for (int count2 = 0; count2 < arrPeaks.length; count2++)
                if(count1!=count2){
                    double distance = Double.parseDouble((new DecimalFormat("0.00")).format(
                            Math.sqrt(Math.pow(arrPeaks[count1][1] - arrPeaks[count2][1], 2)
                            + Math.pow(arrPeaks[count1][2] - arrPeaks[count2][2], 2))));
                    if (distance<minDistance) {
                        minDistance = distance;
                        countMinDistance=1;
                    }
                    else if(distance==minDistance) countMinDistance++;
                }
        int[][][] listMinDistance = new int[countMinDistance/2][2][3];
        int countDistance = 0;
        for (int count1 = 0; count1 < arrPeaks.length; count1++)
            for (int count2 = 0; count2 < arrPeaks.length; count2++)
                if(count1!=count2){
                    double distance = Double.parseDouble((new DecimalFormat("0.00")).format(
                            Math.sqrt(Math.pow(arrPeaks[count1][1] - arrPeaks[count2][1], 2)
                                    + Math.pow(arrPeaks[count1][2] - arrPeaks[count2][2], 2))));
                    if(distance==minDistance){
                        boolean isAddDistance = true;
                        for (int[][] minDistances : listMinDistance)
                            if(minDistances[0][0] == arrPeaks[count2][0] && minDistances[0][1] == arrPeaks[count2][1]
                                    && minDistances[0][2] == arrPeaks[count2][2] && minDistances[1][0] == arrPeaks[count1][0]
                                    && minDistances[1][1] == arrPeaks[count1][1] && minDistances[1][2] == arrPeaks[count1][2])
                                isAddDistance=false;
                        if (isAddDistance) {
                            listMinDistance[countDistance][0] = arrPeaks[count1];
                            listMinDistance[countDistance][1] = arrPeaks[count2];
                            countDistance++;
                        }
                    }
                }
        return listMinDistance;
    }

    /**
     *  Processing for Question 04:
     *      Print the most common elevation in the data set.
     * @param arrNumbers  a two-dimensional array ([row][column]) stored all the data from a file
     * @param arrPeaks  an array stored peaks and its position
     * @return a string contains the most common elevation and its frequency in the data set.
     * */
    public static String frequencyProcess (int[][] arrNumbers, int[][] arrPeaks){
        int maxValue = arrPeaks[0][0];
        for (int[] peak : arrPeaks)
            if (peak[0]>maxValue) maxValue=peak[0];
        int[] frequencies = new int[maxValue + 1];           // creating the frequency array
        for (int count = 0; count < maxValue + 1; count++)  // initializing the listNumDup[] with 0
            frequencies[count] = 0;
        // incrementing the value at listNumbers[count]th index in the frequency array
        for(int[] numbers : arrNumbers)
            for(int number : numbers)
                frequencies[number]++;
        int mostCommonElevation = arrNumbers[0][0];
        int maxFrequency = frequencies[arrNumbers[0][0]];
        for(int[] numbers : arrNumbers)
            for(int number : numbers)
                if(frequencies[number]>maxFrequency){
                    mostCommonElevation = number;
                    maxFrequency = frequencies[number];
                }
        return mostCommonElevation+" "+maxFrequency;
    }

    /**
     * The actual main method that launches the app.
     * @param args unused
     */
    public static void main(String[] args) {
        long startTime = System.nanoTime();         //calculate timing
        int[][] arrNumbers = arrNumbers(fileName);  //read file and store data in a two-dimensional array
        long startTimeEfficiency = System.nanoTime();         //calculate timing
        //QUESTION 01
        String[] minProcess = minProcess(arrNumbers).split(" ");
        System.out.println("\nQuestion 01: \n\tThe lowest elevation value: "+Integer.parseInt(minProcess[0])
                +", and the number of times it is found in the complete data set: "+Integer.parseInt(minProcess[1]));
        //QUESTION 02
        int[][] arrPeaks = arrLocalPeaks(arrNumbers);
        System.out.println("Question 02:\n\tThere are "+arrPeaks.length+" local peaks which occur are:");
        for (int countPeak=0; countPeak<arrPeaks.length;countPeak++)
            System.out.print("\t\t"+(countPeak<99 ? "0"+(countPeak<9 ? "0":"") : "")+(countPeak+1)+") "
                    +arrPeaks[countPeak][0]+"(" +arrPeaks[countPeak][1]+","+arrPeaks[countPeak][2]+")"
                    +(arrPeaks[countPeak][1] <100 ? " " : "") + (arrPeaks[countPeak][2] <100 ? " " : "")
                    +((countPeak == (arrPeaks.length-1)) ? "\n" : ((countPeak+1)%5==0 ? "\n" : "\t")));
        //QUESTION 03
        int[][][] listDistance = distanceProcess(arrPeaks);
        System.out.println("Question 03:\n\tThere are "+listDistance.length+" sets of closest peaks. " +
                "The min distance would be "+ Double.parseDouble((new DecimalFormat("0.00")).format(Math.sqrt(
                Math.pow(listDistance[0][0][1]-listDistance[0][1][1],2) + Math.pow(listDistance[0][0][2]-listDistance[0][1][2],2))))
                +". The two peaks located at: ");
        for (int[][] twoMinDistance : listDistance)
            System.out.println("\t\t*) "+(twoMinDistance[0][1]<100 ? " " : "")+(twoMinDistance[0][2]<100 ? " " : "")+"("
                    +twoMinDistance[0][1]+","+twoMinDistance[0][2]+") with an elevation of "+twoMinDistance[0][0] +", and"
                    +(twoMinDistance[1][1]<100 ? " " : "")+(twoMinDistance[1][2]<100 ? " " : "")+" ("
                    +twoMinDistance[1][1]+","+twoMinDistance[1][2]+") with an elevation of "+twoMinDistance[1][0]);
        //QUESTION 04
        String[] frequencyProcess = frequencyProcess(arrNumbers,arrPeaks).split(" ");
        System.out.println("Question 04:\n\tThe most common value is "
                            +Integer.parseInt(frequencyProcess[0])+
                            " and it occurs "+Integer.parseInt(frequencyProcess[1])+" times.");
        long elapsedEfficiency = System.nanoTime() - startTimeEfficiency;
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("\n**) Efficiency ("+elapsedEfficiency/1000000+"ms ("+(elapsedEfficiency/1000000 < 1000 ? "<":">")+"1s)" +
                (elapsedEfficiency/1000000<1000 ? " => completes in less than 1 second – start timing after data has been read from file":"")
                +")\n**) The program completes in "+elapsedTime/1000000+"ms ("+(elapsedTime/1000000 < 2000 ? "<":">")+"2s)");
    }
}
