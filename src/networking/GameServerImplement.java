package networking;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
        if (sender == 'w') {
            if (black == null) black = new Receiver();
            black.tie();
        } else {
            if (white == null) white = new Receiver();
            white.tie();
        }
    }
}
