package me.vcode.music;

import me.vcode.learn.music.harvester.http.HttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;

/**
 * Created by peng_chao_b on 6/25/17.
 */
public class Playlist implements Serializable {

    public long play_id;
    public String name;
    public String tag;
    public long favourite;
    public long share;
    public long like_count;


    transient Runnable runnable = () -> playlist();

    public void playlist() {

        String url = TaskQueue.PLAY_LIST_URL.poll();
        if (Utils.isNullOrEmpty(url)) return;

        try {
            Document doc = HttpClient.get(url);
            if (doc == null) {
                TaskQueue.PLAY_LIST_URL.add(url);
                return;
            }

            String favourite = Utils.getNumbers(doc.select(".u-btni-fav").text());
            String share = Utils.getNumbers(doc.select(".u-btni-share").text());
            String review = Utils.getNumbers(doc.select(".u-btni-cmmt").text());

            String tag = doc.select(".tags.f-cb").select(".u-tag").text();
            String name = doc.select(".f-ff2.f-brk").text();

            Playlist playlist = new Playlist();
            playlist.name = name;
            playlist.tag = tag;

            if (!Utils.isNullOrEmpty(favourite)) playlist.favourite = Long.valueOf(favourite);

            if (!Utils.isNullOrEmpty(share)) playlist.share = Long.valueOf(share);
            if (!Utils.isNullOrEmpty(review)) playlist.like_count = Long.valueOf(review);

            playlist.play_id = Integer.valueOf(url.split("=")[1]);

            TaskQueue.PLAY_LIST_DATA.add(playlist);

            Elements links = doc.select(".n-songtb").select("a[href]");
            for (Element link : links) {
                String linkStr = link.attr("abs:href");
                if (TaskQueue.addBloom(linkStr)) {
                    Play play = new Play();
                    play.play_id = playlist.play_id;
                    play.url = linkStr;
                    TaskQueue.SONG_URL.add(play);
                }
            }
        } catch (Exception e) {
            System.out.println("playlist url ==> " + url + " " + e.getMessage());
        }
    }


    public static class Play implements Serializable {
        public String url;
        public long play_id;
    }
}
