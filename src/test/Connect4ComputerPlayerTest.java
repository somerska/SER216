package test;

import core.Connect4ComputerPlayer;
import core.Constants;
import core.GameBoard;
import core.ScoreChecker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Connect4ComputerPlayerTest {
    private Connect4ComputerPlayer computerPlayer;
    private GameBoard gameBoard;
    private ScoreChecker scoreChecker;

    @Before
    public void setUp() throws Exception {
        computerPlayer = new Connect4ComputerPlayer(0, false);
        gameBoard = new GameBoard(Constants.NUMROWS, Constants.NUMCOLUMNS);
        scoreChecker = new ScoreChecker();
    }

    @After
    public void tearDown() throws Exception {
        computerPlayer = null;
        gameBoard = null;
        scoreChecker = null;
    }

    @Test
    public void ComputerGeneratesRandomMoveWithinBoardBounds() throws Exception {
        int col = computerPlayer.getMove(gameBoard, scoreChecker);
        assertTrue(col < gameBoard.getColLength());
        assertTrue(col >= 0);
    }

}