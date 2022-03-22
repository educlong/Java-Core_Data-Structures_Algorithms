package a04; /**
 * COMP10205 - Lab#4 Starting Code
 */
/**
 * @date March 07, 2022
 * @author DUC LONG NGUYEN (Paul)
 *
 * DISCUSSION
 * 	1.The significant performance difference between the SortedLinkedList<T> and the ArrayList<T> classes when adding items:
 * 		1.1. The best performance: SortedLinkedList<T>
 * 		1.2. The difference: (performance[ArrayList<T>] - performance[SortedLinkedList<T>]) (String):         ~2000ms
 * 		1.3. The difference: (performance[ArrayList<T>] - performance[SortedLinkedList<T>]) (class BabyName): ~2500ms
 * 	2. The significant performance difference between these two collections when removing items:
 * 		2.1. The best performance: ArrayList<T>
 * 		2.2. The difference: (performance[SortedLinkedList<T>] - performance[ArrayList<T>]) (String):		  ~500ms
 * 		2.3. The difference: (performance[SortedLinkedList<T>] - performance[ArrayList<T>]) (class BabyName): ~800ms
 * 	3. Choose to use a SortedLinkedList over an ArrayList based on the results of this assignment
 * 		3.1 When add elements into a list:		Using SortedLinkedList<T>
 * 		3.2 When remove elements from a list:	Using ArrayList<T>
 * */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
/**
 * The BabyName class stores an element of the name and its position in the txt file
 * This is the class header.
 */
class BabyName implements Comparable {
  String name;  /**The name of the element from txt file*/
  int index;    /**The index of the element from txt file*/
  /**
   * Constructor.
   * @param name The name of the element
   * @param index The index of the element
   */
  public BabyName(String name, int index){
    this.index = index;
    this.name = name;
  }
  /**
   * The compareTo method compares 2 BabyName object based on their name.
   * @param o The BabyName object are compared.
   * @return The integer number if this object's name is greater than o object's name
   */
  @Override
  public int compareTo(Object o) {
    return this.name.compareToIgnoreCase(((BabyName)o).name);
  }
  /**
   * The toString method computes the string representation of the list.
   * @return The string form of the list.
   */
  @Override
  public String toString() {
    return "["+this.name+", "+this.index+"]";
  }
}
/**
 * Main Class
 * This is the class header.
 * */
public class Assignment4
{
  /**
   * The time method stores timing results
   * @param numberName The number of name from the txt file
   * @param linkedList_String a Sorted Linked List (data type: String) stores a list of name from the txt file
   * @param linkedList_BabyName a Sorted Linked List (data type: BabyName) stores a list of name from the txt file
   * @param arrayList_String an Array List (data type: String) stores a list of name from the txt file
   * @param arrayList_BabyName an Array List (data type: BabyName) stores a list of name from the txt file
   * @param timeRequest The param represents if this method calculates for timing results for adding items
   * @return an array stores the timing results of required methods above
   * */
  private static int[] time(int numberName, String[] names, SortedLinkedList<String> linkedList_String,
                            SortedLinkedList<BabyName> linkedList_BabyName, ArrayList<String> arrayList_String,
                            ArrayList<BabyName> arrayList_BabyName, boolean timeRequest){
    return timeRequest ? (new int[]{  //timing results for adding items using the provided ArrayList and LinkedList classes
            (int)timeRequest(numberName, names, linkedList_String, linkedList_BabyName, arrayList_String, arrayList_BabyName, true, true),
            (int)timeRequest(numberName, names, linkedList_String, linkedList_BabyName, arrayList_String, arrayList_BabyName, true, false),
            (int)timeRequest(numberName, names, linkedList_String, linkedList_BabyName, arrayList_String, arrayList_BabyName, false, true),
            (int)timeRequest(numberName, names, linkedList_String, linkedList_BabyName, arrayList_String, arrayList_BabyName, false, false),
    }) : (new int[]{  //timing results for removing items using the provided ArrayList and LinkedList classes
            (int)timeRemove(numberName, linkedList_String, linkedList_BabyName, arrayList_String, arrayList_BabyName, true, true),
            (int)timeRemove(numberName, linkedList_String, linkedList_BabyName, arrayList_String, arrayList_BabyName, true, false),
            (int)timeRemove(numberName, linkedList_String, linkedList_BabyName, arrayList_String, arrayList_BabyName, false, true),
            (int)timeRemove(numberName, linkedList_String, linkedList_BabyName, arrayList_String, arrayList_BabyName, false,false),
    });
  }
  /**
   * The bestPerformance method calculates the best performance of required methods!
   * @param time an array stores the timing results of required methods above
   * @return a boolean value if the performance of SortedLinkedList methods are better than the performance of ArrayList methods
   * */
  private static boolean bestPerformance(int[] time){
    return time[0]<time[2] && time[1]<time[3];
  }
  /**
   * The time method calculates timing results for adding items using the required collections
   * @param numberName The number of name from the txt file
   * @param names an array of names from the txt file.
   * @param linkedList_String a Sorted Linked List (data type: String) stores a list of name from the txt file
   * @param linkedList_BabyName a Sorted Linked List (data type: BabyName) stores a list of name from the txt file
   * @param arrayList_String an Array List (data type: String) stores a list of name from the txt file
   * @param arrayList_BabyName an Array List (data type: BabyName) stores a list of name from the txt file
   * @param whichCollection The param represents if this method calculates for SortedLinkedList collection
   * @param dataType The param represents if this method calculates for String data type
   * @return a long number stores the timing result for adding items using the required collections
   * */
  private static long timeRequest(int numberName, String[] names, SortedLinkedList<String> linkedList_String,
                                  SortedLinkedList<BabyName> linkedList_BabyName, ArrayList<String> arrayList_String,
                                  ArrayList<BabyName> arrayList_BabyName, boolean whichCollection, boolean dataType){
    long start = System.nanoTime();
    if (whichCollection){
      if (dataType)  //collection: SortedLinkedList, datatype: String
        for (int i=0; i<numberName;i++)
          linkedList_String.add(names[i]);
      else   //collection: SortedLinkedList, datatype: BabyName
        for (int i=0; i<numberName;i++)
          linkedList_BabyName.add(new BabyName(names[i], i));
    }
    else {
      if (dataType)  //collection: ArrayList, datatype: String
        for (int i=0; i<numberName;i++){
          arrayList_String.add(names[i]);
          Collections.sort(arrayList_String);
        }
      else   //collection: ArrayList, datatype: BabyName
        for (int i=0; i<numberName;i++){
          arrayList_BabyName.add(new BabyName(names[i], i));
          Collections.sort(arrayList_BabyName, (b1, b2) -> b1.name.compareToIgnoreCase(b2.name));
        }
    }
    return (System.nanoTime() - start) / 1000000;
  }
  /**
   * The time method calculates timing results for removing items using the required collections
   * @param numberName The number of name from the txt file
   * @param linkedList_String a Sorted Linked List (data type: String) stores a list of name from the txt file
   * @param linkedList_BabyName a Sorted Linked List (data type: BabyName) stores a list of name from the txt file
   * @param arrayList_String an Array List (data type: String) stores a list of name from the txt file
   * @param arrayList_BabyName an Array List (data type: BabyName) stores a list of name from the txt file
   * @param whichCollection The param represents if this method calculates for SortedLinkedList collection
   * @param dataType The param represents if this method calculates for String data type
   * @return a long number stores the timing result for removing items using the required collections
   * */
  private static long timeRemove(int numberName, SortedLinkedList<String> linkedList_String,
                                 SortedLinkedList<BabyName> linkedList_BabyName, ArrayList<String> arrayList_String,
                                 ArrayList<BabyName> arrayList_BabyName, boolean whichCollection, boolean dataType){
    long start = System.nanoTime();
    if (whichCollection){
      if (dataType) //collection: SortedLinkedList, datatype: String
        for (int count = linkedList_String.size() - 1, i=0; i<numberName/2; i++, count--)
          linkedList_String.removeMid((int)(Math.random() * count));
      else  //collection: SortedLinkedList, datatype: BabyName
        for (int count = linkedList_BabyName.size() - 1, i=0; i<numberName/2; i++, count--)
          linkedList_BabyName.removeMid((int)(Math.random() * count));
    }
    else {
      if (dataType) //collection: ArrayList, datatype: String
        for (int count = arrayList_String.size() - 1, i=0; i<numberName/2; i++, count--)
          arrayList_String.remove((int)(Math.random() * count));
      else  //collection: ArrayList, datatype: BabyName
        for (int count = arrayList_BabyName.size() - 1, i=0; i<numberName/2; i++, count--)
          arrayList_BabyName.remove((int)(Math.random() * count));
    }
    return  (System.nanoTime() - start) / 1000000;
  }
  /**
   * Main method
   * */
  public static void main(String[] args)
  {
    final int NUMBER_OF_NAMES = 18756;
    final String filename = "src/a04/bnames.txt";
    final String[] names = new String[NUMBER_OF_NAMES];
    
     // May be useful for selecting random words to remove
    Random random = new Random(); 
    
    // Read in all of the names 
    try {
      Scanner fin = new Scanner(new File(filename));
      for (int i=0; i<NUMBER_OF_NAMES; i++)
          names[i] = fin.next();
      fin.close();
    } catch (FileNotFoundException e) {
      System.out.println("Exception caught: " + e.getMessage());
      System.exit(-1);
    }
    SortedLinkedList<String> linkedList_String = new SortedLinkedList<>();
    SortedLinkedList<BabyName> linkedList_BabyName = new SortedLinkedList<BabyName>();
    ArrayList<String> arrayList_String = new ArrayList<>();
    ArrayList<BabyName> arrayList_BabyName = new ArrayList<>();

    // Use the system to build the linked List
    int[] timeRequest=time(NUMBER_OF_NAMES,names,linkedList_String,linkedList_BabyName,arrayList_String,arrayList_BabyName,true);
    /**Test add method:*/
    /**System.out.println(linkedList_String+" Size: "+linkedList_String.size());
    System.out.println(linkedList_BabyName+" Size: "+linkedList_BabyName.size());*/
    String nameRemove = "Paula";
    boolean removeAnElement = linkedList_String.remove(nameRemove);
    int[] timeRemove=time(NUMBER_OF_NAMES,names,linkedList_String,linkedList_BabyName,arrayList_String,arrayList_BabyName,false);
    String _bestPerformanceAdd = bestPerformance(timeRequest) ? "SortedLinkedList<T>" : "ArrayList<T>";
    String _lessPerformanceAdd = bestPerformance(timeRequest) ? "ArrayList<T>" : "SortedLinkedList<T>";
    String _bestPerformanceRemove = bestPerformance(timeRemove) ? "SortedLinkedList<T>" : "ArrayList<T>";
    String _lessPerformanceRemove = bestPerformance(timeRemove) ? "ArrayList<T>" : "SortedLinkedList<T>";
    /**Test remove method:*/
    /**System.out.printf("Remove an element [%s] %s\n",nameRemove, (removeAnElement ? " success." : "fail."));
    System.out.println(linkedList_String+" Size: "+linkedList_String.size());
    System.out.println(linkedList_BabyName+" Size: "+linkedList_BabyName.size());*/

    // 1. Create the linkedList and add all items in Array
    System.out.printf("1. The time required to add %d elements to a Linked List (String)= \t%dms\n", names.length, timeRequest[0]);

    // 2. Remove half the items and time the code.
    System.out.printf("2. Remove an element and remove a half the items to a Linked List (String type).\n\t" +
            "2.1. Remove an element [%s] %s\n\t2.2. Remove half the items to a Linked List, size left: %d elements\n\t" +
                    "2.3. The time required to remove half the items to a Linked List = \t%dms\n"
            , nameRemove, (removeAnElement ? " success." : "fail."), names.length/2, timeRemove[0]);
    // 3. Create a SortedLinkedList of another data type and demonstrate
    System.out.printf("3. Create a SortedLinkedList of another data type (BabyName data type) and demonstrate\n\t" +
            "3.1. The time required to add %d elements to a Linked List = \t%dms\n" +
                    "\t3.2. Remove half the items to a Linked List, size left: %d elements\n\t" +
                    "3.3. The time required to remove half the items to a Linked List = \t%dms\n",
              names.length, timeRequest[1], names.length/2, timeRemove[1]);

    // 4. Build a standard ArrayList of String - Remember to sort list after each element is added
    //    Time this code.
    //    Use this timing data to compare add against SortedLinkedList in discussion
    //    Remove the half the elements and time again.  
    //    Use this timing data to compare remove against SortedLinkedList in discussion
    System.out.printf("4. Hands-on for ArrayList:\n\t" +
                    "4.1 The time required to add %d elements (dataType: String)= \t%dms\n\t" +
                    "4.2 The time required to add %d elements (dataType: BabyName)= \t%dms\n\t" +
                    "4.3 The time required to remove %d elements (dataType: String)= \t%dms\n\t" +
                    "4.4 The time required to remove %d elements (dataType: BabyName)= %dms\n",
            names.length, timeRequest[2], names.length, timeRequest[3],
            names.length/2, timeRemove[2], names.length/2, timeRemove[3]);
    System.out.printf("CONCLUSION\n\t\t_________Method_____________________Timing_____Elements_______\n\t\t" +
            "Add \tSortedLinkedList<String>\t%dms\t\t%d\n\t\tAdd \tSortedLinkedList<BabyName>\t%dms\t\t%d\n\t\t" +
            "Remove\tSortedLinkedList<String>\t%dms\t\t%d\n\t\tRemove\tSortedLinkedList<BabyName>\t%dms\t\t%d\n\t\t"+
            "Add \tArrayList<String>\t\t\t%dms\t\t%d\n\t\tAdd \tArrayList<BabyName>\t\t\t%dms\t\t%d\n\t\t" +
            "Remove\tArrayList<String>\t\t\t%dms\t\t\t%d\n\t\tRemove\tArrayList<BabyName>\t\t\t%dms\t\t\t%d\n",
            timeRequest[0],names.length,timeRequest[1],names.length,timeRemove[0],names.length/2,timeRemove[1],names.length/2,
            timeRequest[2],names.length,timeRequest[3],names.length,timeRemove[2],names.length/2,timeRemove[3],names.length/2);
    // DISCUSSION
    System.out.printf("5.DISCUSSION\n\t5.1.The significant performance difference between the " +
                    "\n\t\tSortedLinkedList<T> and the ArrayList<T> classes when adding items: \n\t\t*) The best performance: %s" +
                    "\n\t\t*) (performance[%s] - performance[%s]) (String):\t\t\t%dms" +
                    "\n\t\t*) (performance[%s] - performance[%s]) (class BabyName): %dms\n\t" +
                    "5.2. The significant performance difference between these two collections when removing items:\n\t\t" +
                    "*) The best performance: %s\n\t\t*) (performance[%s] - performance[%s]) (String):\t\t\t%dms" +
                    "\n\t\t*) (performance[%s] - performance[%s]) (class BabyName): %dms\n\t" +
                    "5.3. Choose to use a SortedLinkedList over an ArrayList based on the results of this assignment\n\t\t" +
                    "*) When add elements into a list:\t\tUsing %s\n\t\t*) When remove elements from a list:\tUsing %s",
            _bestPerformanceAdd, _lessPerformanceAdd, _bestPerformanceAdd, Math.abs(timeRequest[0]-timeRequest[2]),
            _lessPerformanceAdd, _bestPerformanceAdd, Math.abs(timeRequest[1]-timeRequest[3]), _bestPerformanceRemove,
            _lessPerformanceRemove, _bestPerformanceRemove, Math.abs(timeRemove[0]-timeRemove[2]), _lessPerformanceRemove,
            _bestPerformanceRemove, Math.abs(timeRemove[1]-timeRemove[3]), _bestPerformanceAdd, _bestPerformanceRemove);
  }
}
