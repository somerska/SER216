package core;


import java.io.*;
import java.net.Socket;

import static core.Constants.NUMCOLUMNS;
import static core.Constants.NUMROWS;

/**
 * Connect4Client is what all clients use to connect to the Connect4 Server.
 * This class creates ObjectInput and ObjectOutput streams for the clients to
 * use for communication with the server.
 * @author Kevin Somers
 */
public class Connect4Client {

    // Input and output streams from/to server
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    /**
     * initialize object or exit if failure to connect to the server.
     */
    public Connect4Client(){
        try {
            Socket socket = new Socket(Constants.HOST, Constants.PORT);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            //NOTICE: Output must be created before Input on both ends
            fromServer = new ObjectInputStream(socket.getInputStream());

        } catch (Exception ex) {
            System.out.println("Could not connect to server, exiting");
            System.exit(1);
        }
    }

    /**
     * sends the game type, either player vs player or player vs computer
     * @param gameType either 'P' or 'C' to indicate PvP or PvC, respectively.
     * @throws IOException throws if there is a failure to communicate with server.
     */
    public void sendGameType(Character gameType) throws IOException{
        toServer.writeChar(gameType);
        toServer.flush();
    }

    /**
     * gets the game status indicating whose turn it is as well as whether the game
     * should continue, has a winner or tie.
     * @return one of these states: player0won, player1won, draw, continue
     * @throws IOException throws if there is a failure to communicate with server.
     */
    public int getGameStatus() throws IOException {
        return fromServer.readInt();
    }

    /**
     * sends a move and expects a character in return indicating if the move was accepted.
     * @param colSelected the move being requested represents the column for the checker
     * @return 'G' implies the move was good or 'B' implies the move was bad
     * @throws IOException in the event of socket failure
     */
    public Character sendMove(int colSelected) throws IOException {
        toServer.writeInt(colSelected);
        toServer.flush();
        return fromServer.readChar();
    }

    /**
     * receive the updated game board from the server
     * @return returns the gameboard recvd over the socket
     * @throws IOException in the event of socket failure
     */
    public GameBoard getGameBoard() throws IOException {
        try {
            return (GameBoard) fromServer.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Could not translate gameboard from server");
        }
        return new GameBoard(NUMROWS, NUMCOLUMNS);
    }

    public void close() throws IOException {
        try{
            toServer.close();
            fromServer.close();
        } catch (IOException ex){
            System.out.println("Failed to safely close sockets");
        }
    }
}
