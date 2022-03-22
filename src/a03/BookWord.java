package a03;

/**
 * Implementation of each word for the words in the novel and for the words in the provided dictionary.
 * The words must be stored without concern for case sensitivity (all characters must be converted to lowercase).
 * A word is considered to be one or more characters in length separated by white space (space character,
 *      tab, new line character) or a comma, period, exclamation point or question mark.
 * Quotes may appear in a word but these must be ignored and not included as part of the word.
 * The apostrophe character may also appear in a word.
 * a03.BookWord will be used to hold the characters for each unique word as a string along with a
 *      count which will contain the number of occurrences of that word in the novel.
 * This is the class header.
 *
 * @date Feb 20, 2022
 * @author DUC LONG NGUYEN (Paul)
 *
 */
public class BookWord {
    /**the text of this word **/
    private final String text;
    /**the number of this word in the file **/
    private int count;
    /**
     * Constructor to determine the text of this word and the number of this word in the file
     * They is set when a03.BookWord object is created and the text property cannot be changed (meaning the text property does not have setter methods)
     * @param wordText the text of this word in lower case
     */
    public BookWord(String wordText) {
        this.text = wordText.toLowerCase();
    }
    /**
     * The getText method return the text of a word
     * @return a string as a word
     * */
    public String getText() {
        return text;
    }
    /**
     * The getCount method return the number of this word in the file
     * @return an integer number as the number of this word in the file
     * */
    public int getCount() {
        return count;
    }
    /**
     * The incrementCount method to count the number of this word in the file
     * */
    public void incrementCount(){
        this.count++;
    }
    /**
     * The equals method confirms that the text is the same in both BookWords.
     * @param wordToCompare the word to compare
     * @return a boolean value (true/false), return true if the text is the same in both BookWords, otherwise, return false
     * */
    @Override
    public boolean equals(Object wordToCompare) {
        if (this == wordToCompare) return true;
        if (wordToCompare == null || getClass() != wordToCompare.getClass()) return false;
        return this.text.compareToIgnoreCase(((BookWord) wordToCompare).text) == 0;
    }
    /**
     * The hashCode method calculate and return a hashCode for each a03.BookWord
     * algorithm source: https://www.javamex.com/tutorials/collections/hash_function_technical_2.shtml
     * @return an integer value as a hashCode for each a03.BookWord
     * */
    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < this.text.length(); i++) hash = (hash << 5) - hash + this.text.charAt(i);
        return hash;
    }
    /**
     * The toString method prints a text of a word and the number of this word in the file
     * @return a string of a word and the number of this word in the file
     */
    @Override
    public String toString() {
        return " " + this.text + " , " + this.count + "" ;
    }
}
