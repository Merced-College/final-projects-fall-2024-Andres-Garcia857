/**
* Word.java
* @author Andres Garcia
* @since 11-14-2024
* This class declares and defines object Word
*/

public class Word {
    // Instance fields
    private String text;
    private int length;

    // Default constructor
    public Word(){
        text = "/undefined/";
        length = -1;
    }

    /**
     * Contructor that takes a word as a String
     * Preconditions: A string must exist
     * Postconditions: Objects word and length are updated
     * @param word String to update the object with
     */
    public Word(String text) {
        this.text = text;
        this.length = text.length();
    }

    /**
     * Getters and setters for text and length
     * Preconditions: Fields are declared for setters and initialized for getters
     * Postconditions: Fields are returned or updated
     */
    public String getText() {
        return text.toUpperCase();
    }

    public int getLength() {
        return length;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /**
     * toString method
     * Preconditions: Each field is initialized
     * Postconditions: Every field's values are returned
    */
    @Override
    public String toString() {
        return "Word{" + text + ", " + length + "}";
    }
    
}