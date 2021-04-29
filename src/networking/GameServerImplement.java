package networking;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GameServerImplement extends UnicastRemoteObject implements GameServer {

    GameInterface white;
    GameInterface black;

    GameServerImplement() throws RemoteException{
        super();
    }

    @Override
    public void addWhiteListener(GameInterface listener) throws RemoteException {
        white = listener;
        System.out.println("Server has white listener");
    }

    @Override
    public void addBlackListener(GameInterface listener) throws RemoteException {
        black = listener;
        System.out.println("Server has black listener");
    }

    @Override
    public void makeMove(int r, int c, char color) throws RemoteException {
        /*if (white == null) {
            black.makeMove(r, c, color);
            return;
        } else if (black == null) {
            white.makeMove(r, c, color);
            return;
        }*/
        if (color == 'w') {
            if (black == null) black = new Receiver();
            black.makeMove(r, c, color);
        } else {
            if (white == null) white = new Receiver();
            white.makeMove(r, c, color);
        }
    }

    @Override
    public void sendChatMessage(String userName, String message, char sender) throws RemoteException {
        /*if (white == null) {
            black.sendChatMessage(userName, message);
            return;
        } else if (black == null) {
            white.sendChatMessage(userName, message);
            return;
        }*/
        if (sender == 'w') {
            if (black == null) black = new Receiver();
            black.sendChatMessage(userName, message);
        } else {
            if (white == null) white = new Receiver();
            white.sendChatMessage(userName, message);
        }
    }

    @Override
    public void endTurn(char sender) throws RemoteException {
        /*if (white == null) {
            black.endTurn();
            return;
        } else if (black == null) {
            white.endTurn();
            return;
        }*/
        if (sender == 'w') {
            if (black == null) black = new Receiver();
            black.endTurn();
        } else {
            if (white == null) white = new Receiver();
            white.endTurn();
        }
    }

    @Override
    public void undo(char sender) throws RemoteException {
        /*if (white == null) {
            black.undo();
            return;
        } else if (black == null) {
            white.undo();
            return;
        }*/
        if (sender == 'w') {
            if (black == null) black = new Receiver();
            black.undo();
        } else {
            if (white == null) white = new Receiver();
            white.undo();
        }
    }

    @Override
    public void restart(char sender) throws RemoteException {
        /*if (white == null) {
            black.restart();
            return;
        } else if (black == null) {
            white.restart();
            return;
        }*/
        if (sender == 'w') {
            if (black == null) black = new Receiver();
            black.restart();
        } else {
            if (white == null) white = new Receiver();
            white.restart();
        }
    }

    @Override
    public void defeat(char sender) throws RemoteException {
        /*if (white == null) {
            black.defeat();
            return;
        } else if (black == null) {
            white.defeat();
            return;
        }*/
        if (sender == 'w') {
            if (black == null) black = new Receiver();
            black.defeat();
        } else {
            if (white == null) white = new Receiver();
            white.defeat();
        }
    }

    @Override
    public void victory(char sender) throws RemoteException {
        /*if (white == null) {
            black.victory();
            return;
        } else if (black == null) {
            white.victory();
            return;
        }*/
        if (sender == 'w') {
            if (black == null) black = new Receiver();
            black.victory();
        } else {
            if (white == null) white = new Receiver();
            white.victory();
        }
    }

    @Override
    public void tie(char sender) throws RemoteException {
        /*if (white == null) {
            black.tie();
            return;
        } else if (black == null) {
            white.tie();
            return;
        }*/
        if (sender == 'w') {
            if (black == null) black = new Receiver();
            black.tie();
        } else {
            if (white == null) white = new Receiver();
            white.tie();
        }
    }
}
