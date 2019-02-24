package test;

import core.Connect4;
import core.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class Connect4Test {
    private Connect4 connect4;

    private Thread connect4Thread;



    @After
    public void tearDown() throws Exception {
        connect4Thread = null;
        connect4 = null;
    }

    @Test
    public void BadUserInputDefaultsToTextConsole() throws Exception {
        assertFalse(Connect4.isGUI("BAD"));
    }

    @Test
    public void UserProvidesGoodInputSelectsConsoleGetsConsoleGame() throws Exception {
        assertFalse(Connect4.isGUI("C"));
    }

    @Test
    public void UserProvidesGoodInputSelectsGuiGetsGuiGame() throws Exception {
        assertTrue(Connect4.isGUI("G"));
    }

    @Test
    public void NoArgumentsToMainPromptsUserForGameType() throws Exception {

        connect4Thread = new Thread(() -> {
            connect4 = new Connect4();
            connect4.main(new String[] {});
        });
        connect4Thread.start();
    }

    @Test
    public void ConsoleArgumentToMainResultsInNoPrompt() throws Exception {
        connect4Thread = new Thread(() -> {
            connect4 = new Connect4();
            connect4.main(new String[] {"C"});
        });
        connect4Thread.start();
    }

    @Test
    public void GUIArgumentToMainResultsInNoPrompt() throws Exception {
        connect4Thread = new Thread(() -> {
            connect4 = new Connect4();
            connect4.main(new String[] {"G"});
        });
        connect4Thread.start();
    }

    @Test
    public void CanInstantiateConstantsWithoutException() {
        Constants constants = new Constants();
    }

}