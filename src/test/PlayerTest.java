package test;


import core.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class PlayerTest {
    private static Player p1;
    private static Player p2;


    @org.junit.Before
    public void setUp() throws Exception {
        p1 = new Player(0, true);
        p2 = new Player(1, false);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        p1 = null;
        p2 = null;
    }

    @org.junit.Test
    public void getID() throws Exception {
        assertEquals(p1.getID(), 0);
        assertEquals(p2.getID(), 1);
    }

    @org.junit.Test
    public void playerIsHuman() throws Exception {
        assertEquals(p1.playerIsHuman(), true);
        assertEquals(p2.playerIsHuman(), false);
    }

}