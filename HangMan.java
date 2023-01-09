import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author syeda0080
 */
public final class HangMan implements Runnable, ActionListener {

    // Class Variables  
    ArrayList<String> words = new ArrayList<>();
  
    JPanel mainPanel;
    JLabel title;
    JLabel knownWordLabel;
    JLabel wrongGuesses;
    JLabel lines;
    JTextField textBox;
    JButton submitButton;
    String randomWord;
    String wordKnown = "";
    String wrongGuessString = "Wrong guesses ";
    int Wrongcount = 0;
    Font bigger = new Font("arial", Font.BOLD, 36);
    Font bigger2 = new Font("arial", Font.BOLD, 20);

    // Method to assemble our GUI
    public void run() {
        // Creats a JFrame that is 800 pixels by 600 pixels, and closes when you click on the X
        JFrame frame = new JFrame("Hangman");
        // Makes the X button close the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // makes the windows 800 pixel wide by 600 pixels tall
        frame.setSize(800, 600);
        // shows the window
        frame.setVisible(true);
        //create the panel to put things in 
        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // set the x,y position and the width height 
        textBox = new JTextField();
        textBox.setBounds(250, 200, 250, 20);
        // set the x,y position and the width height 
        title = new JLabel("Hangman");
        title.setBounds(280, 50, 350, 50);
        title.setFont(bigger);

        // set the x,y position and the width height  
        knownWordLabel = new JLabel("");
        knownWordLabel.setBounds(291, 125, 350, 50);
        knownWordLabel.setFont(bigger2);

        // set the x,y position and the width height  
        lines = new JLabel("_ _ _ _ _");
        lines.setBounds(290, 125, 350, 50);
        lines.setFont(bigger2);
        // set the x,y position and the width height  
        wrongGuesses = new JLabel("");
        wrongGuesses.setBounds(250, 200, 350, 50);
        // set the x,y position and the width height  
        submitButton = new JButton("Submit");
        submitButton.setBounds(270, 270, 90, 20);
        submitButton.addActionListener(this);
        submitButton.setActionCommand("submit");

        // add it to the main panel 
        mainPanel.add(title);
        mainPanel.add(textBox);
        mainPanel.add(knownWordLabel);
        mainPanel.add(submitButton);
        mainPanel.add(wrongGuesses);
        mainPanel.add(lines);
        // put the panel in the frame 
        frame.add(mainPanel);

    }
 // method for getting word from the dictionary
    public HangMan() throws MalformedURLException, IOException {
        // add the dictionary url
        URL dictionaryFile = new URL("https://web.stanford.edu/class/cs168/dictionary.txt");
        // scans each word 
        Scanner scan = new Scanner(dictionaryFile.openStream());
        // looks for 5 words and add them 
        while (scan.hasNext()) {
            String word = scan.nextLine();
            if (word.length() == 5) {
                words.add(word);
            }
        }
        // method for word generator
        WordGenerator();
    }
    // method for picking random words 
    private void WordGenerator() {
        int lowest = 1;
        int highest = words.size();
        int randomIndex = (int) (Math.random() * (highest - lowest + 1) + lowest);
        randomWord = words.get(randomIndex);
        System.out.println(randomWord);
    }
    // method called when a button is presse
    public void actionPerformed(ActionEvent e) {
        // get the command from the action
        String command = e.getActionCommand();
        if (command.equals("submit")) {

            String displayWord = "";
            // add spaces depending on the random word 
            for (int i = 0; i < randomWord.length(); i++) {
                wordKnown += " ";
                displayWord += "  ";

            }
            // add the code from methods 
            if (randomWord.indexOf(textBox.getText()) >= 0) {
                RightGuess();
            } else {
                wrongGuess();
                textBox.setText("");
            }
        }

    }
    // what happens if the guess is right 
    private void RightGuess() {
        String guess = textBox.getText();
        KnownWord(guess);
        String displayString = "";
        for (int i = 0; i < wordKnown.length(); i++) {
            displayString += wordKnown.substring(i, i + 1) + " ";
        }
        // diplay the right guess 
        knownWordLabel.setText(displayString);
        // the guess right then tell player that you won 
        if (wordKnown.contains(randomWord)) {
            JOptionPane.showMessageDialog(mainPanel, "You Win!");
        }
        textBox.setText("");
    }
    // checks if the input is = to the randomword
    private void KnownWord(String guess) {
         ArrayList<Integer> indexes = new ArrayList<>();
        // checks if the input is = to the randomword
        for (int index = randomWord.indexOf(guess);
                index >= 0;
                index = randomWord.indexOf(guess, index + 1)) {
            indexes.add(index);
        }
        // convert int class to a string 
        for (int i = 0; i < indexes.size(); i++) {
            int index = indexes.get(i);
            StringBuilder stringBuilder = new StringBuilder(wordKnown);
            stringBuilder.replace(index, index + guess.length(), guess);
            wordKnown = stringBuilder.toString();
        }
    }
    // what happens if guess wrong 
    private void wrongGuess() {
        // count it once 
        Wrongcount++;
        // display all the wrong inputs 
        wrongGuessString += textBox.getText() + ", ";
        wrongGuesses.setText(wrongGuessString);
        // end game if wrong 6 times 
        if (Wrongcount == 6) {
            JOptionPane.showMessageDialog(mainPanel, "you lose (⌣︵⌣) and the word was " + randomWord);

        }
    }

    // Main method to start our program
    public static void main(String[] args) throws IOException {
        // Creates an instance of our program
        HangMan gui = new HangMan();
        // Lets the computer know to start it in the event thread
        SwingUtilities.invokeLater(gui);
    }
}
