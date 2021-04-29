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

    GameServer remote;

    //private Sender sender;
    private Receiver receiver;
    private GameServerImplement server;

    private NetworkHandlerSingleton() {

    }

    public void startServer() {
        try {
            LocateRegistry.createRegistry(2020);
            server = new GameServerImplement();
            Naming.rebind("//localhost:2020/Game", server);
        } catch (Exception e) {
            System.out.println("Servidor local já existe! Continuando");
        }
    }

    public void startRMI() {
        try {
            SessionDataSingleton userData = SessionDataSingleton.getInstance();
            if (server == null) { // server local ja existe. conectar com ele
                System.out.println("Server is null");
                remote = (GameServer) Naming.lookup("//localhost:2020/Game");
            } else { // server local foi criado pois outro nao existia.
                System.out.println("Server is not null");
                remote = (GameServer) Naming.lookup("//" + userData.getRemoteAddress() + ":2020/Game");
            }
            receiver = new Receiver();
            if (userData.getUserColor() == 'w') {
                remote.addWhiteListener(receiver);
            } else {
                remote.addBlackListener(receiver);
            }
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
        SessionDataSingleton userData = SessionDataSingleton.getInstance();
        try {
            remote.sendChatMessage(userData.getUserName(), message, userData.getUserColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sender.setStringToSend(buildChatMessage(message));
    }

    public void sendGameEventMessageToSender(String event) {
        SessionDataSingleton userData = SessionDataSingleton.getInstance();
        try {
            switch (event) {
                case "endturn":
                    remote.endTurn(userData.getUserColor());
                    break;
                case "undo":
                    remote.undo(userData.getUserColor());
                    break;
                case "restart":
                    remote.restart(userData.getUserColor());
                    break;
                case "defeat":
                    remote.defeat(userData.getUserColor());
                    break;
                case "victory":
                    remote.victory(userData.getUserColor());
                    break;
                case "tie":
                    remote.tie(userData.getUserColor());
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
