package me.vcode.music;

/**
 * Created by peng_chao_b on 4/9/17.
 */
public class App {

    public static void main(String[] args) {
        TaskQueue.Serialization.read();
        Monitor.startMonitor();
    }
}