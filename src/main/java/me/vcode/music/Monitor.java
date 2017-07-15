package me.vcode.music;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by peng_chao_b on 7/3/17.
 */
public class Monitor {

    public static void startMonitor() {
        ServerSocket server = null;
        BufferedReader br;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress("127.0.0.1", 8888));
        } catch (Exception e) {
            System.out.println("绑定端口失败");
        }
        try {
            while (true) {
                Socket sock = server.accept();
                sock.setSoTimeout(1000);
                br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String readContent = br.readLine();
                System.out.println("接收到的内容是 ==> " + readContent);


                if ("init".equals(readContent)) {
                    Init.init();
                }

                if ("start".equals(readContent)) {
                    TaskQueue.Scheduled.start();
                }

                if ("start_song".equals(readContent)) {
                    TaskQueue.Scheduled.startSong();
                }

                if ("start_playlist".equals(readContent)) {
                    TaskQueue.Scheduled.startPlaylist();
                }

                if ("stop".equals(readContent)) {
                    TaskQueue.Scheduled.stop();
                }

                if ("stop_song".equals(readContent)) {
                    TaskQueue.Scheduled.stopSong();
                }

                if ("stop_playlist".equals(readContent)) {
                    TaskQueue.Scheduled.stopPlaylist();
                }

                if ("stop_discover".equals(readContent)) {
                    TaskQueue.Scheduled.stopDiscover();
                }

                if ("stop_storage".equals(readContent)) {
                    TaskQueue.Scheduled.stopStorage();
                }

                br.close();
                sock.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
