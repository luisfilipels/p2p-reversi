package gameLogic;

import javafx.stage.Stage;
import main.Controller;
import networking.NetworkHandlerSingleton;
import utils.CoordinatePair;
import utils.SessionDataSingleton;

import java.util.*;

public class GameControllerSingleton {

    private static GameControllerSingleton instance;

    private Controller viewController;

    // The working board is the board used to display information to the player
    private char[][] workingBoard = new char[8][8];

    // The undo board is used as a backup for when the user undoes his movement, or when the client
    // receives the undo message from the other player's client.
    private char[][] undoBoard = new char[8][8];

    // Variable used to stop the user from acting when it's not their turn
    public boolean isTurn = false;

    // Used to have  a reference to other stages. When the other player wants to restart the game, with
    // this list it's possible to remotely close any other popup window that is open
    private List<Stage> openStages = new ArrayList<>();

    private GameControllerSingleton(Controller controller) {
        this.viewController = controller;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                workingBoard[r][c] = ' ';
                undoBoard[r][c] = ' ';
            }
        }
        // Initial configuration of the game
        workingBoard[3][3] = 'w';
        workingBoard[3][4] = 'b';
        workingBoard[4][3] = 'b';
        workingBoard[4][4] = 'w';
        undoBoard[3][3] = 'w';
        undoBoard[3][4] = 'b';
        undoBoard[4][3] = 'b';
        undoBoard[4][4] = 'w';
    }

    public Controller getViewController() {
        return viewController;
    }

    public static GameControllerSingleton getInstance(Controller controller) {
        if (instance == null) {
            instance = new GameControllerSingleton(controller);
        }
        return instance;
    }

    public static GameControllerSingleton getInstance() {
        if (instance == null) {
            System.out.println("Error! GameController has not been created!");
            return null;
        }
        return instance;
    }

    // Resets the working board to the state it was when the player's round began
    public void resetWorkingBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                workingBoard[r][c] = undoBoard[r][c];
            }
        }
        viewController.updateBoard();
    }

    // Overwrites the undo board with the working board. Used when a round is ending.
    public void saveWorkingBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                undoBoard[r][c] = workingBoard[r][c];
            }
        }
    }

    // Resets both boards to the initial configuration. Used when the game is reset.
    private void hardResetAllBoards() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                workingBoard[r][c] = ' ';
                undoBoard[r][c] = ' ';
            }
        }
        workingBoard[3][3] = 'w';
        workingBoard[3][4] = 'b';
        workingBoard[4][3] = 'b';
        workingBoard[4][4] = 'w';
        undoBoard[3][3] = 'w';
        undoBoard[3][4] = 'b';
        undoBoard[4][3] = 'b';
        undoBoard[4][4] = 'w';
        viewController.updateBoard();
    }

    private char checkBoardFullVictory() {
        int[] count = getColorCount();
        if (count[0] + count[1] == 64) {
            if (count[0] == count[1]) return 't';
            if (count[0] > count[1]) return 'w';
            else return 'b';
        } else return ' ';
    }

    private int[] getColorCount() {
        int countB = 0;
        int countW = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (workingBoard[r][c] == 'w') countW++;
                if (workingBoard[r][c] == 'b') countB++;
            }
        }
        return new int[] {countW, countB};
    }

    private boolean nextPlayerCanMove() {
        char myColor = SessionDataSingleton.getInstance().getUserColor();
        char otherColor = myColor == 'w' ? 'b' : 'w';
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (workingBoard[r][c] != ' ') continue;
                for (int deltaR = -1; deltaR <= 1; deltaR++) {
                    for (int deltaC = -1; deltaC <= 1; deltaC++) {
                        if (deltaC == 0 && deltaR == 0) continue;
                        if (findOtherPiece(r, c, deltaR, deltaC, otherColor)) return true;
                    }
                }
            }
        }
        return false;
    }

    // Called after every turn ends. Checks if one or the other player won the game.
    private char checkVictory() {
        char boardFullVictory = checkBoardFullVictory();
        if (boardFullVictory != ' ') return boardFullVictory;

        if (nextPlayerCanMove()) return ' ';
        else {
            int[] count = getColorCount();
            if (count[0] == count[1]) return 't';
            if (count[0] > count[1]) return 'w';
            else return 'b';
        }
    }

    public void localUndo() {
        resetWorkingBoard();
    }

    public void sendRemoteUndo() {
        NetworkHandlerSingleton.getHandler().sendGameEventMessageToSender("undo");
    }

    public void endTurn() {
        isTurn = false;
        viewController.endTurnButton.setText("Aguardando");
        viewController.endTurnButton.setDisable(true);
        saveWorkingBoard();
        char victorious = checkVictory();
        if (victorious == ' ') {
            viewController.setStatusToWaiting();
            NetworkHandlerSingleton.getHandler().sendGameEventMessageToSender("endturn");
        }
        else {
            if (victorious == 't') {
                tieGame();
                NetworkHandlerSingleton.getHandler().sendGameEventMessageToSender("tie");
                return;
            }
            char myColor = SessionDataSingleton.getInstance().getUserColor();
            if (victorious == myColor) {
                winGame();
                NetworkHandlerSingleton.getHandler().sendGameEventMessageToSender("victory");
            } else {
                loseGame();
                NetworkHandlerSingleton.getHandler().sendGameEventMessageToSender("defeat");
            }
        }
    }

    public void startTurn() {
        isTurn = true;
        viewController.endTurnButton.setDisable(false);
        viewController.endTurnButton.setText("Encerrar rodada");
        viewController.setStatusToPlaying();
    }

    public void loseGame() {
        viewController.setStatusToDefeat();
    }

    public void winGame() { viewController.setStatusToVictory(); }

    public void tieGame() { viewController.setStatusToTie(); }

    public void closeAllPopups() {
        for (Stage s : openStages) {
            if (s != null) s.close();
        }
        openStages.clear();
    }

    public void restartGame() {
        hardResetAllBoards();
        if (SessionDataSingleton.getInstance().getUserColor() == 'w') {
            isTurn = true;
            viewController.endTurnButton.setDisable(false);
            viewController.endTurnButton.setText("Encerrar rodada");
            viewController.setStatusToPlaying();
        } else {
            isTurn = false;
            viewController.endTurnButton.setDisable(true);
            viewController.endTurnButton.setText("Aguardando");
            viewController.setStatusToWaiting();
        }
    }

    public char getBoardPosition (int r, int c){
        return workingBoard[r][c];
    }

    // Forces a board position to have a certain color, whether it is or not the player's turn.
    // Used by the Receiver class when a movement message arrives
    public void forceSetBoardPosition(int r, int c, char color) {
        workingBoard[r][c] = color;
        viewController.updateBoard();
    }

    // Checks if, given a starting position and a direction (expressed by deltaR and deltaC), it's possible
    // to fill, from startR and startC to another board position with the same expected color, all pieces in
    // between them. If there is no other board piece in that direction (or if an empty position is found before),
    // such piece is found), then return false.
    private boolean findOtherPiece(int startR, int startC, int deltaR, int deltaC, char expectedColor) {
        int r = startR + deltaR, c = startC + deltaC;
        char otherColor = expectedColor == 'w' ? 'b' : 'w';
        int countOtherColor = 0;
        while (r >= 0 && r < 8 && c >= 0 && c < 8) {
            if (workingBoard[r][c] == otherColor) countOtherColor++;
            if (workingBoard[r][c] == ' ') return false;
            if (workingBoard[r][c] == expectedColor && countOtherColor >= 1) return true;
            r += deltaR;
            c += deltaC;
        }
        return false;
    }

    // Called when findOtherPiece is true. Adds all coordinates to a hashset, for posterior use in changing
    // their pieces to the correct color.
    private void setPositions(int startR, int startC, int deltaR, int deltaC, char setColor, HashSet<CoordinatePair> result) {
        int r = startR + deltaR, c = startC + deltaC;
        while (r >= 0 && r < 8 && c >= 0 && c < 8) {
            if (workingBoard[r][c] == setColor) return;
            result.add(new CoordinatePair(r, c));
            r += deltaR;
            c += deltaC;
        }
    }

    // Creates a hashset that contains all positions whose colors can be changed if the player puts a piece
    // in the (startR, startC) position.
    private HashSet<CoordinatePair> autoSetPositions(int startR, int startC) {
        SessionDataSingleton userData = SessionDataSingleton.getInstance();
        char color = userData.getUserColor();
        HashSet<CoordinatePair> returnSet = new HashSet<>();

        for (int deltaR = -1; deltaR <= 1; deltaR++) {
            for (int deltaC = -1; deltaC <= 1; deltaC++) {
                if (deltaC == 0 && deltaR == 0) continue;
                if (findOtherPiece(startR, startC, deltaR, deltaC, color)) {
                    setPositions(startR, startC, deltaR, deltaC, color, returnSet);
                }
            }
        }

        return returnSet;
    }

    // Sets a board position when it's the player's turn. Also takes extra measures if the player wanted
    // aid in putting pieces on the board.
    public void setBoardPosition(int r, int c) {
        SessionDataSingleton userData = SessionDataSingleton.getInstance();
        if (isTurn) {
            if (userData.isAidActivated()) {
                if (workingBoard[r][c] != ' ') return;
                localUndo();
                HashSet<CoordinatePair> result = autoSetPositions(r, c);
                if (result.size() > 0) {
                    try {
                        sendRemoteUndo();
                        Thread.sleep(20);
                        sendRemoteUndo();
                        System.out.println("Setting " + result.size() + " positions");
                        workingBoard[r][c] = userData.getUserColor();
                        NetworkHandlerSingleton.getHandler().sendGameMoveMessageToSender(r,c);
                        Thread.sleep(60);
                        NetworkHandlerSingleton.getHandler().sendGameMoveMessageToSender(r,c);
                        for (CoordinatePair pair : result) {
                            Thread.sleep(60);
                            NetworkHandlerSingleton.getHandler().sendGameMoveMessageToSender(pair.r, pair.c);
                            workingBoard[pair.r][pair.c] = userData.getUserColor();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                workingBoard[r][c] = userData.getUserColor();
                NetworkHandlerSingleton.getHandler().sendGameMoveMessageToSender(r,c);
            }
            viewController.updateBoard();
        }
    }

}
