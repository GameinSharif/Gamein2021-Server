package ir.sharif.gamein2021.ClientHandler.transport.model;

public class Packet {
    private String clientIp;
    private int clientPort;
    private int packetNumber;
    private byte[] data;
    private int checkSum;

    public Packet(byte[] fullPacket) {
        clientIp = String.valueOf(fullPacket[0] & 0xFF) + '.' + (fullPacket[1] & 0xFF) + '.' + (fullPacket[2] & 0xFF) + '.' + (fullPacket[3] & 0xFF);
        clientPort = ((fullPacket[4] & 0xFF) << 8) + (fullPacket[5] & 0xFF);
        packetNumber = ((fullPacket[6] & 0xFF) << 24) + ((fullPacket[7] & 0xFF) << 16) + ((fullPacket[8] & 0xFF) << 8)
                + (fullPacket[9] & 0xFF);
        checkSum = ((fullPacket[10] & 0xFF) << 8) + (fullPacket[11] & 0xFF);
        data = new byte[fullPacket.length - 12];
        System.arraycopy(fullPacket, 12, data, 0, data.length);
    }

    public Packet(String clientIp, int clientPort, int packetNumber, byte[] data) {
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.packetNumber = packetNumber;
        this.data = data;
        this.checkSum = calculateCheckSum();
    }

    public byte[] getByteArray() {
        byte[] segments = new byte[12 + data.length];
        String[] ipSplits = clientIp.split("\\.");
        segments[0] = Integer.valueOf(ipSplits[0]).byteValue();
        segments[1] = Integer.valueOf(ipSplits[1]).byteValue();
        segments[2] = Integer.valueOf(ipSplits[2]).byteValue();
        segments[3] = Integer.valueOf(ipSplits[3]).byteValue();
        segments[4] = (byte) (clientPort >> 8);
        segments[5] = (byte) (clientPort);
        segments[6] = (byte) (packetNumber >> 24);
        segments[7] = (byte) (packetNumber >> 16);
        segments[8] = (byte) (packetNumber >> 8);
        segments[9] = (byte) (packetNumber);
        segments[10] = (byte) (checkSum >> 8);
        segments[11] = (byte) (checkSum);
        System.arraycopy(data, 0, segments, 12, data.length);
        return segments;
    }

    public int calculateCheckSum() {
        byte[] segment = this.getByteArray();
        int sum = 0, i;
        for (i = 0; i < segment.length - 1; i += 2) {
            if (i == 10) continue;
            sum += ((int) segment[i] << 8) + (int) segment[i + 1];
            sum = (sum & 65535) + (sum >> 16);
        }
        if (i == segment.length - 1) {
            sum += ((int) segment[i] << 8);
            sum = (sum & 65535) + (sum >> 16);
        }
        return sum;
    }

    public String getClientIp() {
        return clientIp;
    }

    public int getClientPort() {
        return clientPort;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public byte[] getData() {
        return data;
    }

    public int getCheckSum() {
        return checkSum;
    }
}
