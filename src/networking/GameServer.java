package networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameServer extends Remote {

    void addWhiteListener(GameInterface listener) throws RemoteException;
    void addBlackListener(GameInterface listener) throws RemoteException;

    void makeMove(int r, int c, char color) throws RemoteException;

    void sendChatMessage(String userName, String message, char sender) throws RemoteException;

    void endTurn(char sender) throws RemoteException;

    void undo(char sender) throws RemoteException;

    void restart(char sender) throws RemoteException;

    void defeat(char sender) throws RemoteException;

    void victory(char sender) throws RemoteException;

    void tie(char sender) throws RemoteException;
}
