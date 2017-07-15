package me.vcode.music;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by peng_chao_b on 7/3/17.
 */


// init, start, start_song, start_playlist, stop, stop_song, stop_storage, stop_discover, stop_playlist
public class Command {
    public static void main(String[] args) {
        exec("stop");
    }

    private static void exec(String cmd) {
        String targetHost = "127.0.0.1";
        int targetPort = 8888;
        try {
            Socket socket = new Socket(targetHost, targetPort);
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())));
            pw.println(cmd);
            pw.flush();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
