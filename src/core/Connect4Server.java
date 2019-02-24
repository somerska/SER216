package core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import static core.Constants.*;

/**
 * Connect4Server is the class that creates a server ready to accept socket connections
 * and pair players up to play games of connect 4.
 * @author Kevin Somers
 */
public class Connect4Server {

    private ServerSocket serverSocket = null;
    private int sessionNo = 1; // Number a session
    private ArrayList<Thread> connectedThreads;
    private boolean continueRunning;


    /**
     * Entry point for the class
     * @param args not used here.
     */
    public static void main(String[] args){
        Connect4Server server = new Connect4Server();
        ServerSocket socket = server.getServerSocket();
        server.start(socket);
    }

    public Connect4Server(){
        connectedThreads = new ArrayList<>();
        continueRunning = true;
        // Create a server socket
        try {
            serverSocket = new ServerSocket(Constants.PORT);
            System.out.println(new Date() + ": Server started at socket " + Constants.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket(){
        return serverSocket;
    }

    /**
     * Represents a players collective in and out object streams.
     */
    public static class Streams{
        public ObjectOutputStream out;
        public ObjectInputStream in;
        public void setInStream(ObjectInputStream in){
            this.in = in;
        }
        public void setOutStream(ObjectOutputStream out){
            this.out = out;
        }
    }

    /**
     * handles all incoming connections.
     * When a connection comes in it will launch a new thread for the game session.
     * @param server the ServerSocket to use for accepting connections
     */
    public void start(ServerSocket server) {
        while (continueRunning){
            if (server == null)
                break;
            // Ready to create a session for every two players

            System.out.println((new Date() + ": Wait for players to join session " + sessionNo));
            try {
                Streams player0PvP = loopUntilPvP(server, 1);
                Streams player1PvP = loopUntilPvP(server, 2);
                // Display this session and increment session number
                System.out.println(new Date() + ": Starting a thread for session " + sessionNo++);

                // Launch a new thread for this session of two human players
                Thread thread = new Thread(new HandleASession(player0PvP, player1PvP, true));
                connectedThreads.add(thread);
                thread.start();
            } catch(IOException | NullPointerException ex){
                System.out.println("Player has disconnected in match making");
            }
        }
        close();
    }

    /**
     * stop main server thread, stop all threads dedicated to handling a session.
     */
    public void close(){
        continueRunning = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(Thread thread: connectedThreads){
            if (thread != null)
                thread = null;
        }
        serverSocket = null;
    }

    /**
     * If a player connects and requests player vs computer, this method will start a new thread
     * for them to play immediately.  Method will not return until it has a player connect and that player
     * requests Player vs player.
     * @param playerNumber the player number that is connecting
     * @return Streams obj which is a container for in/out streams of the player
     */
    private Streams loopUntilPvP(ServerSocket server, int playerNumber)
            throws IOException, NullPointerException{

        boolean hasPlayerVsPlayer = false;
        Streams playerStreams = new Streams();

        //continue to loop until two players that are interested in player vs player have connected
        while(!hasPlayerVsPlayer && server != null){
            Socket player0 = server.accept();

            System.out.println(new Date() + ": Player " + playerNumber + "  joined session "
                    + sessionNo + '\n' + "Player " + playerNumber + "'s IP address " +
                    player0.getInetAddress().getHostAddress());

            //setup object streams for tcp communication
            ObjectOutputStream out = new ObjectOutputStream(player0.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(player0.getInputStream());
            playerStreams.setInStream(in);
            playerStreams.setOutStream(out);

            //get gameType from player
            Character gameType = in.readChar();
            if (gameType == PLAYERVSCOMP){
                System.out.println(new Date() + ": Player " + playerNumber +  "  requested Player vs Computer");
                System.out.println(new Date() + ": Starting a thread for session " + sessionNo++);
                //start game with player vs computer immediately
                Thread thread = new Thread(new HandleASession(playerStreams, new Streams(), false));
                connectedThreads.add(thread);
                thread.start();
                //new Thread(new HandleASession(playerStreams, new Streams(), false)).start();
            } else if (gameType == PLAYERVSPLAYER) {
                System.out.println(new Date() + ": Player " + playerNumber + " requested Player vs Player");
                break;
            } else{ //user did not provide 'P' or 'C'
                out.writeChar(BADINPUT);
                out.flush();
            }
        }
        return playerStreams;
    }

    /**
     * Handles a single session of Connect4.  This class is used for both player vs player and
     * player vs computer.
     */
    public static class HandleASession implements Runnable {
        private Streams player0Stream;
        private Streams player1Stream;
        private GameBoard gameBoard;
        private GameManager gameManager;
        private ScoreChecker scoreChecker;
        private boolean continueToPlay = true;

        /**
         * Setup the game session
         * @param player0Stream first players socket information
         * @param player1Stream second players socket information
         * @param playerVsPlayer true if this is a player vs player game, false otherwise
         */
        public HandleASession(Streams player0Stream, Streams player1Stream, boolean playerVsPlayer) {
            this.player0Stream = player0Stream;
            this.player1Stream = player1Stream;

            // Initialize game
            this.gameBoard = new GameBoard(NUMROWS, NUMCOLUMNS);
            this.scoreChecker = new ScoreChecker();
            this.gameManager = new GameManager();
            if (playerVsPlayer){
                gameManager.setupPvP();
            } else{
                gameManager.setupPvC();
            }
            gameManager.setPlayerTurn(0);
        }

        /**
         * Handles the coordination and game logic for the two connected players.
         */
        @SuppressWarnings("Duplicates")
        public void run() {
            try {
                Player player1 = gameManager.getPlayers().get(0);
                Player player2 = gameManager.getPlayers().get(1);
                try {
                    this.player0Stream.out.writeInt(player1.getID());
                    this.player0Stream.out.flush();
                    if(gameManager.isPlayerVsPlayer()){
                        this.player1Stream.out.writeInt(player2.getID());
                        this.player1Stream.out.flush();
                    }
                } catch (IOException | NullPointerException ex){
                    System.out.println("player disconnected while match was in progress");
                    continueToPlay = false;
                }

                while(continueToPlay){
                    int column = 0;
                    int row = 0;
                    Player curPlayer = gameManager.getPlayers().get(0);
                    player0Stream.out.writeInt(CONTINUE);
                    player0Stream.out.writeObject(gameBoard);
                    player0Stream.out.flush();

                    //loop until valid input is provided
                    boolean moveIsGood = false;
                    while(!moveIsGood){
                        column = player0Stream.in.readInt();
                        if( (gameBoard.isOutOfBounds(column)) || (gameBoard.colIsFull(column)) ){
                            player0Stream.out.writeChar(BADINPUT);
                            player0Stream.out.flush();
                            continue;
                        } else {
                            player0Stream.out.writeChar(GOODINPUT);
                            player0Stream.out.flush();
                            row = gameBoard.putPiece(column, curPlayer.getID());
                            moveIsGood =true;
                        }
                    }
                    gameBoard.decrementSpot();
                    player0Stream.out.flush();
                    player0Stream.out.reset(); //very important for ensuring the GameBoard is not cached.

                    //check for a winner of the current player
                    if(scoreChecker.gameHasWinner(gameBoard, row, column)){
                        handleWinnerOrTie(curPlayer, player0Stream, player1Stream, true);
                        continueToPlay = false;
                        break;
                    } else if (gameBoard.getSpots() == 0) { // check for tie
                        handleWinnerOrTie(curPlayer, player0Stream, player1Stream, false);
                        continueToPlay = false;
                        break;
                    }
                    //-------END HANDLING FIRST PLAYER ---------------
                    //set current player to the second player
                    curPlayer = gameManager.getPlayers().get(1);
                    //get player input handling both computer and human players
                    if(!curPlayer.playerIsHuman()){
                        //get the computers input and update the board
                        column = ((Connect4ComputerPlayer) curPlayer).getMove(gameBoard, scoreChecker);
                        row = gameBoard.putPiece(column, curPlayer.getID());
                    } else { //human player
                        curPlayer = gameManager.getPlayers().get(1);
                        player1Stream.out.writeInt(CONTINUE);
                        player1Stream.out.writeObject(gameBoard);
                        player1Stream.out.flush();

                        //loop until valid input is provided
                        moveIsGood = false;
                        while(!moveIsGood){
                            column = player1Stream.in.readInt();
                            if( (gameBoard.isOutOfBounds(column)) || (gameBoard.colIsFull(column)) ){
                                player1Stream.out.writeChar(BADINPUT);
                                player1Stream.out.flush();
                                continue;
                            } else {
                                player1Stream.out.writeChar(GOODINPUT);
                                player1Stream.out.flush();
                                row = gameBoard.putPiece(column, curPlayer.getID());
                                moveIsGood = true;
                            }
                        }
                        player1Stream.out.flush();
                        player1Stream.out.reset();
                    }

                    gameBoard.decrementSpot();
                    //check for winner or tie and if so end the game
                    if(scoreChecker.gameHasWinner(gameBoard, row, column)){
                        handleWinnerOrTie(curPlayer, player0Stream, player1Stream, true);
                        continueToPlay = false;
                        break;
                    } else if (gameBoard.getSpots() == 0) { // check for tie
                        handleWinnerOrTie(curPlayer, player0Stream, player1Stream, false);
                        continueToPlay = false;
                        break;
                    }
                    //-------------END HANDLING SECOND PLAYER-------------
                }

            } catch (IOException | NullPointerException ex) {
                System.out.println("player disconnected while match was in progress");
            }
        }


        /**
         * For a winner or tie situation this method will write out who the winner was (or announce both players
         * have tied), in both scenarios it will send out the game board after the announcement.
         * @param currentPlayer player that is going to be announced as the winner (if there's a winner)
         * @param player0 the socket connections for the first player
         * @param player1 the socket connections for the second player
         * @param isWinner if this is false then announce a tie
         * @throws IOException if player disconnects
         * @throws NullPointerException if player disconnects
         */
        public void handleWinnerOrTie(Player currentPlayer, Streams player0, Streams player1, boolean isWinner)
                throws IOException, NullPointerException {
            int winnerID = (currentPlayer.getID() == 0) ? PLAYER0_WON : PLAYER1_WON;
            int number_to_announce = (isWinner) ? winnerID : DRAW;
                if (gameManager.isPlayerVsPlayer()){
                    player0.out.writeInt(number_to_announce);
                    player1.out.writeInt(number_to_announce);
                    player0.out.writeObject(gameBoard);
                    player1.out.writeObject(gameBoard);
                    player0Stream.out.flush();
                    player1Stream.out.flush();
                } else {
                    player0.out.writeInt(number_to_announce);
                    player0.out.writeObject(gameBoard);
                    player0Stream.out.flush();
                }
        }
    }
}