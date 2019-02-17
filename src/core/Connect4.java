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
        String input = "";
        if(args.length == 0){
            System.out.println("Note that you must have the server launched first!");
            System.out.println("Do you wish Console 'C' or GUI 'G' version of the game?");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
        } else {
            input = args[1];
        }
        if(isGUI(input))
            Connect4GUI.main(args);
        Connect4TextConsole.main(args);
    }
    /**
     * Simple text-interface to ask the user if they want a GUI or text version of the game.
     * @return returns true if they provide valid input and do want a GUI version of the game.
     */
    public static boolean isGUI(String input){
        if(input.equalsIgnoreCase("G")){
            System.out.println("Setting up GUI version");
            return true;
        }
        else if(input.equalsIgnoreCase("C")){
            System.out.println("Setting up console version");
            return false;
        }
        else {
            System.out.println("Invalid input, defaulting to console version");
            return false;
        }
    }
}
