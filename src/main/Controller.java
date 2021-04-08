package main;

import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import networking.NetworkHandlerSingleton;
import gameLogic.GameControllerSingleton;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import utils.Piece;

import java.util.HashMap;
import java.util.Map;

public class Controller {

    @FXML
    private TableView<String> chatView;

    @FXML
    private static ObservableList<String> chatMessages;

    @FXML
    public TableColumn<String, String> chatColumn;

    @FXML
    public TextField chatInput;

    @FXML
    private GridPane gameBoard;

    @FXML
    public Button endTurnButton;

    @FXML
    public Button undoButton;

    @FXML
    public Button quitButton;

    @FXML
    public Rectangle statusSquare;

    @FXML
    public Text statusText;

    private GameControllerSingleton gameController;

    // Used to easily get the pieces in the board by their coordinates
    Map<Integer, Map<Integer, Piece>> pieceMap = new HashMap<>();

    // Called once when the controller is created, after the constructor. Initializes the controller itself.
    @FXML
    void initialize() {

        //Initializing the chat
        chatMessages = FXCollections.observableArrayList();
        chatColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        chatView.setItems(chatMessages);

        // Initializing the game controller
        gameController = GameControllerSingleton.getInstance(this);
        endTurnButton.setText("Aguardando");
        endTurnButton.setDisable(true);

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = new Piece(0,10,10, r, c, gameController);
                piece.setFill(Color.TRANSPARENT);
                if (!pieceMap.containsKey(r)) {
                    pieceMap.put(r, new HashMap<>());
                }
                pieceMap.get(r).put(c, piece);
                gameBoard.add(piece, r, c);
                GridPane.setHalignment(piece, HPos.CENTER);
            }
        }
        updateBoard();
    }

    @FXML
    void handleUndoButton() {
        if (gameController.isTurn) {
            gameController.localUndo();
            gameController.sendRemoteUndo();
        }
    }

    @FXML
    void handleEndTurnButton() {
        if (statusSquare.getFill() == Color.GREEN || statusSquare.getFill() == Color.RED) {
            // If the status square has one of these colors, then the game has ended. If this is the the case,
            // then the end turn button becomes a button for restarting the game.
            System.out.println("Trying to restart game");
            GameControllerSingleton.getInstance().restartGame();
            NetworkHandlerSingleton.getHandler().sendGameEventMessageToSender("restart");
        } else if (gameController.isTurn) {
            gameController.endTurn();
        }
    }

    @FXML
    void handleQuitButton() {
        if (statusSquare.getFill() == Color.GREEN || statusSquare.getFill() == Color.RED) return;
        gameController.loseGame();
        NetworkHandlerSingleton.getHandler().sendGameEventMessageToSender("defeat");
    }

    public void setStatusToPlaying() {
        statusSquare.setFill(Color.BLUE);
        statusText.setText("Sua vez de jogar");
    }

    public void setStatusToWaiting() {
        statusSquare.setFill(Color.GREY);
        statusText.setText("Vez do oponente de jogar");
    }

    public void setStatusToVictory() {
        statusSquare.setFill(Color.GREEN);
        statusText.setText("Parabéns, você ganhou!");
        endTurnButton.setDisable(false);
        endTurnButton.setText("Reiniciar jogo");
    }

    public void setStatusToDefeat() {
        statusSquare.setFill(Color.RED);
        statusText.setText("Você perdeu! Mais sorte da próxima!");
        endTurnButton.setDisable(false);
        endTurnButton.setText("Reiniciar jogo");
    }

    // Gets the current state of the game from the game controller, and updates the view accordingly.
    public void updateBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece currentPiece = pieceMap.get(r).get(c);
                char state = gameController.getBoardPosition(r,c);
                if (state == ' ') {
                    currentPiece.setFill(Color.TRANSPARENT);
                } else if (state == 'w') {
                    currentPiece.setFill(Color.GREY);
                } else {
                    currentPiece.setFill(Color.BLACK);
                }
            }
        }
    }

    // This is executed when the user presses enter after typing something into the chat input
    @FXML
    public void handleChatInput () {
        logMessage("Você: " + chatInput.getText());
        NetworkHandlerSingleton.getHandler().sendChatMessageToSender(chatInput.getText());
        chatInput.clear();
    }

    public static void logMessage(String message) {
        chatMessages.add(message);
    }
}
