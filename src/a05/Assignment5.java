/**
 * COMP10205 - Lab#5 Starting Code: Queue
 */
/**
 * @date March 27, 2022
 * @author DUC LONG NGUYEN (Paul)
 */
import java.io.File;
import java.util.*;
/**
 * The Customer class stores an element of the items (in carts) and an option to know
 *  if this customer should check out in express line or not
 * This is the class header.
 */
class Customer{
  private int items;  /**The number of items in customer's cart when he/she check out*/
  /**
   * Constructor.
   * @param items The number of items in customer's cart when he/she check out
   */
  public Customer(int items){
    this.items = items;
  }
  /**
   * The isOptionalExpressLine method shows that
   *    whether current customer should check in express line or in normal line
   * @return a boolean value to know whether current customer should check in express line or in normal line
   *    The true value means current customer should check in express line. Otherwise, in normal line
   */
  public boolean isOptionalExpressLine (int maxItems){
    return items <= maxItems;
  }
  /**
   * The timeToServeCustomer method calculate the time (in seconds) that it takes to serve a customer
   * @return The integer number to know the time (in seconds) that it takes to serve a customer
   */
  public int timeToServeCustomer(){
    return 45 + 5*this.items;
  }
  /**
   * The toString method computes the string representation of this Customer model
   * @return The string form of the list.
   */
  @Override
  public String toString() {
    return "["+items+" ("+timeToServeCustomer()+"s)]";
  }
}
/**
 * Main Class
 * This is the class header.
 * */
public class Assignment5
{
  private static int NUM_EXPRESS_LINE = 0;
  private static int NUM_NORMAL_LINE = 0;
  private static int MAX_ITEMS = 0;
  private static final int TIME_STEP = 30;
  /**
   * The method to read and store all the number of items for each customer
   *    into an ArrayList from a CustomerData.txt file.
   * @param fileName The name of file
   * @return an ArrayList stored all the number of items for each customer from a CustomerData.txt file.
   */
  private static  ArrayList<Customer> readFile(String fileName){
    ArrayList<Customer> listNumber = new ArrayList<>();
    try {
      Scanner fin = new Scanner(new File(fileName));
      int count = 0;
      while (fin.hasNext()) {
        String fileWord = fin.next();
        if (fileWord.length() > 0) {
          if (count == 0) NUM_EXPRESS_LINE = Integer.parseInt(fileWord);
          else if (count == 1) NUM_NORMAL_LINE = Integer.parseInt(fileWord);
          else if (count == 2) MAX_ITEMS = Integer.parseInt(fileWord);
          else if (count == 3) {}
          else listNumber.add(new Customer(Integer.parseInt(fileWord)));
        }
        count++;
      }
      fin.close();
    }
    catch (Exception ex){
      System.out.println("Exception caught: " + ex.getMessage());
    }
    return listNumber;
  }
  /** for PART A
   * The getPositionAndTimeLine method find the line that has the longest and/or shortest service time,
   *    as well as finding the shortest and longest service time (in seconds) from all the lines
   * @param timeLines An array that stored the total service time for all customers in each line.
   * @param loopStart The start value for loop
   * @param numberOfLines The number of express lines or express lines or total of lines
   * @param isPosition The boolean value indicates if the getPositionAndTimeLine method return a position of line
   * @param isShortest The boolean value indicates if the getPositionAndTimeLine method return the shortest service time
   * @return the line that has the longest and/or shortest service time,
   *    and/or the shortest and longest service time (in seconds) from all the lines
   * */
  private static int getPositionAndTimeLine(int[] timeLines, int loopStart, int numberOfLines,
                                            boolean isPosition, boolean isShortest){
    int shortestTimeLine = timeLines[loopStart];          //the shortest service time
    int longestTimeLine = timeLines[loopStart];           //the shortest service time
    int longestLineTimePosition = loopStart;              //the position of line that has the shortest service time
    int shortestLineTimePosition = loopStart;             //the position of line that has the shortest service time
    for (int count = loopStart; count<numberOfLines; count++){
      if (timeLines[count] < shortestTimeLine){
        shortestTimeLine = timeLines[count];
        shortestLineTimePosition = count;
      }
      if (timeLines[count] > longestTimeLine) {
        longestTimeLine = timeLines[count];
        longestLineTimePosition = count;
      }
    }
    return isPosition ? (isShortest ? shortestLineTimePosition : longestLineTimePosition)
                      : (isShortest ? shortestTimeLine : longestTimeLine);
  }
  /** for PART A
   * The isNotExpressLine checks that the customer do not need to enter an express line
   *    if another check-out line would result in a shorter service time.
   * This means that if a customer has less than x items and long service time is too long
   *    => This customer is required to move to express checkoutLine
   * @param timeLines An array that stored the total service time for all customers in each line.
   * @param numberOfLines The number of express lines or express lines or total of lines
   * @param longestTimeLine The longest service time from all lines
   * @return The boolean value indicates whether the service time in current line is shorter than express line or not.
   *    isNotExpressLine=true: service time in current line is shorter than express line. Otherwise, =false
   * */
  private static boolean isNotExpressLine(int[] timeLines, int numberOfLines, int longestTimeLine){
    boolean isNotExpressLine = false;
    for (int countLineExpress = 0; countLineExpress<numberOfLines; countLineExpress++)
      if(longestTimeLine == timeLines[countLineExpress]) isNotExpressLine = true;
    return isNotExpressLine;
  }
  /** for PART A
   * The position method indicates the line customer should check out.
   *    This means that position method indicates LinkedQueue that the customer should add to
   * @param timeLines An array that stored the total service time for all customers in each line.
   * @param customer The customer add to LinkedQueue (based on the position of lines)
   * @param isNotExpressLine indicates whether the service time in current line is shorter than express line or not.
   * @param shortestExpressLinePosition     The position of line has the shortest service time from all express lines.
   * @param shortestTimeLineNormalPosition  The position of line has the shortest service time from all normal lines.
   * @param shortestTimeLinePosition        The position of line has the shortest service time from all the lines
   * @return the line customer should check out.
   * */
  private static int position(int[] timeLines, Customer customer, boolean isNotExpressLine,
                              int shortestExpressLinePosition, int shortestTimeLineNormalPosition, int shortestTimeLinePosition){
    int position = (customer.isOptionalExpressLine(MAX_ITEMS) && !isNotExpressLine) ? shortestExpressLinePosition :
            (!customer.isOptionalExpressLine(MAX_ITEMS) ? shortestTimeLineNormalPosition : shortestTimeLinePosition);
    timeLines[position] += customer.timeToServeCustomer();
    return position;
  }
  /** for PART B
   * The timeInSteps method stores the time after every 30 or 60 second. Eg: [0, 60, 120, 180, 240, 300, 330]
   * @param timeLines A two-dimensional array stored service time of each Customer in each line.
   * @return an array that stores the time after every 30 or 60 second. Eg output: [0, 60, 120, 180, 240, 300, 330]
   * */
  private static int[] timeInSteps(int[][] timeLines){
    /** timeTotal[]: figure out the total time of each line. Eg: [290, 330, 270] */
    int[] timeTotal = new int[timeLines.length];
    for(int count = 0; count<timeLines.length; count++)
      for (int time : timeLines[count]) timeTotal[count]+=time;
    int timeMax = timeTotal[0];
    for (int time : timeTotal)
      timeMax = Math.max(timeMax, time);
    int timeSteps = timeMax/TIME_STEP + (timeMax%TIME_STEP > 0 ? 1 : 0);
    int[] timeInSteps = new int[timeSteps+1];
    timeInSteps[0] = 0;
    for (int count = 1; count<timeInSteps.length; count++)
      if (count==timeInSteps.length-1 && timeMax%TIME_STEP > 0)
        timeInSteps[count] = timeMax%TIME_STEP + timeInSteps[count-1];
      else timeInSteps[count] = timeInSteps[count-1] + TIME_STEP;
    return timeInSteps;
  }
  /** for PART B
   * The arrTimeTotal method stores the total time after each customer who checked out.
   * @param timeLines A two-dimensional array stored service time of each Customer in each line.
   * @return A two-dimensional array to store the total time after each customer who checked out.
   *      Eg: [ [75, 170, 225, 290] , [80, 180, 330] , [270] ]
   * */
  private static int[][] arrTimeTotal(int[][] timeLines){
    int[][] arrTimeTotal = new int[timeLines.length][];
    for (int count = 0; count < timeLines.length; count++) {
      arrTimeTotal[count] = new int[timeLines[count].length];
      arrTimeTotal[count][0] = timeLines[count][0];
      for (int countTime = 1; countTime<timeLines[count].length; countTime++)
        arrTimeTotal[count][countTime] = arrTimeTotal[count][countTime-1] + timeLines[count][countTime];
    }
    return arrTimeTotal;
  }
  /** for PART B
   * The stateCheckoutLine method stores the state of the Checkout Line after each 30 or 60 second.
   * @param arrTimeTotal stores the total time after each customer who checked out.
   *                     Eg: [ [75, 170, 225, 290] , [80, 180, 330] , [270] ]
   * @param timeInSteps stores the time after every 30 or 60 second. Eg: [0, 60, 120, 180, 240, 300, 330]
   * @return a two-dimensional array that stores the state of the Checkout Line after each 30 or 60 second.
   *    Eg: [ [0, 4, 3, 2, 1, 0, 0]
   *    Error--^
   *          [0, 3, 2, 0, 0, 1, 0]
   *    Error--^--------^--^
   *          [0, 0, 0, 0, 1, 0, 0] ]
   *    Error--^
   * However, there are some positions that are incorrect
   *    (this problem is processed in arrZeros and arrBreakPoint method)
   * */
  private static int[][] stateCheckoutLine(int[][] arrTimeTotal, int[] timeInSteps){
    int[][] stateCheckoutLine = new int[arrTimeTotal.length][timeInSteps.length];
    for (int count = 0; count<stateCheckoutLine.length; count++)
      stateCheckoutLine[count] = new int[timeInSteps.length];
    for (int count = 0; count<arrTimeTotal.length; count++)
      for(int countTime = 0; countTime<arrTimeTotal[count].length; countTime++)  //each times in a line
        for (int step = 1; step<timeInSteps.length; step++){
          int currentTime = arrTimeTotal[count][countTime];
          if (currentTime>timeInSteps[step-1] && currentTime<=timeInSteps[step])
            stateCheckoutLine[count][step-1] = arrTimeTotal[count].length - countTime;
        }
    return stateCheckoutLine;
  }
  /** for PART B
   * The endCheckout method stores the last checkpoint position of each line
   * @param stateCheckoutLine stores the state of the Checkout Line after each 30 or 60 second.
   *                          Eg: [ [0, 4, 3, 2, 1, 0, 0] , [0, 3, 2, 0, 0, 1, 0] , [0, 0, 0, 0, 1, 0, 0] ]
   * @return an array that stores the last checkpoint position of each line
   *    Eg: [ [0, 4, 3, 2, 1, 0, 0]
   *                       ^----------- The last checkpoint position = 4
   *          [0, 3, 2, 0, 0, 1, 0]
   *                          ^-------- The last checkpoint position = 5
   *          [0, 0, 0, 0, 1, 0, 0] ]
   *                       ^----------- The last checkpoint position = 4
   *  ==> The result: [4,5,4]
   * */
  private static int[] endCheckout(int[][] stateCheckoutLine){
    int endCheckout[] = new int[stateCheckoutLine.length];
    for (int count = 0; count<stateCheckoutLine.length; count++)
      for (int state = stateCheckoutLine[count].length-1; state>0; state--) {
        if (stateCheckoutLine[count][state] != 0) endCheckout[count] = state;
        else endCheckout[count] = 0;
        if(endCheckout[count] != 0) break;
      }
    return endCheckout;
  }
  /** for PART B
   * The arrZeros method stores the positions that have zero value from stateCheckoutLine method
   * @param stateCheckoutLine stores the state of the Checkout Line after each 30 or 60 second.
   *                          Eg: [ [0, 4, 3, 2, 1, 0, 0] , [0, 3, 2, 0, 0, 1, 0] , [0, 0, 0, 0, 1, 0, 0] ]
   * @param endCheckout stores the last checkpoint position of each line
   * @return an array stores the positions that have zero value from stateCheckoutLine method
   * Explain arrZeros method:
   *    Eg: [ [0, 4, 3, 2, 1, 0, 0]
   *          [0, 3, 2, 0, 0, 1, 0]
   *                    ^--^------------ The position has zero value: 3, 4
   *          [0, 0, 0, 0, 1, 0, 0] ]
   *              ^--^--^--------------- The position has zero value: 1, 2, 3
   *  ==> The result: [ [], [3,4] , [1,2,3] ]
   * */
  private static ArrayList<Integer>[] arrZeros(int[][] stateCheckoutLine, int[]endCheckout){
    ArrayList<Integer>[] arrZeros = new ArrayList[stateCheckoutLine.length];
    for (int count = 0; count< arrZeros.length; count++)
      arrZeros[count] = new ArrayList<>();
    for (int count = 0; count<stateCheckoutLine.length; count++)
      for (int countState = 1; countState<endCheckout[count]; countState++)
        if (stateCheckoutLine[count][countState]==0)
          arrZeros[count].add(countState);
    return arrZeros;
  }
  /** for PART B
   * The arrBreakPoint method stores the positions
   *    that have positive value which is located between 2 zero value from stateCheckoutLine method
   * @param stateCheckoutLine stores the state of the Checkout Line after each 30 or 60 second.
   *                          Eg: [ [0, 4, 3, 2, 1, 0, 0] , [0, 3, 2, 0, 0, 1, 0] , [0, 0, 0, 0, 1, 0, 0] ]
   * @param endCheckout stores the last checkpoint position of each line
   * @param arrZeros stores the positions that have zero value from stateCheckoutLine method
   * @return an array stores the positions
   *    that have positive value which is located between 2 zero value from stateCheckoutLine method
   * Explain arrBreakPoint method:
   *    Eg: [ [0, 4, 3, 0, 2, 0, 0, 1, 0]
   *                       ^--------^-------- Position have positive value which is located between 2 zero value: 4, 7
   *          [0, 3, 0, 0, 2, 0, 1, 0, 0]
   *                       ^-----^----------- Position have positive value which is located between 2 zero value: 4, 6
   *  ==> The result: [ [4,7] , [4,6] ]
   * */
  private static ArrayList<Integer>[] arrBreakPoint(int[][] stateCheckoutLine, ArrayList<Integer>[] arrZeros, int[] endCheckout){
    ArrayList<Integer>[] arrBreakPoint = new ArrayList[stateCheckoutLine.length];
    for (int count = 0; count< arrBreakPoint.length; count++)
      arrBreakPoint[count] = new ArrayList<>();
    for (int count = 0; count<arrZeros.length; count++)
      for (int countZero = arrZeros[count].size()-1; countZero > 0; countZero--)
        if (arrZeros[count].get(countZero)-1 != arrZeros[count].get(countZero-1))
          arrBreakPoint[count].add(arrZeros[count].get(countZero)-1);
    for (int count = 0; count< arrZeros.length; count++)
      if (arrZeros[count].size()>0)
        arrBreakPoint[count].add(endCheckout[count]);
    for (int count = 0; count< arrBreakPoint.length; count++)
      Collections.sort(arrBreakPoint[count]);
    return arrBreakPoint;
  }
  /** for PART B
   * The partBResult method stores a result including
   *    -  the time after every 30 or 60 second. Eg: [0, 60, 120, 180, 240, 300, 330]
   *    -  and state of the Checkout Line after each 30 or 60 second (fix bugs-errors from stateCheckoutLine method)
   * @param stateCheckoutLine stores the state of the Checkout Line after each 30 or 60 second.
   *                          Eg: [ [0, 4, 3, 2, 1, 0, 0] , [0, 3, 2, 0, 0, 1, 0] , [0, 0, 0, 0, 1, 0, 0] ]
   * @param timeInSteps stores the time after every 30 or 60 second. Eg: [0, 60, 120, 180, 240, 300, 330]
   * @param arrZeros stores the positions that have zero value from stateCheckoutLine method
   * @param arrBreakPoint stores the positions
   *                      that have positive value which is located between 2 zero value from stateCheckoutLine method
   * @return a two-dimensional array stores a result including
   *      the time after every 30 or 60 second and the Checkout Line after each 30 or 60 second
   *    Eg: [ [60, 4, 3, 1]
   *          [120, 3, 2, 1]
   *          [180, 2, 1, 1]
   *              ...        ]
   *  with the first element in each array is the time after every 30 or 60 second
   * */
  private static int[][] partBResult(int[][] stateCheckoutLine, int[] timeInSteps,
                                     ArrayList<Integer>[] arrZeros, ArrayList<Integer>[] arrBreakPoint){
    for (int count = 0; count<stateCheckoutLine.length; count++)
      if (arrZeros[count].size() > 0 && arrBreakPoint[count].size() > 0){   //only fix if length(arrZeros && arrBreakPoint)>0
        /*the first case: fix if the length of arrBreakPoint == 1*/
        if (arrBreakPoint[count].size()<=1){
          for (int countZero = 0; countZero < arrZeros[count].size(); countZero++)
            if (arrZeros[count].get(countZero) < arrBreakPoint[count].get(0))
              stateCheckoutLine[count][arrZeros[count].get(countZero)] = stateCheckoutLine[count][arrBreakPoint[count].get(0)];
        }
        /*the default case: fix if the length of arrBreakPoint > 1*/
        else {
          for (int countZero = 0; countZero < arrZeros[count].size(); countZero++)        //handling for the first arrBreakPoint
            if (arrZeros[count].get(countZero) < arrBreakPoint[count].get(0))
              stateCheckoutLine[count][arrZeros[count].get(countZero)] = stateCheckoutLine[count][arrBreakPoint[count].get(0)];
          for (int countBreak = 1; countBreak < arrBreakPoint[count].size(); countBreak++)//handling for other arrBreakPoint
            for (int countZero = 0; countZero < arrZeros[count].size(); countZero++)
              if (arrZeros[count].get(countZero) < arrBreakPoint[count].get(countBreak)
                      && arrZeros[count].get(countZero) > arrBreakPoint[count].get(countBreak - 1))
                stateCheckoutLine[count][arrZeros[count].get(countZero)] = stateCheckoutLine[count][arrBreakPoint[count].get(countBreak)];
        }
      }
    int[][] partBResult = new int[timeInSteps.length-1][];
    for (int count = 0; count<partBResult.length; count++)
      partBResult[count] = new int[stateCheckoutLine.length+1];
    for (int countRow = 0; countRow<partBResult.length; countRow++){
      partBResult[countRow][0] = timeInSteps[countRow+1];
      for (int countColumn = 0; countColumn<partBResult[countRow].length; countColumn++)
        if (countColumn!=0)
          partBResult[countRow][countColumn] = stateCheckoutLine[countColumn-1][countRow+1];
    }
    return partBResult;
  }
  /** for PART A ==> Result
   * The enqueuePartA method stores an array (type: LinkedQueue<Customer>) of all Customers in all lines. (stores in QUEUE)
   * @param customers an arraylist stores all customers read from txt file
   * @return an array (type: LinkedQueue<Customer>) of all Customers in all of lines
   * */
  public static LinkedQueue<Customer> [] enqueuePartA(ArrayList<Customer> customers){
    LinkedQueue[] customerQueues = new LinkedQueue[NUM_NORMAL_LINE+NUM_EXPRESS_LINE];
    for (int countCustomer = 0; countCustomer<customerQueues.length; countCustomer++)
      customerQueues[countCustomer] = new LinkedQueue();
    int [] timeLines = new int[NUM_NORMAL_LINE+NUM_EXPRESS_LINE];
    for (Customer customer : customers) {   //get each customer
      int longestTimeLine = getPositionAndTimeLine(timeLines, 0, customerQueues.length, false,false);
      int shortestTimeLinePosition = getPositionAndTimeLine(timeLines, 0, customerQueues.length, true,true);
      int shortestExpressLinePosition = getPositionAndTimeLine(timeLines,0, NUM_EXPRESS_LINE,true, true);
      int shortestTimeLineNormalPosition = getPositionAndTimeLine(timeLines, NUM_EXPRESS_LINE, customerQueues.length,true,true);
      boolean isNotExpressLine = isNotExpressLine(timeLines, NUM_EXPRESS_LINE, longestTimeLine);
      int positionToAddQueue = position(timeLines, customer, isNotExpressLine,
              shortestExpressLinePosition, shortestTimeLineNormalPosition, shortestTimeLinePosition);
      customerQueues[positionToAddQueue].enqueue(customer);
    }
    return customerQueues;
  }
  /** for PART B ==> Result
   * The dequeuePartB method stores
   *    a result of the time after every 30 or 60 second and the Checkout Line after each 30 or 60 second
   *    or the total time after each customer who checked out.
   * @param customerQueues an array (type: LinkedQueue<Customer>) of all Customers in all lines. (stores in QUEUE)
   * @param isPartB The boolean value indicates whether the dequeuePartB method return the result of part B or not
   * @return a two-dimensional array stores
   *    a result of the time after every 30 or 60 second and the Checkout Line after each 30 or 60 second
   *    or the total time after each customer who checked out.
   * */
  public static int[][] dequeuePartB(LinkedQueue<Customer>[] customerQueues, boolean isPartB) {
    int[][] timeLines = new int[customerQueues.length][];
    for (int countQueue = 0; countQueue < customerQueues.length; countQueue++) {
      timeLines[countQueue] = new int[customerQueues[countQueue].size()];
      int countCustomer = 0;
      while (!customerQueues[countQueue].isEmpty()) {
        timeLines[countQueue][countCustomer] = customerQueues[countQueue].dequeue().timeToServeCustomer();
        countCustomer++;
      }
    }
    int[] timeInSteps = timeInSteps(timeLines);
    int[][] stateCheckoutLine = stateCheckoutLine(arrTimeTotal(timeLines),timeInSteps);
    int endCheckout[] = endCheckout(stateCheckoutLine);
    ArrayList<Integer>[] arrZeros = arrZeros(stateCheckoutLine,endCheckout);
    int[][] partBResult = partBResult(stateCheckoutLine,timeInSteps,arrZeros,arrBreakPoint(stateCheckoutLine,arrZeros,endCheckout));
    return isPartB ? partBResult : timeLines;
  }

  /**
   * Main method
   * */
  public static void main(String[] args)
  {
    ArrayList<Customer> readFile = readFile("src/CustomerData.txt");
    LinkedQueue<Customer> [] customerQueues = enqueuePartA(readFile);
    int[][] timeLines = dequeuePartB(customerQueues, false);

    /** Enqueue*/
    customerQueues = enqueuePartA(readFile);

    /** PART A */
    String partA = "\n1. PART A - Checkout lines and time estimates for each line.\n\n";
    for (int countCustomer = 0; countCustomer < customerQueues.length; countCustomer++) {
      partA += "\tCheckOut(" + ((countCustomer < NUM_EXPRESS_LINE) ? "Express) #" : "Normal)  #");
      int timeLine = 0;
      for (int time : timeLines[countCustomer]) timeLine += time;
      partA += (countCustomer+1) + " (Estimate time = " + timeLine+ "):\n\t\t\t\t\tFirst -> " + customerQueues[countCustomer] + " <- Last\n";
    }

    /** Dequeue*/
    int[][] customerDequeues = dequeuePartB(customerQueues,true);

    /** PART B */
    String partB = "\n2. PART B - Number of customers in line after each minute ("+TIME_STEP+"s)\n\tt(s)\t\t";
    for (int count = 0; count<NUM_EXPRESS_LINE+NUM_NORMAL_LINE; count++)
      partB+=" Line #"+(count+1)+(count == NUM_EXPRESS_LINE+NUM_NORMAL_LINE-1 ? "\n\t________\t" : "\t\t");
    for (int count=0; count<NUM_EXPRESS_LINE; count++)  partB+="<Express> ____\t";
    for (int count=0; count<NUM_NORMAL_LINE; count++)   partB+=" <Normal>"+(count==NUM_NORMAL_LINE-1 ? "\n":" ____\t");
    for (int count = 0; count<customerDequeues.length; count++) {
      for (int countLine = 0; countLine < customerDequeues[count].length; countLine++)
        partB+="\t"+customerDequeues[count][countLine]+"\t\t\t";
      partB+="\n";
    }

    System.out.println(partA);
    System.out.println(partB);
  }
}
