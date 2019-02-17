package test;

import core.Connect4;
import core.Constants;
import core.GameBoard;
import core.ScoreChecker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static core.Constants.*;
import static org.junit.Assert.*;

public class ScoreCheckerTest {
    private static GameBoard vertWinnerBoard;
    private static GameBoard horiWinnerBoard;
    private static GameBoard diagWinnerBoard;
    private static GameBoard noWinner;

    private static ScoreChecker scoreChecker;


    @Before
    public void setUp() throws Exception {
        //setup a board with a vertical winner
        vertWinnerBoard = new GameBoard(NUMROWS, NUMCOLUMNS);
        vertWinnerBoard.putPiece(0, 0);
        vertWinnerBoard.putPiece(0, 0);
        vertWinnerBoard.putPiece(0, 0);
        vertWinnerBoard.putPiece(0, 0);
        //setup a board with a horizontal winner
        horiWinnerBoard = new GameBoard(NUMROWS, NUMCOLUMNS);
        horiWinnerBoard.putPiece(0, 0);
        horiWinnerBoard.putPiece(1, 0);
        horiWinnerBoard.putPiece(2, 0);
        horiWinnerBoard.putPiece(3, 0);
        horiWinnerBoard.putPiece(4, 0);
        //setup a board with a diagonal winner
        diagWinnerBoard = new GameBoard(NUMROWS, NUMCOLUMNS);
        diagWinnerBoard.putPiece(0, 0);
        diagWinnerBoard.putPiece(1, 1);
        diagWinnerBoard.putPiece(1, 0);
        diagWinnerBoard.putPiece(2, 1);
        diagWinnerBoard.putPiece(2, 1);
        diagWinnerBoard.putPiece(2, 0);
        diagWinnerBoard.putPiece(3, 1);
        diagWinnerBoard.putPiece(3, 1);
        diagWinnerBoard.putPiece(3, 1);
        diagWinnerBoard.putPiece(3, 0);
        diagWinnerBoard.putPiece(4, 1);
        diagWinnerBoard.putPiece(4, 1);
        diagWinnerBoard.putPiece(4, 1);
        diagWinnerBoard.putPiece(4, 0);
        diagWinnerBoard.putPiece(4, 1);
        diagWinnerBoard.putPiece(4, 0);
        noWinner = new GameBoard(NUMROWS, NUMCOLUMNS);
        //create a no winner board
        noWinner.putPiece(0, 0);
        noWinner.putPiece(1, 1);
        noWinner.putPiece(2, 1);
        noWinner.putPiece(3, 1);
        noWinner.putPiece(4, 0);
        noWinner.putPiece(3,1);
        noWinner.putPiece(0, 0);
        noWinner.putPiece(0, 0);
        noWinner.putPiece(1, 0);
        noWinner.putPiece(5, 0);

        scoreChecker = new ScoreChecker();
    }

    @After
    public void tearDown() throws Exception {
        vertWinnerBoard = null;
        horiWinnerBoard = null;
        diagWinnerBoard = null;
        noWinner = null;
    }

    @Test
    public void WinningGameBoardHasWinner() throws Exception {
        assertEquals(scoreChecker.gameHasWinner(vertWinnerBoard, 5, 0), true);
    }

    @Test
    public void VerticalWinningBoardHasWinner() throws Exception{
        assertEquals(scoreChecker.hasVertWinner(vertWinnerBoard, 5, 0), true);
    }

    @Test
    public void NoVerticalWinnerOnHorizontalWinnerBoard() throws Exception{
        assertEquals(scoreChecker.hasVertWinner(horiWinnerBoard,5,0), false);
    }

    @Test
    public void HorizontalWinningBoardHasWinner() throws Exception{
        assertEquals(scoreChecker.hasHorizWinner(horiWinnerBoard, 5, 0), true);
    }

    @Test
    public void NoHorizontalWinnerOnVerticalWinnerBoard() throws Exception{
        assertEquals(scoreChecker.hasHorizWinner(vertWinnerBoard,5,0), false);
    }

    @Test
    public void DiagonalWinningBoardHasWinner() throws Exception{
        assertEquals(scoreChecker.hasDiagWinner(diagWinnerBoard, 5, 0), true);
    }

    @Test
    public void NoDiagonalWinnerOnVerticalWinnerBoard() throws Exception{
        assertEquals(scoreChecker.hasDiagWinner(vertWinnerBoard,5,0), false);
    }

    @Test
    public void NoWinnerGameBoardDoesNotHaveWinner() throws Exception{
        assertEquals(scoreChecker.gameHasWinner(noWinner, 4, 3), false);
        assertEquals(scoreChecker.gameHasWinner(noWinner, 4, 1), false);
    }

    @Test
    public void VertWinnerBoardHas4Vertically() throws Exception {
        assertEquals(scoreChecker.navigateAndCount(vertWinnerBoard, 5, 0, "vertical"), 4);
    }

    @Test
    public void DiagBoardHas3ConsecutiveAtRow3Col3() throws Exception{
        assertEquals(scoreChecker.navigateAndCount(diagWinnerBoard, 3, 3, "diagonal"), 3);
    }


}