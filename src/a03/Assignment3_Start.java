package a03;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
/**
 *  Program will count the words and occurrences of words in the complete Lord of the Rings Trilogy by J.R.R Tolkien,
 *      calculate how many words are not found in the provided dictionary,
 *      and calculate the most closely associated with the Ring using proximity searching.
 * This is the class header.
 *
 * @date Feb 20, 2022
 * @author DUC LONG NGUYEN (Paul)
 *
 * DISCUSSIONS:
 * 1. The percentage of total empty buckets ~ 2%
 * 2. The best performance  ->  Use the a03.SimpleHashSet dictionary and the contains method provided (~5ms)
 *                              Use the binarySearch method of the Collections class (Collections.binarySearch) (~20ms)
 *    The lowest performance->  Use the contains method of ArrayList to find matches (~2000ms)
 *
 *    The calculation of the perfomances are based on the time required to figure out the number of
 *      misspelled words in the file. Therefore, before calculate the number of misspelled word,
 *      we need to call System.nanoTime() and call this method after calculate.
 *      From that, we will have the performance of each method measured in milisecond.
 *      The best performance (Use the a03.SimpleHashSet dictionary and the contains method provided) is
 *          the method with the completely time is smallest
 */

public class Assignment3_Start {
    private static final int MOST_FREQUENT_WORDS = 10;  // The list of the 10 most frequent words and counts
    private static final int OCCUR_EXACTLY = 64;        // The list of words that occur exactly 64 times in the file.
    private static final String WORD_TO_CALC_PRO_DISTANCE = "ring"; // Measure the proximity distance of a character to the word “ring”
    private static final int PRO_DISTANCE_CUT_OFF =42;  // a proximity distance cut-off
    private static final String[] ACTORS = new String[] // Evaluate the character list to find The Lord of the "Ring"
        {"frodo", "sam", "bilbo", "gandalf", "boromir", "aragorn", "legolas", "gollum","pippin", "merry",
                "gimli", "sauron", "saruman", "faramir", "denethor", "treebeard", "elrond", "galadriel"};
    private static String DISCUSSIONS = "";
    /** PART A
     * The method to read and store all unique words into an ArrayList from a US.txt file.
     * @param fileDic The name of file
     * @return an ArrayList stored all the unique words from UX.txt file
     */
    private static ArrayList<BookWord> arrDic(String fileDic){
        ArrayList<BookWord> dic = new ArrayList<>();
        try {
            Scanner finDic = new Scanner(new File(fileDic));
            while (finDic.hasNext()) dic.add(new BookWord(finDic.next()));
            finDic.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Exception opening file : " + ex.getMessage());
        }
        Collections.sort(dic, (w1, w2) -> w1.getText().compareToIgnoreCase(w2.getText()));
        return dic;
    }
    /** PART A
     * The method to read and store all unique words into an a03.SimpleHashSet from a US.txt file.
     * @param fileDic The name of file
     * @return an a03.SimpleHashSet stored all the unique words from UX.txt file
     */
    private static SimpleHashSet<BookWord> simpleHashSetDic(String fileDic){
        SimpleHashSet<BookWord> dic = new SimpleHashSet<>();
        try {
            Scanner finDic = new Scanner(new File(fileDic));
            while (finDic.hasNext()) dic.insert(new BookWord(finDic.next()));
            finDic.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Exception opening file : " + ex.getMessage());
        }
        DISCUSSIONS+="\t1. The percentage of total empty buckets: "+ (new DecimalFormat("#.##")
                .format((double) dic.getNumberofEmptyBuckets()*100/dic.size()))+"%\n";
        return dic;
    }
    /** PART A
     * The method to read and store all unique words into an ArrayList from a TheLordOfTheRIngs.txt file.
     * @param fileName The name of file
     * @return a sorted ArrayList stored all the unique words from TheLordOfTheRIngs.txt file
     */
    private static ArrayList<BookWord> words(String fileName){
        SimpleHashSet<BookWord> words =new SimpleHashSet<>();
        try {
            Scanner fin = new Scanner(new File(fileName));
            fin.useDelimiter("\\s|\"|\\(|\\)|\\.|\\,|\\?|\\!|\\_|\\-|\\:|\\;|\\n");  // Filter - DO NOT CHANGE
            while (fin.hasNext()) {
                String fileWord = fin.next().toLowerCase();
                if (fileWord.length() > 0) {
                    BookWord word = new BookWord(fileWord);
                    words.insert(word);
                    if (words.contains(word)) {
                        ArrayList<BookWord> _words = words.buckets[words.getHash(word, words.getNumberofBuckets())];
                        _words.get(_words.indexOf(word)).incrementCount();  //count the total of this word in the file
                    }
                }
            }
            fin.close();
        }catch (Exception e){
            System.out.println("Exception caught: " + e.getMessage());
        }
        ArrayList<BookWord> arrayListWords = new ArrayList<>();
        for (ArrayList<BookWord> _words : words.buckets) arrayListWords.addAll(_words);
        Collections.sort(arrayListWords, (w1, w2) -> (w2.getCount() - w1.getCount())!=0
                    ? (w2.getCount() - w1.getCount()) :  w1.getText().compareToIgnoreCase(w2.getText()));
        return arrayListWords;
    }
    /** PART A
     * The method to calculate the most frequent words, and stores they into an array from an ArrayList of words.
     * @param words an ArrayList of words (from TheLordOfTheRIngs.txt file)
     * @return a sorted array stores the most frequent words
     */
    private static BookWord[] mostFrequentWords(ArrayList<BookWord> words){
        BookWord[] frequentWords = new BookWord[MOST_FREQUENT_WORDS];
        for (int i=0; i<MOST_FREQUENT_WORDS; i++) frequentWords[i]=words.get(i);
        return frequentWords;
    }
    /** PART A
     * The method to calculate and list the words that occur exactly [OCCUR_EXACTLY] times in the file, and store they into an ArrayList
     * @param words an ArrayList of words (from TheLordOfTheRIngs.txt file)
     * @return a sorted ArrayList stores the words that occur exactly [OCCUR_EXACTLY] times in the file (TheLordOfTheRIngs.txt file)
     */
    private static ArrayList<BookWord> occurExactly(ArrayList<BookWord> words){
        ArrayList<BookWord> occurExactly = new ArrayList<>();
        for (BookWord word : words)
            if(word.getCount()==OCCUR_EXACTLY) occurExactly.add(word);
        return occurExactly;
    }
    /** PART A
     * The method to calculate the number of misspelled words in the file (the words that are not contained in the dictionary)
     *      through the METHOD 01: Use the contains method of ArrayList to find matches.
     * @param words an ArrayList of words (from TheLordOfTheRIngs.txt file)
     * @param dic an ArrayList of words in dictionary (from UX.txt file)
     * @return an integer number that is the number of misspelled words in the file
     */
    private static int checkMisspelledM1(ArrayList<BookWord> words, ArrayList<BookWord> dic){
        int count = 0;
        for (BookWord _word : words) {
            boolean isContain = false;
            if (dic.contains(_word)) isContain=true;
            count = isContain ? count : count+1;
        }
        return count;
    }
    /** PART A
     * The method to calculate the number of misspelled words in the file (the words that are not contained in the dictionary)
     *      through the METHOD 02: Use the binarySearch method of the Collections class (Collections.binarySearch)
     *                  to search the ArrayList dictionary. Supply a Lambda expression for the binary search of the Dictionary.
     * @param words an ArrayList of words (from TheLordOfTheRIngs.txt file)
     * @param dic an ArrayList of words in dictionary (from UX.txt file)
     * @return an integer number that is the number of misspelled words in the file
     */
    private static int checkMisspelledM2(ArrayList<BookWord> words, ArrayList<BookWord> dic){
        int count = 0;
        for (BookWord word : words)
            if(Collections.binarySearch(dic, word, (o1, o2) -> o1.getText().compareToIgnoreCase(o2.getText()))<0) count++;
        return count;
    }
    /** PART A
     * The method to calculate the number of misspelled words in the file (the words that are not contained in the dictionary)
     *      through the METHOD 03: Use the a03.SimpleHashSet dictionary and the contains method provided
     * @param words an ArrayList of words (from TheLordOfTheRIngs.txt file)
     * @param dic a a03.SimpleHashSet of words in dictionary (from UX.txt file)
     * @return an integer number that is the number of misspelled words in the file
     */
    private static int checkMisspelledM3(ArrayList<BookWord> words, SimpleHashSet<BookWord> dic){
        int count = 0;
        for (BookWord word : words)
            if(dic.contains(word))
                count++;
        return words.size() - count;
    }
    /** PART A
     * The method to print the information of 3 methods above when calculation the number of misspelled words in the file
     *      including the name of methods, the number of misspelled words and the performance for each method.
     * @param words an ArrayList of words (from TheLordOfTheRIngs.txt file)
     * @param dic an ArrayList of words in dictionary (from UX.txt file)
     * @param simpleHashSetDic a a03.SimpleHashSet of words in dictionary (from UX.txt file)
     * @return a string that includes all the information of 3 methods (including the number of misspelled word and the performance)
     */
    private static String checkMisspelled3M(ArrayList<BookWord> words,
                                            ArrayList<BookWord> dic, SimpleHashSet<BookWord> simpleHashSetDic){
        ArrayList<String[]> performances = new ArrayList<>();
        String checkMisspelled = "5. Check misspelled words:\n\t____________Method________________Misspelled Words___Performance\n\t";
        String performanceM1 = "Contains method of ArrayList";
        long startTimeEfficiency = System.nanoTime();         //calculate timing
        checkMisspelled += performanceM1+"\t\t" +checkMisspelledM1(words,dic)+"\t\t\t  ";
        int endTimeEfficiency = Math.abs((int)(System.nanoTime() - startTimeEfficiency)/1000000);
        performances.add(new String[]{performanceM1,endTimeEfficiency+""});
        checkMisspelled+=performances.get(0)[1]+"ms\n\t";

        String performanceM2 = "BinarySearch method";
        startTimeEfficiency = System.nanoTime();
        checkMisspelled += "\t"+performanceM2+"\t\t\t\t" +checkMisspelledM2(words,dic)+"\t\t\t  ";
        endTimeEfficiency = Math.abs((int)(System.nanoTime() - startTimeEfficiency)/1000000);
        performances.add(new String[]{performanceM2, endTimeEfficiency+""});
        checkMisspelled+=performances.get(1)[1]+"ms\n\t";

        String performanceM3 = "Contains method and a03.SimpleHashSet";
        startTimeEfficiency = System.nanoTime();
        checkMisspelled += performanceM3+"\t" +checkMisspelledM3(words,simpleHashSetDic)+"\t\t\t  ";
        endTimeEfficiency = Math.abs((int)(System.nanoTime() - startTimeEfficiency)/1000000);
        performances.add(new String[]{performanceM3,endTimeEfficiency+""});
        checkMisspelled+=performances.get(2)[1]+"ms\n\t";

        Collections.sort(performances, (p1,p2) -> Integer.parseInt(p1[1]) - Integer.parseInt(p2[1]));
        DISCUSSIONS+="\t2. The best performance -> ";
        for (String[] per : performances)
            DISCUSSIONS+=per[0]+"("+per[1]+"ms) -> ";
        DISCUSSIONS+=" The lowest performance.\n";

        return checkMisspelled;
    }
    /** PART B
     * The method to read and store all words into an array from a TheLordOfTheRIngs.txt file.
     * @param filename The name of file
     * @param length the length of this array
     * @return an array stored all the word from TheLordOfTheRIngs.txt file
     */
    private static String[] arrWords(String filename, int length){
        String[] _arrWords = new String[length];
        int count = 0;
        try {
            Scanner fin = new Scanner(new File(filename));
            fin.useDelimiter("\\s|\"|\\(|\\)|\\.|\\,|\\?|\\!|\\_|\\-|\\:|\\;|\\n");  // Filter - DO NOT CHANGE
            while (fin.hasNext()) {
                String fileWord = fin.next().toLowerCase();
                if (fileWord.length() > 0){
                    _arrWords[count] = fileWord;
                    count++;
                }
            }
            fin.close();
        } catch (FileNotFoundException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
        return _arrWords;
    }
    /** PART B
     * The method to store a word and its indexes into a two-dimensional array ([word][index]) from an array of words
     * @param arrWords a string array stored all the word from TheLordOfTheRIngs.txt file
     * @param closenessString the word to store into this two-dimensional array
     * @return an array that stores this word and its indexes into a two-dimensional array ([word][index]) from an array of words
     */
    private static String[][] arrWordAndIndex(String[] arrWords, String closenessString){
        int count=0;
        for (String word : arrWords) if (word.equalsIgnoreCase(closenessString)) count++;
        String[][] _closeness = new String[count][2];
        count=0;
        for (int i=0; i<arrWords.length; i++)
            if (arrWords[i].equalsIgnoreCase(closenessString)){
                _closeness[count][0] = arrWords[i];
                _closeness[count][1] = i+"";
                count++;
            }
        return _closeness;
    }
    /** PART B
     * The method to store words of [ACTORS] array and their indexes
     *      into a three-dimensional array ([index of ACTORS array][word][index]) from an array of words
     * @param arrWords a string array stored all the word from TheLordOfTheRIngs.txt file
     * @return an array that stores these words and their indexes
     *      into a three-dimensional array ([index of ACTORS array][word][index]) from an array of words
     */
    private static String[][][] actors(String[] arrWords){
        String[][][] actors = new String[ACTORS.length][][];
        int count=0;
        for (String actor : ACTORS){
            actors[count] = arrWordAndIndex(arrWords, actor);
            count++;
        }
        return actors;
    }
    /** PART B
     * This method will calculate and store the proximity distances of characters to the word [WORD_TO_CALC_PRO_DISTANCE]
     *      within the text and count up the times it is within a proximity cut-off distance
     * @param rings an array of the word [WORD_TO_CALC_PRO_DISTANCE] including their positions
     * @param actors an array of characters to calculate the proximity distances including their positions
     * @return an array that stores proximity distances of characters to the word [WORD_TO_CALC_PRO_DISTANCE]
     *      into a two-dimensional array ([index of ACTORS array][the info of the proximity distances for this ACTOR])
     */
    private static String[][] proximityDistances(String[][] rings, String[][][] actors){
        int[] closeness = new int[actors.length];
        int count=0;
        for (String[][] actor : actors){
            if (actor.length>0) {
                int countProDistance = 0;
                for (String[] _actor : actor)
                    for (String[] ring : rings)
                        if (Math.abs(Integer.parseInt(_actor[1]) - Integer.parseInt(ring[1])) <= PRO_DISTANCE_CUT_OFF)
                            countProDistance++;
                closeness[count] = countProDistance;
            }
            else closeness[count] = 0;
            count++;
        }
        String[][] proximityDistances = new String[ACTORS.length][4];
        for (int i=0; i< ACTORS.length; i++)
            proximityDistances[i] = new String[]{ACTORS[i], actors[i].length+"",
                    closeness[i]+"", (actors[i].length == 0 ? 0 : (double)closeness[i]/actors[i].length)+""};
        Arrays.sort(proximityDistances,
                (pd1, pd2) -> (int) (Double.parseDouble(pd2[3])*10000 - Double.parseDouble(pd1[3])*10000));
        return proximityDistances;
    }
    /**
     * The starting point of the application
     */
    public static void main(String[] args)
    {
        // File is stored in a resources folder in the project
        final String filename = "src/a03/TheLOrdOfTheRings.txt";
        int count = 0;
        try {
            Scanner fin = new Scanner(new File(filename));
            fin.useDelimiter("\\s|\"|\\(|\\)|\\.|\\,|\\?|\\!|\\_|\\-|\\:|\\;|\\n");  // Filter - DO NOT CHANGE
            while (fin.hasNext()) {
                String fileWord = fin.next().toLowerCase();
                if (fileWord.length() > 0)
                {
                    // Just print to the screen for now - REMOVE once you have completed code
//                    System.out.printf("%s\n", fileWord);
                    count++;
                }
            }
            fin.close();
        } catch (FileNotFoundException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
        System.out.println("I/ PART A ____________\n1. There are a total of " + count + " words in the file ");

        // ADD other code after here
        ArrayList<BookWord> dic = arrDic("src/a03/US.txt");  // read us.txt
        SimpleHashSet<BookWord> simpleHashSetDic = simpleHashSetDic("src/US.txt");  // read us.txt
        ArrayList<BookWord> words = words(filename);    // read TheLordOfTheRIngs.txt

        /** PART A*/
        System.out.print("2. There are a total of "+words.size()+" different words in the file\n");
        // The list of the most frequent words and counts
        String mostFrequentWords = "3. The list of the "+MOST_FREQUENT_WORDS+" most frequent words and counts : ";
        for (int i=0; i< mostFrequentWords(words).length; i++)
            mostFrequentWords += "\n\t 3."+(i+1)+": ["+mostFrequentWords(words)[i]+"]";
        System.out.println(mostFrequentWords);
        // The list of words below each occur 64 times
        String occurList = "4. The words below each occur "+OCCUR_EXACTLY+" times in the file: ";
        for (int i=0; i< occurExactly(words).size(); i++)
            occurList += "\n\t 4."+(i+1)+": ["+occurExactly(words).get(i)+"]";
        System.out.println(occurList);
        // Calculate the total of misspelled words and performance.
        System.out.println(checkMisspelled3M(words,dic, simpleHashSetDic)
                +"\nII/ PART B ____________\n\t____ORDER of WHO REALLY WANTS the RING_______________");

        /** PART B*/
        String[] arrWords = arrWords(filename,count);
        String[][] proximityDistances = proximityDistances(arrWordAndIndex(arrWords, WORD_TO_CALC_PRO_DISTANCE), actors(arrWords));
        for (String[] pd : proximityDistances)
            System.out.println("\t["+pd[0]+","+pd[1]+"]\t"+(pd[0].length()<6 && Integer.parseInt(pd[1])<1500 ? "\t" :"")
                    +"Close to "+WORD_TO_CALC_PRO_DISTANCE.toUpperCase()+" "+pd[2]+(Integer.parseInt(pd[2])<10 ?"\t":"")
                    +"\tCloseness Factor "+(new DecimalFormat("#.####").format(Double.parseDouble(pd[3]))));

        /** DISCUSSIONS*/
        System.out.println("\nIII/ DISCUSSION: ____________\n"+DISCUSSIONS);
    }
}