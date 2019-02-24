package core;


import java.util.ArrayList;

/**
 * This class is responsible for maintaining the players: keeping track of the number
 * and type of players as well as whether the game is player vs player or player
 * vs computer.
 * @author Kevin Somers
 */
public class GameManager {
    private ArrayList<Player> players;
    private int playerTurn = -1;
    private boolean isPlayerVsPlayer;

    /**
     * Initialize the players and game
     */
    public GameManager(){
        this.players = new ArrayList<>();
    }

    /**
     * Setup a player vs player match by adding two human players
     */
    public void setupPvP(){
        this.players.add(new Player(0, true));
        this.players.add(new Player(1, true));
        isPlayerVsPlayer = true;
    }

    /**
     * Setup a player vs computer match by adding 1 human and 1 computer player
     */
    public void setupPvC(){
        this.players.add(new Player(0, true));
        this.players.add(new Connect4ComputerPlayer(1, false));
        isPlayerVsPlayer = false;
    }

    /**
     * set which player's turn it is
     * @param playerID the id of the player to set as having the current turn
     * @throws IndexOutOfBoundsException if an invalid id is supplied ex is thrown
     * @return returns the new players turn (an id)
     */
    public int setPlayerTurn(int playerID) throws IndexOutOfBoundsException{
        if (playerID < 0 || playerID >= this.players.size())
            throw new IndexOutOfBoundsException("playerID must be within bounds of the list of players!");
        this.playerTurn = playerID;
        return this.playerTurn;
    }

    /**
     * gets all players in the game
     * @return list of all players
     */
    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    /**
     * gets game type, either play vs player or player vs computer
     * @return true if the game is player vs player otherwise false
     */
    public boolean isPlayerVsPlayer(){
        return isPlayerVsPlayer;
    }
}