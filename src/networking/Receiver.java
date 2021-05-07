package networking;

import gameLogic.GameControllerSingleton;
import javafx.application.Platform;
import main.Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Receiver extends UnicastRemoteObject implements GameInterface {

    Receiver() throws RemoteException {
        super();
    }

    @Override
    public void endTurn() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameControllerSingleton gameController = GameControllerSingleton.getInstance();
                gameController.saveWorkingBoard();
                gameController.startTurn();
            }
        });
    }

    @Override
    public void makeMove(int r, int c, char color) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameControllerSingleton gameController = GameControllerSingleton.getInstance();
                gameController.forceSetBoardPosition(r, c, color);
            }
        });

    }

    @Override
    public void sendChatMessage(String userName, String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Controller.logMessage(userName + ": " + message);
            }
        });
    }

    @Override
    public void undo() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameControllerSingleton gameController = GameControllerSingleton.getInstance();
                gameController.localUndo();
            }
        });

    }

    @Override
    public void restart() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameControllerSingleton gameController = GameControllerSingleton.getInstance();
                gameController.restartGame();
            }
        });

    }

    @Override
    public void defeat() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameControllerSingleton gameController = GameControllerSingleton.getInstance();
                gameController.winGame();
            }
        });
    }

    @Override
    public void victory() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameControllerSingleton gameController = GameControllerSingleton.getInstance();
                gameController.loseGame();
            }
        });
    }

    @Override
    public void tie() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameControllerSingleton gameController = GameControllerSingleton.getInstance();
                gameController.tieGame();
            }
        });

    }

}
