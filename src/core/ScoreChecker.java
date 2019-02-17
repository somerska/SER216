package core;


/**
 * ScoreChecker is responsible for knowing how to check win scenarios and
 * calculating consecutive pieces.
 * @author Kevin Somers
 */
public class ScoreChecker {

    /**
     * uses ScoreChecker and checks for all kinds of winning scenarios (horizontal, vertical or diagonal)
     * @param row the row of interest to be checked
     * @param col the column of interest to be checked
     * @return boolean indicating whether or not there is a winner
     */
    public boolean gameHasWinner(GameBoard gameBoard, int row, int col){
        return hasVertWinner(gameBoard, row, col) ||
                hasHorizWinner(gameBoard, row, col) ||
                hasDiagWinner(gameBoard, row, col);
    }

    /**
     * checks for a vertical winner, this method is just a wrapper around navigateAndCount.
     * @param row the row of interest to be checked
     * @param col the column of interest to be checked
     * @return boolean indicating whether or not there is a vertical winner
     */
    public boolean hasVertWinner(GameBoard gameBoard, int row, int col){
        if (navigateAndCount(gameBoard, row, col, "vertical") >= 4)
            return true;
        return false;
    }

    /**
     * checks for a horizontal winner, this method is just a wrapper around navigateAndCount.
     * @param row the row of interest to be checked
     * @param col the column of interest to be checked
     * @return boolean indicating whether or not there is a horizontal winner
     */
    public boolean hasHorizWinner(GameBoard gameBoard, int row, int col){
        if(navigateAndCount(gameBoard, row, col, "horizontal") >= 4)
            return true;
        return false;
    }

    /**
     * checks for a diagonal winner, this method is just a wrapper around navigateAndCount.
     * @param row the row of interest to be checked
     * @param col the column of interest to be checked
     * @return boolean indicating whether or not there is a diagonal winner
     */
    public boolean hasDiagWinner(GameBoard gameBoard, int row, int col){
        if(navigateAndCount(gameBoard, row, col, "diagonal") >= 4)
            return true;
        return false;
    }

    /**
     * navigate from the provided position in both direction to count the total consecutive.  For example, if
     * checking horizontally, this method will start at the col,row and navigate right counting consecutive
     * pieces, then navigate left.  The total of navigating in both directions is returned.  This method can handle
     * checking for horizontal, vertical and diagonal winners.
     * @param row the row of interest to be checked
     * @param col the column of interest to be checked
     * @param direction the direction that's being checked: horizontal, vertical, diagonal
     * @return the total number of consecutive slots for the given player and given direction
     */

    public int navigateAndCount(GameBoard gameBoard, int row, int col, String direction){
        int slotOfInterest = gameBoard.getPos(row, col);
        int counterInOneDirection = 0;
        int counterInOtherDirection = 0;
        int rowOfInterest = row;
        int colOfInterest = col;
        switch(direction){
            //all of the counting checks if the position is within bounds, and that the marker in the slot
            //is the same as the one we're counting at that time. For example, if we're counting X's, it will
            //continue counting while the slot's being checked have X's in them.
            case "horizontal":
                //count to the right
                while(!gameBoard.isOutOfBounds(row, colOfInterest) &&
                        gameBoard.getPos(row, colOfInterest) == slotOfInterest) {
                    counterInOneDirection++;
                    colOfInterest++;
                }
                colOfInterest = col - 1; //avoid counting the original spot twice
                //count to the left
                while(!gameBoard.isOutOfBounds(row, colOfInterest) &&
                        gameBoard.getPos(row, colOfInterest) == slotOfInterest){
                    counterInOtherDirection++;
                    colOfInterest--;
                }
                break;
            case "vertical":
                //count up
                while(!gameBoard.isOutOfBounds(rowOfInterest, col) &&
                        gameBoard.getPos(rowOfInterest, col) == slotOfInterest){
                    counterInOneDirection++;
                    rowOfInterest++;
                }
                rowOfInterest = row - 1; //avoid counting the original spot twice
                //count down
                while(!gameBoard.isOutOfBounds(rowOfInterest, col) &&
                        gameBoard.getPos(rowOfInterest, col) == slotOfInterest){
                    counterInOtherDirection++;
                    rowOfInterest--;
                }
                break;
            case "diagonal":
                int inc = calculateConseqDiagIncreasing(gameBoard, row, col, slotOfInterest);
                int dec = calculateConseqDiagDecreasing(gameBoard, row, col, slotOfInterest);
                counterInOneDirection = Math.max(inc, dec);
                counterInOtherDirection = 0;
                break;
        }
        return counterInOneDirection + counterInOtherDirection;
    }

    /**
     * calculates the number of consecutive diagonals in the increasing direction there are.
     * @param row integer row thats in use
     * @param col integer column thats in use
     * @param slotOfInterest the slot being checked, this is the slot that was just put down.
     * @return the number of consecutive diagonals found in the increasing direction
     */
    private int calculateConseqDiagIncreasing(GameBoard gameBoard, int row, int col, int slotOfInterest){
        int counterInOneDirection = 0;
        int counterInOtherDirection = 0;
        int rowOfInterest = row;
        int colOfInterest = col;
        //count diagonal towards the top right
        while(!gameBoard.isOutOfBounds(rowOfInterest, colOfInterest) &&
                gameBoard.getPos(rowOfInterest, colOfInterest) == slotOfInterest){

            counterInOneDirection++;
            rowOfInterest--;
            colOfInterest++;
        }
        rowOfInterest = row + 1; //avoid counting the original spot twice
        colOfInterest = col - 1; //avoid counting the original spot twice
        //count diagonal towards the bot left
        while(!gameBoard.isOutOfBounds(rowOfInterest, colOfInterest) &&
                gameBoard.getPos(rowOfInterest, colOfInterest) == slotOfInterest){
            counterInOneDirection++;
            rowOfInterest++;
            colOfInterest--;
        }
        return counterInOneDirection + counterInOtherDirection;
    }
    /**
     * calculates the number of consecutive diagonals in the decreasing direction there are.
     * @param row integer row thats in use
     * @param col integer column thats in use
     * @param slotOfInterest the slot being checked, this is the slot that was just put down.
     * @return the number of consecutive diagonals found in the decreasing direction
     */
    private int calculateConseqDiagDecreasing(GameBoard gameBoard, int row, int col, int slotOfInterest){
        int counterInOneDirection = 0;
        int counterInOtherDirection = 0;
        int rowOfInterest = row;
        int colOfInterest = col;
        //count diagonal towards the top left
        while(!gameBoard.isOutOfBounds(rowOfInterest, colOfInterest) &&
                gameBoard.getPos(rowOfInterest, colOfInterest) == slotOfInterest){
            counterInOneDirection++;
            rowOfInterest--;
            colOfInterest--;
        }
        rowOfInterest = row + 1; //avoid counting the original spot twice
        colOfInterest = col + 1; //avoid counting the original spot twice
        //count diagonal towards the bot right
        while(!gameBoard.isOutOfBounds(rowOfInterest, colOfInterest) &&
                gameBoard.getPos(rowOfInterest, colOfInterest) == slotOfInterest){
            counterInOneDirection++;
            rowOfInterest++;
            colOfInterest++;
        }
        return counterInOneDirection + counterInOtherDirection;
    }
}
