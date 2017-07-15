package me.vcode.music;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by peng_chao_b on 7/2/17.
 */
public class TaskQueue {

    public static BloomFilter BLOOM_FILTER = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000000);

    public static ConcurrentLinkedQueue<String> DISCOVER_URL = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<String> PLAY_LIST_URL = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<Playlist.Play> SONG_URL = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<Playlist> PLAY_LIST_DATA = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<Song> SONG_DATA = new ConcurrentLinkedQueue<>();


    public static synchronized boolean addBloom(String url) {
        return BLOOM_FILTER.put(url);
    }


    public static class Serialization {

        public static void write() {
            write(BLOOM_FILTER, "BLOOM_FILTER");

            write(DISCOVER_URL, "DISCOVER_URL");
            write(PLAY_LIST_URL, "PLAY_LIST_URL");
            write(SONG_URL, "SONG_URL");

            write(PLAY_LIST_DATA, "PLAY_LIST_DATA");
            write(SONG_DATA, "SONG_DATA");
        }

        public static void read() {

            Object bloom = read("BLOOM_FILTER");
            if (bloom != null) BLOOM_FILTER = (BloomFilter) bloom;

            Object discover = read("DISCOVER_URL");
            if (discover != null) DISCOVER_URL = (ConcurrentLinkedQueue<String>) discover;

            Object playlist = read("PLAY_LIST_URL");
            if (playlist != null) PLAY_LIST_URL = (ConcurrentLinkedQueue<String>) playlist;

            Object song = read("SONG_URL");
            if (song != null) SONG_URL = (ConcurrentLinkedQueue<Playlist.Play>) song;

            Object playData = read("PLAY_LIST_DATA");
            if (playData != null) PLAY_LIST_DATA = (ConcurrentLinkedQueue<Playlist>) playData;

            Object songData = read("SONG_DATA");
            if (songData != null) SONG_DATA = (ConcurrentLinkedQueue<Song>) songData;
        }

        public static void write(Object object, String name) {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream("/Users/peng_chao_b/Desktop/serialization/" + name));
                oos.writeObject(object);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static Object read(String name) {
            ObjectInputStream ois = null;
            try {
                File file = new File("/Users/peng_chao_b/Desktop/serialization/" + name);
                ois = new ObjectInputStream(new FileInputStream(file));
                return ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    public static class Scheduled {
        private static ScheduledExecutorService discover;
        private static ScheduledExecutorService palylist;
        private static ScheduledExecutorService song;
        private static ScheduledExecutorService storage;
        private static ScheduledExecutorService exception;


        public static synchronized void stop() {
            stopDiscover();
            stopPlaylist();
            stopSong();
            stopStorage();
            stopExceptionCheck();
            Serialization.write();
        }

        public static synchronized void stopDiscover() {
            if (discover != null) {
                discover.shutdown();
                discover = null;
            }
        }

        public static synchronized void stopPlaylist() {
            if (palylist != null) {
                palylist.shutdown();
                palylist = null;
            }
        }

        public static synchronized void stopSong() {
            if (song != null) {
                song.shutdown();
                song = null;
            }
        }


        public static synchronized void stopStorage() {
            if (storage != null) {
                storage.shutdown();
                storage = null;
            }
        }


        public static synchronized void stopExceptionCheck() {
            if (exception != null) {
                exception.shutdown();
                exception = null;
            }
        }


        public static synchronized void startSong() {
            if (song == null) {
                song = Executors.newScheduledThreadPool(25);
                song.scheduleAtFixedRate(new Song().runnable, 1000, 300, TimeUnit.MILLISECONDS);
                for (int i = 1; i < 25; i++) {
                    song.scheduleAtFixedRate(new Song().runnable, i * 2000, 300, TimeUnit.MILLISECONDS);
                }
            }

            startStorage();
        }

        public static synchronized void startPlaylist() {
            if (palylist == null) {
                palylist = Executors.newScheduledThreadPool(25);
                palylist.scheduleAtFixedRate(new Playlist().runnable, 1000, 300, TimeUnit.MILLISECONDS);
                for (int i = 1; i < 25; i++) {
                    palylist.scheduleAtFixedRate(new Playlist().runnable, i * 2000, 300, TimeUnit.MILLISECONDS);
                }
            }

            startStorage();
        }


        public static synchronized void startStorage() {
            if (storage == null) {
                storage = Executors.newScheduledThreadPool(2);

                storage.scheduleAtFixedRate(new Storage().runnable_play, 30, 5, TimeUnit.SECONDS);
                storage.scheduleAtFixedRate(new Storage().runnable_song, 30, 5, TimeUnit.SECONDS);
            }
        }

        public static synchronized void start() {


            if (discover == null) {
                discover = Executors.newScheduledThreadPool(1);

                discover.scheduleAtFixedRate(new Discover().runnable, 1000, 100, TimeUnit.MILLISECONDS);
            }

            startPlaylist();
            startSong();
            startStorage();
        }
    }
}
