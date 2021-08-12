package ir.sharif.gamein2021.ClientHandler.transport.thread;

import ir.sharif.gamein2021.ClientHandler.transport.model.InetWrapper;
import ir.sharif.gamein2021.ClientHandler.transport.model.Packet;
import ir.sharif.gamein2021.core.util.DatagramSocketFactory;
import ir.sharif.gamein2021.core.util.NetworkConstants;
import ir.sharif.gamein2021.ClientHandler.view.View;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ExecutorThread extends Thread {
    Packet[] receivedBuffer;
    Packet[] sentBuffer;
    private final InetWrapper inet;
    private final Map<InetWrapper, Integer> sequenceNumberMap;
    private final static int RECEIVE_WINDOW_SIZE = NetworkConstants.WINDOW_SIZE;
    private final static int SENT_WINDOW_SIZE = NetworkConstants.SENT_WINDOW_SIZE;
    int readPacketNumber = 0;
    private boolean isDone = false;
    DatagramSocket socket;
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private View view;




    public ExecutorThread(Packet[] receivedBuffer, Packet[] sentBuffer, InetWrapper inet, Map<InetWrapper, Integer> sequenceNumberMap) {
        socket = DatagramSocketFactory.getInstance().getSocket();
        this.receivedBuffer = receivedBuffer;
        this.sentBuffer = sentBuffer;
        this.inet = inet;
        this.sequenceNumberMap = sequenceNumberMap;
    }

    public void run() {
//        try {
//            while (!isDone) {
//                readPacketNumber = sequenceNumberMap.get(inet);
//                if (receivedBuffer[readPacketNumber % RECEIVE_WINDOW_SIZE] != null && receivedBuffer[readPacketNumber % RECEIVE_WINDOW_SIZE].getPacketNumber() == readPacketNumber) {
//                    Packet packet;
//                    synchronized (receivedBuffer) {
//                        packet = receivedBuffer[readPacketNumber % RECEIVE_WINDOW_SIZE];
//                    }
//
//                    String response = view.processCommands(new String(packet.getData(), StandardCharsets.UTF_8));
//                    System.out.println("\treponse -> " + readPacketNumber + " : " + response);
//                    logger.info("\treponse -> " + readPacketNumber + " : " + response);
//                    byte[] data = response.getBytes();
//                    Packet sentPacket = new Packet(packet.getClientIp(), packet.getClientPort()
//                            , readPacketNumber, data);
//                    DatagramPacket sentDatagramPacket = new DatagramPacket(sentPacket.getByteArray(), sentPacket.getByteArray().length,
//                            InetAddress.getByName(NetworkConstants.GATEWAY_IP), NetworkConstants.GATEWAY_PORT);
//                    socket.send(sentDatagramPacket);
//                    synchronized (sentBuffer) {
//                        sentBuffer[readPacketNumber % SENT_WINDOW_SIZE] = sentPacket;
//                    }
//                    synchronized (sequenceNumberMap) {
//                        sequenceNumberMap.put(inet, readPacketNumber + 1);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            logger.error("executor", e);
//        }
    }

    public void setDone() {
        isDone = true;
    }
}
