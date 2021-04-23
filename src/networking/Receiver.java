package networking;

import gameLogic.GameControllerSingleton;
import javafx.application.Platform;
import main.Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Receiver extends UnicastRemoteObject implements GameInterface {

    // Reference to the socket created by NetworkHandlerSingleton
    //DatagramSocket socket;

    /*Receiver(DatagramSocket socket) {
        this.socket = socket;
    }*/

    Receiver() throws RemoteException {
        super();
    }

    //TODO: These methods probably have to use runLater
    @Override
    public void endTurn() {
        GameControllerSingleton gameController = GameControllerSingleton.getInstance();
        gameController.saveWorkingBoard();
        gameController.startTurn();
    }

    @Override
    public void makeMove(int r, int c, char color) {
        GameControllerSingleton gameController = GameControllerSingleton.getInstance();
        gameController.forceSetBoardPosition(r, c, color);
    }

    @Override
    public void sendChatMessage(String userName, String message) {
        Controller.logMessage(userName + ": " + message);
    }

    @Override
    public void undo() {
        GameControllerSingleton gameController = GameControllerSingleton.getInstance();
        gameController.localUndo();
    }

    @Override
    public void restart() {
        GameControllerSingleton gameController = GameControllerSingleton.getInstance();
        gameController.closeAllPopups();
        gameController.restartGame();
    }

    @Override
    public void defeat() {
        GameControllerSingleton gameController = GameControllerSingleton.getInstance();
        gameController.winGame();
    }

    @Override
    public void victory() {
        GameControllerSingleton gameController = GameControllerSingleton.getInstance();
        gameController.loseGame();
    }

    @Override
    public void tie() {
        GameControllerSingleton gameController = GameControllerSingleton.getInstance();
        gameController.tieGame();
    }

    /*void handleChat(String[] parts) {
        Controller.logMessage(parts[1] + ": " + parts[2]);
    }

    void handleGame(String[] parts) {
        String messageType = parts[1];
        GameControllerSingleton gameController = GameControllerSingleton.getInstance();
        if (gameController == null) {
            System.out.println("Couldn't change board as gameController is null!");
            return;
        }
        switch (messageType) {
            case "move" -> {
                String position = parts[2];
                char color = parts[3].charAt(0);
                int r = position.charAt(0) - '0';
                int c = position.charAt(2) - '0';
                gameController.forceSetBoardPosition(r, c, color);
            }
            case "endturn" -> Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameController.saveWorkingBoard();
                    gameController.startTurn();
                }
            });
            case "undo" -> Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameController.localUndo();
                }
            });
            case "restart" -> Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameController.closeAllPopups();
                    gameController.restartGame();
                }
            });
            case "defeat" -> Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameController.winGame();
                }
            });
            case "victory" -> Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameController.loseGame();
                }
            });
            case "tie" -> Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameController.tieGame();
                }
            });
        }
    }*/

    /*@Override
    public void run() {
        byte[] buffer = new byte[1000]; // Cria um buffer local
        try {
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(pacote);
                String contents = new String(pacote.getData(), 0, pacote.getLength());

                String[] parts = contents.split("\\|");

                if (parts[0].equals("chat")) {
                    handleChat(parts);
                } else if (parts[0].equals("game")) {
                    handleGame(parts);
                } else {
                    System.out.println("Message with invalid header received! Header: " + parts[0]);
                }

                pacote.setLength(buffer.length);
            }
        } catch (Exception e) {}
    }*/

}
