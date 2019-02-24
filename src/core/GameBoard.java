package core;

import java.io.Serializable;

/**
 * The GameBoard represents the state of the game and is backed by a 2D array of integers.
 * @author Kevin Somers
 */
public class GameBoard implements Serializable {
    private int[][] board;
    private int colLength;
    private int rowLength;
    private int totalSpots;


    /**
     * declare and initialize the structure backing the game board.  Unused spaces contain -1, when a
     * players piece is placed on the board the -1 is replaced with the players ID, indicating that is their spot.
     * @param row the number of rows the GameBoard will have
     * @param col the number of columns the GameBoard will have
     */
    public GameBoard(int row, int col){
        this.colLength = col;
        this.rowLength = row;
        this.board = new int[this.rowLength][this.colLength];
        for (int i = 0; i < this.rowLength; i++){
            for(int j = 0; j < this.colLength; j++){
                this.board[i][j] = -1;
            }
        }
        this.totalSpots = row * col;
    }

    /**
     * accessor method
     * @param row row of interest
     * @param col column of interest
     * @return returns the integer value at the position (row,col)
     */
    public int getPos(int row, int col){
        return this.board[row][col];
    }

    /**
     * places the players piece in the requested column by 'dropping' the checker to the bottom.
     *
     * @param col the column the player has requested to place a checker at
     * @param playerID the current player requesting to put the checker on the board
     * @return the integer value of the row the checker was successfully placed at,
     * allowing the caller to know exactly where the checker was placed. If the column is full returns -1.
     */
    public int putPiece(int col, int playerID){
        int row = -1;
        if (colIsFull(col) || isOutOfBounds(col))
            return row;
        for(int i = this.rowLength - 1; i >= 0; i--){
            if (slotIsTaken(i, col))
                continue;
            this.board[i][col] = playerID;
            row = i;
            break;
        }
        return row;
    }

    /**
     * checks to see if the slot requested is already taken
     * @param row row of interest
     * @param col column of interest
     * @return boolean of whether or not the specific (row,col) is taken by another Player or not
     */
    public boolean slotIsTaken(int row, int col){
        return getPos(row, col) != -1;
    }

    /**
     * checks to see if the column is full (and therefore not accepting anymore checkers)
     * @param col column of interest
     * @return boolean indicating whether the entire column is already full
     */
    public boolean colIsFull(int col){
        return slotIsTaken(0, col);
    }

    /**
     * accessor method for the GameBoard's column length
     * @return integer of the total number of columns
     */
    public int getColLength() {
        return this.colLength;
    }

    /**
     * accessor method for the GameBoard's row length
     * @return integer of the total number of rows
     */
    public int getRowLength() {
        return this.rowLength;
    }

    /**
     * checks whether the row,col pair is within bounds of the GameBoard
     * @param row row of interest
     * @param col column of interest
     * @return boolean indicating whether the (row,col) slot is out of bounds or not
     */
    public boolean isOutOfBounds(int row, int col){
        return row >= this.rowLength
                || col >= this.colLength
                || row < 0
                || col < 0;
    }

    public boolean isOutOfBounds(int col){
        return col >= this.colLength || col < 0;
    }

    /**
     * accessor
     * @return returns the totalSpots variable, representing the number of spots remaining on the board
     */
    public int getSpots(){
        return this.totalSpots;
    }

    /**
     * reduces totalSpots by one
     */
    public void decrementSpot(){
        this.totalSpots--;
    }
}
