package networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {

    void makeMove(int r, int c, char color) throws RemoteException;

    void sendChatMessage(String userName, String message) throws RemoteException;

    void endTurn() throws RemoteException;

    void undo() throws RemoteException;

    void restart() throws RemoteException;

    void defeat() throws RemoteException;

    void victory() throws RemoteException;

    void tie() throws RemoteException;

}
