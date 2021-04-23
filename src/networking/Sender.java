package networking;

import utils.SessionDataSingleton;

import java.net.InetAddress;
import java.util.concurrent.Semaphore;

public class Sender {

    // Used to avoid polling the stringToSend field for new data to send to the other player's client.
    // When there is no new message to send, Sender's thread sleeps, and is awakened when a new message arrives
    // from other objects in the game.
    //Semaphore s = new Semaphore(0);

    // Reference to the socket created by NetworkHandlerSingleton
    //DatagramSocket socket;

    /*Sender(DatagramSocket socket) {
        this.socket = socket;
    }*/

    private String stringToSend;
    public void setStringToSend(String string) {
        stringToSend = string;
        //s.release();
        // Releases the mutex so the thread can continue to run
    }

    public String getStringToSend() {
        String returnString = "";
        /*try {
            s.acquire();
            returnString = stringToSend;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return returnString;
    }

    /*@Override
    public void run() {
        try {
            InetAddress server = InetAddress.getByName(SessionDataSingleton.getInstance().getRemoteAddress());
            int port = SessionDataSingleton.getInstance().getSendPort();

            while (true) {
                String message = getStringToSend();
                byte[] data = message.getBytes();
                DatagramPacket pacote = new DatagramPacket(data, data.length, server, port);
                socket.send(pacote);
            }
        } catch (Exception e) {

        }
    }*/

}
