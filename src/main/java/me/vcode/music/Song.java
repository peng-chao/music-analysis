package me.vcode.music;

import me.vcode.learn.music.harvester.http.HttpClient;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng_chao_b on 6/27/17.
 */
public class Song implements Serializable {

    public long song_id;
    public String name;
    public long like_count;
    public String image;
    public String author;
    public long play_id;
    public List<Review.Content> reviewList = new ArrayList<>();

    transient Runnable runnable = () -> song();

    public void song() {
        Playlist.Play play = TaskQueue.SONG_URL.poll();

        try {
            if (play == null || Utils.isNullOrEmpty(play.url)) return;

            Document doc = HttpClient.get(play.url);
            if (doc == null) {
                TaskQueue.SONG_URL.add(play);
                return;
            }

            String name = "";
            String author = "";
            String image = "";

            try {
                name = doc.select(".m-lycifo").select(".f-ff2").text();
                image = doc.select(".j-img").select("img[data-src]").attr("data-src");
                author = doc.select(".des.s-fc4").select(".s-fc7").first().text();
            } catch (Exception e) {
                // ignore
            }


            Song song = new Song();
            song.song_id = Long.valueOf(play.url.split("=")[1]);
            song.name = name;
            song.image = image;
            song.author = author;
            song.play_id = play.play_id;

            Review review = Review.commentAPI(song.song_id);
            if (review != null) {
                song.reviewList = review.review;
                song.like_count = review.all_count;
            }

            TaskQueue.SONG_DATA.add(song);
        } catch (Exception e) {
            if (play != null) {
                System.out.println("[song] song url ==> " + play.url + " ==> " + e.getMessage());
            }
        }
    }
}
