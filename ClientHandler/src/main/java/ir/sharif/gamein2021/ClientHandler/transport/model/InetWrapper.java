package ir.sharif.gamein2021.ClientHandler.transport.model;

import java.util.Objects;

public class InetWrapper {
    private String ip;
    private int port;

    public InetWrapper(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InetWrapper that = (InetWrapper) o;
        return port == that.port && ip.equals(that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
