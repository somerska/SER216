package ui;

import core.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import java.io.IOException;

import static core.Constants.*;


/**
 * This class is responsible for displaying a GUI version of the game
 * @author Kevin Somers
 */

public class Connect4GUI extends Application {
    private Slot[][] slots = new Slot[Constants.NUMROWS][Constants.NUMCOLUMNS];
    private Label playerStatusLabel = new Label("");
    private Label gameStatusLabel = new Label("");
    private GridPane checkerGrid;
    private GridPane buttonGrid;
    private HBox gameTypeBox;
    private RadioButton rbPvP;
    private RadioButton rbPvC;
    private ToggleGroup radioGroup;
    private Connect4Client client;
    private int playerId;
    private String playerColor;
    private int winnerId;
    private boolean otherPlayerConnected = false;
    private boolean yourTurn = false;
    private boolean continueToPlay = true;
    private boolean waiting = true;
    private GameBoard gameBoard;

    public static void main(String[] args){
        launch(args);
    }


    public Connect4GUI(){
        this.client = new Connect4Client();
    }

    /**
     * Entry point for the javaFX application, uses a single stage
     * @param primaryStage the single state the application utilizes to display the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        //setup the buttons that allow the user to select which column they want to put the checker in
        buttonGrid = new GridPane();
        for(int i = 0; i < Constants.NUMCOLUMNS; i++){
            Pane pane = new Pane();
            pane.setPrefSize(2000, 2000);
            IdButton btn = new IdButton("Column" + (i+1), i);
            btn.setOnAction(event -> addChecker(event));
            pane.getChildren().add(btn);
            buttonGrid.add(pane, i, 0);
        }

        //setup the checker grid
        checkerGrid = new GridPane();
        for (int i = 0; i < Constants.NUMROWS; i++)
            for (int j = 0; j < Constants.NUMCOLUMNS; j++)
                checkerGrid.add(slots[i][j] = new Slot(), j, i);

        //Setup game type box to select player vs player or computer vs player
        gameTypeBox = new HBox();
        Button submitGameTypeBtn = new Button("Submit Game Type");
        radioGroup = new ToggleGroup();
        rbPvP = new RadioButton("Player vs Player");
        rbPvP.setToggleGroup(radioGroup);
        rbPvP.setSelected(true);
        rbPvC = new RadioButton("Player vs Computer");
        rbPvC.setToggleGroup(radioGroup);
        gameTypeBox.getChildren().add(rbPvP);
        gameTypeBox.getChildren().add(rbPvC);
        gameTypeBox.getChildren().add(submitGameTypeBtn);
        gameTypeBox.setMargin(rbPvP, new Insets(20,20,20,20));
        gameTypeBox.setMargin(rbPvC, new Insets(20,20,20,20));
        gameTypeBox.setMargin(submitGameTypeBtn, new Insets(20,20,20,20));
        submitGameTypeBtn.setOnAction(event -> requestGameType());

        //box for the labels, these print out text messages to show game or player status.
        HBox labelBox = new HBox();
        labelBox.getChildren().add(playerStatusLabel);
        labelBox.getChildren().add(gameStatusLabel);
        playerStatusLabel.setPadding(new Insets(10, 10, 10, 10));
        gameStatusLabel.setPadding(new Insets(10,10,10,10));

        //create the final layout that will contain the higher level components
        VBox outerMostLayout = new VBox();
        outerMostLayout.getChildren().add(labelBox);
        outerMostLayout.getChildren().add(gameTypeBox);
        outerMostLayout.getChildren().add(buttonGrid);
        outerMostLayout.getChildren().add(checkerGrid);
        outerMostLayout.setMargin(buttonGrid, new Insets(0,0,40,0));


        //to start, do not show the grid or column buttons, user must select a gametype first
        checkerGrid.setVisible(false);
        buttonGrid.setVisible(false);
        gameStatusLabel.setText("Select Game Type");

        //set the scene, title, min width/height and show
        Scene scene = new Scene(outerMostLayout, 525, 400);
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(480);
        primaryStage.setMinHeight(300);
        primaryStage.show();

        beginGame();
    }

    /**
     * starts the game main loop
     */
    private void beginGame() {
        // Control the game on a separate thread
        new Thread(() -> {
            try {
                // Get notification from the server
                int id = client.getGameStatus();
                if(id == 0){
                    playerId = PLAYER0;
                    playerColor = Constants.PLAYER0COLOR;
                    winnerId = Constants.PLAYER0_WON;
                    Platform.runLater(() -> {
                        gameStatusLabel.setText("You are player 1, " + playerColor);
                    });
                } else {
                    playerId = Constants.PLAYER1;
                    playerColor = Constants.PLAYER1COLOR;
                    winnerId = Constants.PLAYER1_WON;
                    Platform.runLater(() -> {
                        gameStatusLabel.setText("You are player 2, " + playerColor + " waiting for other play to go");
                    });
                }


                int gameStatus = client.getGameStatus();
                checkerGrid.setVisible(true);
                buttonGrid.setVisible(true);
                gameBoard = client.getGameBoard();
                // It is my turn
                yourTurn = true;

            }
            catch (Exception ex) {
                    ex.printStackTrace();
            }
        }).start();
    }
    /** Receive info from the server */
    private void receiveInfoFromServer() throws IOException {
        // Receive game status
        int status = client.getGameStatus();

        if (status == PLAYER0_WON) {
            String statusText;
            // Player 1 won, stop playing
            continueToPlay = false;
            if (playerId == 0)
            {
                statusText = "You won!";
            } else {
                statusText = "You lost!";
            }
            Platform.runLater(() -> {
                gameStatusLabel.setText(statusText);
            });
        }
        else if (status == PLAYER1_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
            String statusText;
            if (playerId == 1)
            {
                statusText = "You won!";
            } else {
                statusText = "You lost!";
            }
            Platform.runLater(() -> {
                gameStatusLabel.setText(statusText);
            });
        }
        else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            gameStatusLabel.setText("game ends in a draw!");
        }
        else {
            gameBoard = client.getGameBoard();
            updateBoard(gameBoard);
            yourTurn = true; // It is my turn
        }
    }


    private void updateBoard(GameBoard gameBoard){
        for(int i = 0; i < gameBoard.getRowLength(); i++){
            for (int j = 0; j < gameBoard.getColLength(); j++){
                if (gameBoard.slotIsTaken(i, j))
                    slots[i][j].drawChecker(((gameBoard.getPos(i,j) == PLAYER0) ? Color.RED : Color.BLACK));
            }
        }
    }

    /**
     * Determines the game type (player vs player or player vs computer) by reading the radio button selection
     * Uses the gameManager to setup the appropriate game type, sets the starting player to player 0
     * stops displaying the gameTypeBox (radio buttons and submit button) since they're no longer needed
     * and displays the checkerGrid and buttonGrid to begin the game.
     */
    private void requestGameType() {
        new Thread(() -> {
            RadioButton selectedRb = (RadioButton) radioGroup.getSelectedToggle();
            if (selectedRb == rbPvP) {
                try {
                    client.sendGameType('P');
                } catch (IOException exc) {
                    Platform.runLater(() -> gameStatusLabel.setText("Unable to send server game type selected, exiting . . ."));
                    //sleep long enough for user to see message
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(1);
                }
            } else if (selectedRb == rbPvC) {
                try {
                    client.sendGameType('C');
                } catch (IOException exc) {
                    Platform.runLater(() -> gameStatusLabel.setText("Unable to send server game type selected, exiting . . ."));
                    System.exit(1);
                    //sleep long enough for user to see message
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //hide the gameTypeBox since it's no longer needed
            gameTypeBox.setVisible(false);
            gameTypeBox.setManaged(false);
            Platform.runLater(() -> gameStatusLabel.setText("Waiting for other player to connect . . ."));
        }).start();
    }

    /**
     * This method is called when one of the column buttons is clicked, the event is from the
     * specific button clicked.  If the game is still going and the column selected is not full
     * then the checker of the current player is placed in that column.  If this game is a
     * player vs computer, then the computer immediately goes after the player.
     * @param event event triggered when a column button is pushed
     */
    private void addChecker(ActionEvent event){
        if (!yourTurn) {
            return;
        }

            try {
                IdButton button = (IdButton) event.getSource();
                int col = button.getID();
                Character servResponse = client.sendMove(col);
                if (servResponse == GOODINPUT) {
                    int row = gameBoard.putPiece(col, playerId);
                    Color color = (playerColor == "red") ? Color.RED : Color.BLACK;
                    slots[row][col].drawChecker(color);
                    yourTurn = false; // Just completed a successful move
                    Platform.runLater(() -> {
                        updateBoard(gameBoard);
                    });
                    receiveInfoFromServer();
                    yourTurn = true;
                    gameStatusLabel.setText("");
                    return;
                } else if (servResponse == BADINPUT) {
                    gameStatusLabel.setText("bad choice, select a different column");
                    return;
                }
            } catch (IOException ex) {
                System.out.println("lost connection, exiting . . . ");
                System.exit(1);
            }

    }

    /**
     * IdButton is a button that also has an id.  The id is used for knowing which
     * button was clicked.  These buttons are used for selecting a column.
     */
    public class IdButton extends Button {
        private int id;

        public IdButton(String text, int id) {
            setText(text);
            this.id = id;
        }
        public int getID(){
            return id;
        }
    }

    /**
     * Slots are used in each cell of the checkerGrid.  The slots represent a spot where
     * a player's checker can be drawn.
     */
    public class Slot extends Pane {
        public Slot() {
            setStyle("-fx-border-color: black");
            this.setPrefSize(2000, 2000);
        }
        /**
         * Draws the checker provided based on the color
         * @param color of the checker to be drawn
         */
        public void drawChecker(Color color) {
            Circle circle = new Circle(this.getWidth() / 2);
            circle.centerXProperty().bind(this.widthProperty().divide(2));
            circle.centerYProperty().bind(this.heightProperty().divide(2));
            circle.radiusProperty().bind(this.heightProperty().divide(2).subtract(10));
            circle.setStroke(Color.BLACK);
            circle.setFill(color);
            getChildren().add(circle);
        }
    }
}



