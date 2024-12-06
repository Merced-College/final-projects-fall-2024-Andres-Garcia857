/**
 * AdvancedWordle.java
 * @author Andres Garcia
 * @since 11/14/2024
 * This class simulates a terminal-based game of wordle while
 * adding a difficulty based on varying lengths and allowing replays
 * so long as you have guesses remaining.
*/

import java.io.*;
import java.util.*;

public class AdvancedWordle {
    public static ArrayList<Word> easyWords = new ArrayList<Word>(); // List of 4-lettered words
    public static ArrayList<Word> normalWords = new ArrayList<Word>(); // List of 5-lettered words
    public static ArrayList<Word> hardWords = new ArrayList<Word>(); // List of 6-lettered words
    public static ArrayList<Word> veryHardWords = new ArrayList<Word>(); // List of 7-lettered words

    public static Random random = new Random();
    public static Scanner scnr = new Scanner(System.in);
    

    public static void main(String[] args) {
        
        /**
         * Reads in words of WordList.txt, adding them to appropriate list
         * Partially generated by ChatGPT
        */
        String filePath = "WordList.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Reads through every word in file
            while ((line = br.readLine()) != null) {
                Word word = new Word(line); // Creates a Word object for each word
                // If current word has 4, 5, 6, or 7 letters
                if (word.getLength() == 4){
                    easyWords.add(word); // Add 4-letter words for easy difficulty
                }else if (word.getLength() == 5) {
                    normalWords.add(word); // Add 5-letter words for normal difficulty
                } else if (word.getLength() == 6) {
                    hardWords.add(word); // Add 6-letter words for hard difficulty
                } else if (word.getLength() == 7) {
                    veryHardWords.add(word); // Add 7-letter words for very hard difficulty
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        int totalGuesses = 6; // Total amount of guesses left
        int currGuess; // Current guess
        Word word; // Holds word for user to guess
        String input; // Reads in user input
        Queue<Word> wordsGuessed = new LinkedList<>(); // List of words the user correctly guessed
        System.out.println("\nWelcome to Advanced Wordle! Remember to type /STOP at any point to end the game");

        // Start of game, ends when user runs out of guesses
        while (true){
            // Begins current guess
            currGuess = 1;

            // User decides difficulty
            System.out.println("EASY - 4 letters, NORMAL - 5 letters, HARD - 6 letters, or VERY HARD - 7 letters");
            System.out.print("Choose difficulty: ");
            input = scnr.nextLine().toUpperCase();

            // If user entered /STOP, end the game
            if (isStop(input)) {
                break;
            }
            
            // If user entered a difficulty
            if (input.equals("EASY")){
                word = new Word(getRandomWord(easyWords)); // Get random word for easy difficulty
            } else if (input.equals("NORMAL")) {
                word = new Word(getRandomWord(normalWords)); // Get random word for normal difficulty
            } else if(input.equals("HARD")) {
                word = new Word(getRandomWord(hardWords)); // Get random word for hard difficulty
            } else if(input.equals("VERY HARD")) {
                word = new Word(getRandomWord(veryHardWords)); // Get random word for very hard difficulty
            } else {
                // Otherwise, ask user to enter difficulty again
                System.out.println("Incorrect input. Type EASY, NORMAL, HARD, or VERY HARD");
                continue;
            }
            System.out.println(word.getText());

            // Print out initial length of word
            for (int i = 0; i < word.getLength(); i++){
                System.out.print("- ");
            }
            System.out.println();
            
            // While user has guesses remaining
            while(currGuess <= totalGuesses){
                System.out.print("Guess " + currGuess + " of " + totalGuesses + ": ");
                input = scnr.nextLine().toUpperCase(); // User makes a guess

                // If user enters /STOP, end the game
                if (isStop(input)) {
                    break;
                }

                // If user's guess is correct, user wins
                if (isCorrect(input, word)) {
                    System.out.println("You win! With " + (totalGuesses - currGuess) + " attempts left, the answer was: " + word.getText());
                    wordsGuessed.add(word);
                    break;
                } else if (input.length() == word.getLength()) {
                    // Else if user's guess is same length as word
                    for (char ch : input.toCharArray()) {
                        System.out.print(ch + " "); // Print out user's guess
                    }
                    System.out.println("");
                    
                    // Print out every correct, misplaced, or incorrect letter
                    checkGuess(input, word);
                    currGuess++; // Start next guess
                } else {
                    // Otherwise, ask user enter guess with correct number of letters
                    System.out.println("Please enter " + word.getLength() + " letters");
                }
            }

            // If user entered /STOP in prior inner loop, end the game
            if (isStop(input)) {
                break;
            }

            // If user has guesses remaining, continue the game with remaining guesses plus 6 more
            if (totalGuesses - currGuess >= 0) {
                totalGuesses = totalGuesses - currGuess + 6;
                System.out.println("Adding 6 more guesses, you now have " + totalGuesses + " attempts to continue!");
                continue;
            } else {
                // Otherwise, user loses
                System.out.println("Game over! The answer was: " + word.getText());
            }

            // If user lost, user decides whether to restart or end game
            while (!input.equals("YES") || !input.equals("NO")) {
                System.out.print("Play again? (YES or NO): ");
                input = scnr.nextLine().toUpperCase();

                // If user entered YES, game restarts
                if (input.equals("YES")) {
                    printWordsGuessed(wordsGuessed); // Print out any correctly guessed words
                    System.out.println("Restarting game....");
                    totalGuesses = 6; // Set total guesses back to base 6
                    break;
                } else if (input.equals("NO")) {
                    // Else if user entered NO, break out of while loop
                    break ;
                }
            }
            // If input entered NO in prior while loop, game ends
            if (input.equals("NO")) {
                break;
            }
        }

        printWordsGuessed(wordsGuessed); // Print out any correctly guessed words
        System.out.println("Thanks for playing!");

        scnr.close();
        
    }// end main

    /**
     * Method to grab a random word from a list
     * Preconditions: ArrayList of Word objects must exist
     * Postconditions: A random word from a given ArrayList is returned
     * @param list The ArrayList to pull a random word from
    */
    public static String getRandomWord(ArrayList<Word> list) {
        int randomIndex = random.nextInt(list.size() - 1); // Generate a random index for the list
        return list.get(randomIndex).getText(); // Return the randomized word
    }

    /**
     * Method compares every letter in user's guess to the anwer and print
     * its accuracy
     * Preconditions: A string and Word object must exist for parameters
     * Postconditions: Prints out whether the user's guess was correct, misplaced,
     * or incorrect
     * @param input String used as user's guess
     * @param word Word object containing the answer
     */
    public static void checkGuess(String input, Word word) {
        // Iterates through every letter of the answer and user's guess
        for (int i = 0; i < word.getLength(); i++){
            // If correct letter and correct position, print out letter
            if (input.charAt(i) == word.getText().charAt(i)) {
                System.out.print(input.charAt(i) + " ");
            } else if (word.getText().contains(Character.toString(input.charAt(i)))){
                // Else if correct letter but incorrect position, print out /
                System.out.print("/ ");
            } else {
                // Otherwise, print out -
                System.out.print("- ");
            }
        }
        System.out.println("");
    }

    /**
     * Method to check if user inputted /STOP
     * Preconditions: A string must exist
     * Postconditions: Method returns true if user wrote /STOP, false if not
     * @param input String to check for /STOP
    */
    public static boolean isStop(String input) {
        if (input.equals("/STOP")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to determine whether or not the user's guess was correct
     * Preconditions: String and Word object must exist for parameters
     * Postconditions: Returns true if guess was correct, false if not
     * @param input String for user's guess
     * @param word Word object holding the answer
     */
    public static boolean isCorrect(String input, Word word) {
        if (input.equals(word.getText())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to print and empty out a list of words correctly guessed
     * Preconditions: A queue must exist for parameters
     * Postconditions: The list of words are printed out and emptied
     * @param wordsGuessed Queue of words correctly guessed
     */
    public static void printWordsGuessed(Queue<Word> wordsGuessed) {
        if(!wordsGuessed.isEmpty()){
            System.out.print("What you've guessed correctly this round: ");
            while (!wordsGuessed.isEmpty()) {
                System.out.print(wordsGuessed.poll().getText() + " ");
            }
            System.out.println();
        }
    }
}