package networking;

import javafx.application.Platform;
import utils.SessionDataSingleton;

import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class NetworkHandlerSingleton {

    private static NetworkHandlerSingleton instance;

    GameInterface remote;

    //private Sender sender;
    private Receiver receiver;

    private NetworkHandlerSingleton() {

    }

    public void startServer() {
        try {
            LocateRegistry.createRegistry(2020);

            receiver = new Receiver();
            Naming.rebind("//localhost:2020/Game", receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRMI() {
        try {
            SessionDataSingleton userData = SessionDataSingleton.getInstance();
            remote = (GameInterface) Naming.lookup("//" + userData.getRemoteAddress() + ":2020/Game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NetworkHandlerSingleton getHandler() {
        if (instance == null) {
            synchronized(NetworkHandlerSingleton.class){
                if (instance == null){
                    instance = new NetworkHandlerSingleton();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    /*String buildChatMessage(String message) {
        return "chat|" + SessionDataSingleton.getInstance().getUserName() + "|" + message;
    }

    String buildGameEventMessage(String messageType) {
        return "game|" + messageType;
    }

    String buildGameMoveMessage(int r, int c) {
        return "game|move|" + r + "," + c + "|" + SessionDataSingleton.getInstance().getUserColor();
    }*/

    public void sendChatMessageToSender(String message) {
        try {
            remote.sendChatMessage(SessionDataSingleton.getInstance().getUserName(), message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sender.setStringToSend(buildChatMessage(message));
    }

    public void sendGameEventMessageToSender(String event) {
        try {
            switch (event) {
                case "endturn":
                    remote.endTurn();
                    break;
                case "undo":
                    remote.undo();
                    break;
                case "restart":
                    remote.restart();
                    break;
                case "defeat":
                    remote.defeat();
                    break;
                case "victory":
                    remote.victory();
                    break;
                case "tie":
                    remote.tie();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //sender.setStringToSend(buildGameEventMessage(event));
    }

    public void sendGameMoveMessageToSender(int r, int c) {
        try {
            remote.makeMove(r, c, SessionDataSingleton.getInstance().getUserColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sender.setStringToSend(buildGameMoveMessage(r, c));
    }

    /*public void start() {
        t1 = new Thread(sender);
        t2 = new Thread(receiver);

        t1.start();
        t2.start();
    }*/

}
