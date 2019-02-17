package test;

import core.GameBoard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static core.Constants.*;
import static org.junit.Assert.*;

public class GameBoardTest {
    private static GameBoard gameBoard;
    private static GameBoard fullColGameBoard;

    @Before
    public void setUp() throws Exception {
        gameBoard = new GameBoard(NUMROWS, NUMCOLUMNS);
        fullColGameBoard = new GameBoard(NUMROWS, NUMCOLUMNS);
        for(int i = 0; i < fullColGameBoard.getRowLength(); i++){
            fullColGameBoard.putPiece(0, 0);
        }
    }

    @After
    public void tearDown() throws Exception {
        gameBoard = null;
    }

    @Test
    public void SixBySevenGameBoardHasFortyTwoSpots() throws Exception {
        assertEquals(gameBoard.getSpots(), 42);
    }

    @Test
    public void NewBoardIsEmpty() throws Exception {
        for(int i = 0; i < gameBoard.getColLength() - 1; i++){
            for(int j = 0; j < gameBoard.getRowLength() - 1; j++)
                assertEquals(gameBoard.getPos(i, j), -1);
        }
    }

    @Test
    public void FullColumnReturnsNegOneWhenTryingToPutPieceThere() throws Exception{
        assertEquals(fullColGameBoard.putPiece(0, 0), -1);
    }

    @Test
    public void SlotIsTakenOnFullColGameBoard() throws Exception{
        assertEquals(fullColGameBoard.slotIsTaken(0, 0), true);
    }

    @Test
    public void ColIsFullOnFullColGameBoardFirstColumn() throws Exception {
        assertEquals(fullColGameBoard.colIsFull(0), true);
    }

    @Test
    public void ColisNotFullOnFullColGameBoardSecondColumn() throws Exception {
        assertEquals(fullColGameBoard.colIsFull(1), false);
    }

    @Test
    public void ColSevenIsOutOfBounds() throws Exception {
        assertEquals(fullColGameBoard.isOutOfBounds(7), true);
    }

    @Test
    public void ColSixIsNotOutOfBounds() throws Exception {
        assertEquals(fullColGameBoard.isOutOfBounds(6), false);
    }

    @Test
    public void ColNegOneIsOutOfBounds() throws Exception {
        assertEquals(fullColGameBoard.isOutOfBounds(-1), true);
    }

    @Test
    public void RowFiveColFiveIsNotOutOfBounds() throws Exception {
        assertEquals(fullColGameBoard.isOutOfBounds(5,5), false);
    }

    @Test
    public void RowFiveColEightIsOutOfBounds() throws  Exception {
        assertEquals(fullColGameBoard.isOutOfBounds(5,8), true);
    }

    @Test
    public void NewGameBoardTwoMovesInHasTwoLessSpots() throws Exception {
        int newBoardSpots = gameBoard.getSpots();
        gameBoard.decrementSpot();
        gameBoard.decrementSpot();
        int twoLessSpots = gameBoard.getSpots();
        assertEquals(newBoardSpots - twoLessSpots, 2);
    }

}