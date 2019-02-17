package core;

import java.util.Random;

/**
 * Connect4ComputerPlayer is a specialization of the Player class, this represents a computer player.
 * @author Kevin Somers
 */
public class Connect4ComputerPlayer extends Player{

    /**
     * player constructor, initializes the player
     *
     * @param id     integer identification number for the player, this is what is used on the backend for the board.
     * @param isHuman for the computer player this is always false
     */
    public Connect4ComputerPlayer(int id, boolean isHuman) {
        super(id, isHuman);
    }

    /**
     * currently use the naive algorithm of random guesses to generate a random number
     * for the computers choice.
     * @param gameBoard the gameboard in use for the game
     * @param scoreChecker currently does not use the scorechecker but a more intelligent algorithm would use this.
     * @return returns int representing the column of the computers choosing
     */
    public int getMove(GameBoard gameBoard, ScoreChecker scoreChecker){
        return getRandMove(gameBoard);
    }

    /**
     * Very naive computer strategy of random guesses.
     * This method continues to generate a random number until the random number is
     * within the bounds of the board and not a column that's already full.
     * @param gameBoard uses the gameboard to verify the random number generated is valid
     * @return an integer representing the column of choice
     */
    private int getRandMove(GameBoard gameBoard){
        Random rand = new Random();
        while(true){
            int randomNum = rand.nextInt((gameBoard.getColLength() - 1) + 1);
            if (randomNum >= 0
                    && (randomNum <= gameBoard.getColLength() - 1)
                    && !gameBoard.colIsFull(randomNum))
                return randomNum; }
    }
}
