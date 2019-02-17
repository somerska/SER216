package core;

import java.io.*;

/**
 * Player class represents a player in the game.  Each player has their own unique id
 * @author Kevin Somers
 */
public class Player implements Serializable{
    private int id;
    private boolean isHuman;


    /**
     * player constructor, initializes the player
     * @param id integer identification number for the player, this is what is used on the backend for the board.
     * @param isHuman boolean representing whether this particular player is human or not
     */
    public Player(int id, boolean isHuman){
        this.id = id;
        this.isHuman = isHuman;
    }

    /**
     * accessor method for the players integer ID
     * @return the players integer id
     */
    public int getID(){
        return this.id;
    }


    /**
     * useful for determining if the player is human or not
     * @return boolean false implies the player is a computer
     */
    public boolean playerIsHuman() {
        return isHuman;
    }

}
