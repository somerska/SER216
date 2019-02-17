package test;

import core.GameManager;
import core.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameManagerTest {
    private static GameManager gameManager;

    @Before
    public void setUp() throws Exception {
        gameManager = new GameManager();

    }

    @After
    public void tearDown() throws Exception {
        gameManager = null;
    }

    @Test
    public void SetupPVPMakesTwoHumanPlayers() throws Exception {
        gameManager.setupPvP();
        ArrayList<Player> players = gameManager.getPlayers();
        for (Player player: players) {
            assertEquals(player.playerIsHuman(), true);
        }
    }

    @Test
    public void SetupPVCMakesOneHumanOneComputerPlayer() throws Exception {
        gameManager.setupPvC();
        ArrayList<Player> players = gameManager.getPlayers();
        assertEquals(players.get(0).playerIsHuman(), true);
        assertEquals(players.get(1).playerIsHuman(), false);
    }

    @Test
    public void SetupPVPMakesGamePVP() throws Exception{
        gameManager.setupPvP();
        assertEquals(gameManager.isPlayerVsPlayer(), true);
    }

    @Test
    public void SetupPVCMakesGamePVC() throws Exception{
        gameManager.setupPvC();
        assertEquals(gameManager.isPlayerVsPlayer(), false);
    }

    @Test
    public void SetPlayer0TurnMakesPlayer0Turn() throws Exception{
        gameManager.setupPvP();
        int playerTurn = gameManager.setPlayerTurn(0);
        assertEquals(playerTurn, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void SetPlayer5ThrowsIndexOutOfBoundsException() throws Exception {
        gameManager.setupPvP();
        int playerTurn = gameManager.setPlayerTurn(5);
    }

}