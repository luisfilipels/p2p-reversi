package networking;

import utils.SessionDataSingleton;

import java.net.DatagramSocket;
import java.net.SocketException;

public class NetworkHandlerSingleton {

    private static NetworkHandlerSingleton instance;

    private Sender sender;
    private Receiver receiver;

    private Thread t1;
    private Thread t2;

    private NetworkHandlerSingleton() {
        /*try {
            socket = new DatagramSocket(SessionDataSingleton.getInstance().getReceivePort());
            sender = new Sender(socket);
            receiver = new Receiver(socket);
        } catch (SocketException e) {
            e.printStackTrace();
        }*/
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

    String buildChatMessage(String message) {
        return "chat|" + SessionDataSingleton.getInstance().getUserName() + "|" + message;
    }

    String buildGameEventMessage(String messageType) {
        return "game|" + messageType;
    }

    String buildGameMoveMessage(int r, int c) {
        return "game|move|" + r + "," + c + "|" + SessionDataSingleton.getInstance().getUserColor();
    }

    public void sendChatMessageToSender(String message) {
        sender.setStringToSend(buildChatMessage(message));
    }

    public void sendGameEventMessageToSender(String event) {
        sender.setStringToSend(buildGameEventMessage(event));
    }

    public void sendGameMoveMessageToSender(int r, int c) {
        sender.setStringToSend(buildGameMoveMessage(r, c));
    }

    public void start() {
        t1 = new Thread(sender);
        t2 = new Thread(receiver);

        t1.start();
        t2.start();
    }

}
