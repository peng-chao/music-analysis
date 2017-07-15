package me.vcode.music.harvester.http;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public class IProxy {
    private String ip;
    private int port;
    private boolean available;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
