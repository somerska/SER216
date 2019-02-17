package ui;

import core.*;
import static core.Constants.*;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Connect4TextConsole is a text UI layer for the game.
 * @author Kevin Somers
 */
public class Connect4TextConsole {

    private String divider = "|";
    private Scanner scanner = new Scanner(System.in);
    private Connect4Client client;
    private int playerID;
    private int winnerID;
    private Character playerMarker;


    public static void main(String[] args){
        Connect4TextConsole textConsole = new Connect4TextConsole();
        textConsole.playGame();
    }

    /**
     * sets up the GameBoard, ScoreChecker and GameManager
     */
    public Connect4TextConsole(){
        this.client = new Connect4Client();
    }

    /**
     * contains the game logic for a text-based version of the game.
     * gets player input, places checker and checks for a winner.  Game
     * continues until there is a winner or a tie.
     */
    public void playGame(){
        requestGameType();
        setupPlayerFromServer();
        GameBoard gameBoard = null;

        while(true){
            try{
                int gameStatus = client.getGameStatus();
                if(gameStatus == CONTINUE){
                    gameBoard = client.getGameBoard();
                    printBoard(gameBoard);
                    int column = getPlayerInput(playerID);
                    Character servResponse = client.sendMove(column);
                    if(servResponse == GOODINPUT){
                        gameBoard.putPiece(column, playerID);
                        printBoard(gameBoard);
                        waitingOnOtherPlayer();
                        continue;
                    } else if(servResponse == BADINPUT){
                        while(true){
                            badColumnSelection();
                            column = getPlayerInput(playerID);
                            servResponse = client.sendMove(column);
                            if (servResponse == GOODINPUT)
                                break;
                        }
                        gameBoard.putPiece(column, playerID);
                        printBoard(gameBoard);
                        waitingOnOtherPlayer();
                    }
                } else if(gameStatus == PLAYER0_WON || gameStatus == PLAYER1_WON){
                    gameBoard = client.getGameBoard();
                    printBoard(gameBoard);
                    announceWinner(gameStatus);
                    break;
                } else if(gameStatus == DRAW){
                    gameEndsInTie();
                    break;
                }
            } catch(IOException ex){
                System.out.println("Lost connection to server, exiting");
                System.exit(1);
            }

        }
    }

    /**
     * get the players id from the server, using constants setup this player to have the correct
     * player id and player marker.
     */
    private void setupPlayerFromServer(){
        try{
            int id = client.getGameStatus();
            if(id == 0){
                playerID = Constants.PLAYER0;
                playerMarker = Constants.PLAYER0MARKER;
                winnerID = Constants.PLAYER0_WON;
            } else {
                playerID = Constants.PLAYER1;
                playerMarker = Constants.PLAYER1MARKER;
                winnerID = Constants.PLAYER1_WON;
            }
        } catch(IOException ex){
            System.out.println("Unable to retrieve player from server, exiting . . .");
            System.exit(1);
        }
    }

    private void waitingOnOtherPlayer(){
        System.out.println("Waiting for other player's move . . .");
    }

    /**
     * Gets user input for game type (player vs player or player vs computer)
     * and uses the GameManager to set up the players based on the input.
     */
    private void requestGameType(){
        promptGameType();
        String gameType = "";
        try {
            gameType = scanner.next();
        } catch(InputMismatchException ex){
            badGameTypeInput();
            //default to player vs player if the input wasn't even a String
            sendGameTypeOrExit('P');
            return;
        }

        if(gameType.equalsIgnoreCase("P")){
            sendGameTypeOrExit('P');
            displayGameType();
        } else if(gameType.equalsIgnoreCase("C")){
            sendGameTypeOrExit('C');
            displayGameType();
        } else {
            //default to player vs player if the input was a string but wasn't P or C
            badGameTypeInput();
            sendGameTypeOrExit('P');
        }
    }

    private void sendGameTypeOrExit(Character gameType){
        try{
            client.sendGameType(gameType);
        } catch (IOException exc){
            System.out.println("Unable to send server game type selected, exiting . . .");
            System.exit(1);
        }
    }


    /**
     * continuously prompts the current human player for their input until a valid input is returned.
     * This method handles bad input and will not return until a good input is provided.  For example,
     * this method handles when a player provides a column that is not within the acceptable range.
     * This method also handles if a user selects a column that's already full.  Once a good input is
     * provided the method converts it back to 0 based indexing and returns the input.
     * @return the validated column of choice
     */
    private int getPlayerInput(int id){
        boolean playerInputValid = false;
        int input = -1;
        while(!playerInputValid){
            try{
                input = promptPlayer(id);
                playerInputValid = true;
            } catch(InputMismatchException ex){
                inputNotanInteger();
            }
        }
        return input;
    }

    /**
     * prompt for game type
     */
    public void promptGameType(){
        System.out.println("Do you wish to play Player vs Player or Player vs Computer?" +
                " 'P' for Player vs Player or 'C' for computer");
    }

    /**
     * Inform player of invalid input when selecting the game type (player vs player or
     * player vs computer)
     */
    public void badGameTypeInput(){
        System.out.println("Invalid input for game play selection, defaulting to " +
                "player vs player");
        displayGameType();

    }

    public void badColumnSelection(){
        System.out.println("The column number provided is bad. " +
                "Provide a valid column that is within range and not full.");
    }

    /**
     * display the game type that was chosen (or defaulted to) to inform the player of the game type
     */
    public void displayGameType(){
        System.out.println("Waiting for other player to connect or select a move");
    }

    /**
     * prompt player for their column of choice, validate their input and return the int value
     * representing their column of choice.  This method catches and throws invalid input and also
     * handles player vs player or computer vs player prompting.
     */
    public int promptPlayer(int id)
            throws InputMismatchException{

        System.out.println("Player " + (id+1) + "(" + playerMarker + ")" +
                " your turn.  Choose a column number from 1-7.");
        //get the input and handle its validity
        int input = -1;
        try {
            input = scanner.nextInt() - 1;
        } catch(InputMismatchException ex){
            scanner.nextLine(); //consume remaining newline
            throw ex; //throw it back up to caller
        }
        return input;
    }



    /**
     * warns that input is not an integer
     */
    public void inputNotanInteger(){
        System.out.println("The input provided is " +
                "not an integer value.");
    }

    /**
     * notify player that the game is ending in a tie, no slots remain.
     */
    public void gameEndsInTie(){
        System.out.println("No more slots are left and no one has gotten 4 consecutively, " +
                "the game ends in a tie!");
    }

    /**
     * notify the player who the winner is
     * @param reportedID the id of the winner
     */
    public void announceWinner(int reportedID){
        if(winnerID == reportedID){
            System.out.println("You won!");
        } else {
            System.out.println("The other player has won the game!");
        }
    }

    /**
     * Print the game board, showing the current status of what slots are still open
     * and where the players markers are.
     * @param gameBoard the game board that is being used, in this case a 2D array of integers
     */
    public void printBoard(GameBoard gameBoard){
        for(int i = 0; i < gameBoard.getRowLength(); i++){
            System.out.print(divider);
            for (int j = 0; j < gameBoard.getColLength(); j++){
                Character slot = ' ';
                if (gameBoard.slotIsTaken(i, j))
                    slot = (gameBoard.getPos(i,j) == PLAYER0) ? PLAYER0MARKER : PLAYER1MARKER;
                System.out.print(slot + divider);
            }
            System.out.println();
        }
    }
}

