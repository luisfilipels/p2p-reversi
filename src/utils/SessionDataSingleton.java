package utils;

public class SessionDataSingleton {

    private String userName = "";
    private char userColor = ' ';

    private boolean aidActivated = false;

    private String remoteAddress;
    private int sendPort;
    private int receivePort;

    private static final SessionDataSingleton instance = new SessionDataSingleton();

    public static SessionDataSingleton getInstance() {
        return instance;
    }

    public char getUserColor() { return userColor; }
    public void setUserColor(char color) { this.userColor = color; }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public String getRemoteAddress() { return remoteAddress; }
    public void setRemoteAddress(String address) { this.remoteAddress = address; }

    public int getSendPort() { return sendPort; }
    public void setSendPort(int sendPort) { this.sendPort = sendPort; }

    public int getReceivePort() { return receivePort; }
    public void setReceivePort(int receivePort) { this.receivePort = receivePort; }

    public boolean isAidActivated() { return aidActivated; }
    public void setAidActivated(boolean aidActivated) { this.aidActivated = aidActivated; }
}
