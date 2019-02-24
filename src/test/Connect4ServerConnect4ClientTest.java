package test;

import core.*;
import org.junit.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static org.junit.Assert.*;

public class Connect4ServerConnect4ClientTest {
    private Connect4Server server;
    private Connect4Client c1;
    private Connect4Client c2;
    private Thread serverThread;

    @Before
    public void setUpServer() throws Exception {

        serverThread = new Thread(() -> {
            server = new Connect4Server();
            ServerSocket serverSocket = server.getServerSocket();
            server.start(serverSocket);
        });
        serverThread.start();
    }


    @After
    public void tearDown() throws Exception {
        Thread.sleep(500);
        server.close();
        serverThread = null;
        c1 = null;
        c2 = null;
        server = null;
    }

    @Test
    public void ClientCanConnectHasNoExceptions() throws Exception {
        c1 = new Connect4Client();
        c1.close();
    }

    @Test
    public void ServerCanSetupPvPHasNoExceptions() throws Exception {
        c1 = new Connect4Client();
        c1.sendGameType(Constants.PLAYERVSPLAYER);
        c2 = new Connect4Client();
        c2.sendGameType(Constants.PLAYERVSPLAYER);
        c1.close();
        c2.close();
    }

    @Test
    public void ServerCanSetupPvCHasNoExceptions() throws Exception {
        c1 = new Connect4Client();
        c1.sendGameType(Constants.PLAYERVSCOMP);
        c1.close();
    }

    @Test
    public void PlayerConnectsProvidesBadInputHasNoExceptions() throws Exception {
        c1 = new Connect4Client();
        c1.sendGameType('Z');
        c1.close();
    }

    @Test(expected = NullPointerException.class)
    public void HandleWinnerOrTieThrowsNullPointerWhenStreamsArentInitialized() throws Exception {
        Player player = new Player(0, true);
        Connect4Server.Streams player0Stream = new Connect4Server.Streams();
        Connect4Server.Streams player1Stream = new Connect4Server.Streams();
        Connect4Server.HandleASession session = new Connect4Server.HandleASession(
                player0Stream, player1Stream, true);
        session.handleWinnerOrTie(player, player0Stream, player1Stream, true);
    }

    @Test
    public void PlayersBadInputReturnsBadInputFromServer() throws Exception {
        c1 = new Connect4Client();
        c1.sendGameType(Constants.PLAYERVSPLAYER);
        c2 = new Connect4Client();
        c2.sendGameType(Constants.PLAYERVSPLAYER);
        c1.getGameStatus(); //players number
        c2.getGameStatus(); //players number
        c1.getGameStatus(); //continue

        c1.getGameBoard(); //get gb
        Character servResponse = c1.sendMove(-1); //bad input
        c1.close();
        c2.close();
        assertEquals(servResponse, Constants.BADINPUT);
    }

    @Test
    public void Player0WinsServerReturnsPlayer0IsWinner() throws Exception {
        c1 = new Connect4Client(); //player 0
        c1.sendGameType(Constants.PLAYERVSPLAYER);
        c2 = new Connect4Client(); //player 1
        c2.sendGameType(Constants.PLAYERVSPLAYER);
        c1.getGameStatus(); //players number
        c2.getGameStatus(); //players number
        c1.getGameStatus(); //continue

        int finalStatus = 0;
        for(int i = 0; i < 4; i++){
            c1.getGameBoard();
            c1.sendMove(0);
            if(i != 3){
                c2.getGameStatus();
                c2.getGameBoard();
                c2.sendMove(1);
            }
            finalStatus = c1.getGameStatus();
        }
        c1.close();
        c2.close();
        assertEquals(finalStatus, Constants.PLAYER0_WON);
    }

    @Test
    public void Player1WinsServerReturnsPlayer1IsWinner() throws Exception {
        c1 = new Connect4Client(); //player 0
        c1.sendGameType(Constants.PLAYERVSPLAYER);
        c2 = new Connect4Client(); //player 1
        c2.sendGameType(Constants.PLAYERVSPLAYER);
        c1.getGameStatus(); //players number
        c2.getGameStatus(); //players number
        c1.getGameStatus(); //continue

        int finalStatus = 0;
        for(int i = 0; i < 4; i++){
            c1.getGameBoard();
            if(i % 2 == 0)
                c1.sendMove(0);
            else
                c1.sendMove(1);
            c2.getGameStatus();
            c2.getGameBoard();
            c2.sendMove(4);
            if ( i != 3)
                c1.getGameStatus();
            else
                finalStatus = c2.getGameStatus();
        }
        c1.close();
        c2.close();
        assertEquals(finalStatus, Constants.PLAYER1_WON);
    }
    @Test
    public void ServerHandlesPlayVsCompWithoutExceptionThrown() throws Exception {
        c1 = new Connect4Client(); //player 0
        c1.sendGameType(Constants.PLAYERVSCOMP);
        c1.getGameStatus(); //players number
        c1.getGameStatus(); //continue

        c1.getGameBoard();
        c1.sendMove(0);
        c1.close();
    }
}