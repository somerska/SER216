package core;

import ui.Connect4GUI;
import ui.Connect4TextConsole;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The Connect4 class is the entry point to the program.
 * Prompts user for GUI or Text based game and creates the needed objects to start them.
 * @author Kevin Somers
 * @version 3.0
 *
 */
public class Connect4 {
    public static void main(String[] args) {
        if(isGUI()){
            Connect4GUI.main(args);
        } else {
            Connect4TextConsole.main(args);
        }
    }
    /**
     * Simple text-interface to ask the user if they want a GUI or text version of the game.
     * @return returns true if they provide valid input and do want a GUI version of the game.
     */
    private static boolean isGUI(){
        System.out.println("Note that you must have the server launched first!");
        System.out.println("Do you wish to play a console (text) or GUI version " +
                "of the game? 'G' for GUI or 'C' for console");
        Scanner scanner = new Scanner(System.in);
        String input = "";
        try{
            input = scanner.nextLine();
        } catch (InputMismatchException ex){
            System.out.println("Invalid input, defaulting to console version");
            return false;
        }
        if(input.equalsIgnoreCase("G")){
            return true;
        }
        else if(input.equalsIgnoreCase("C")){
            return false;
        }
        else {
            System.out.println("Invalid input, defaulting to console version");
            return false;
        }
    }
}
