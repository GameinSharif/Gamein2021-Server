package ir.sharif.gamein2021.core.util;

import java.net.DatagramSocket;
import java.net.SocketException;

public class DatagramSocketFactory {
    private static DatagramSocketFactory instance = null;

    private DatagramSocket socket;

    public DatagramSocketFactory() {
        try {
            socket = new DatagramSocket(NetworkConstants.SERVER_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static DatagramSocketFactory getInstance() {
        if (instance == null)
            instance = new DatagramSocketFactory();
        return instance;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}
