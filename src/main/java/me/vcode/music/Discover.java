package me.vcode.music;

import me.vcode.learn.music.harvester.http.HttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

/**
 * Created by peng_chao_b on 6/27/17.
 */
public class Discover {

    Runnable runnable = () -> playlistUrl();

    public void discoverUrl(String s) {
        String url = "";
        try {
            String encoding = URLEncoder.encode(s, "UTF-8");
            url = "http://music.163.com/discover/playlist/?order=hot&cat={category}".replace("{category}", encoding);

            Document doc = HttpClient.get(url);
            if (doc == null) return;

            String lastPage = doc.select(".u-page").select("a[href]").last().previousElementSibling().text();
            if (Utils.isNumeric(lastPage)) {
                int page = Integer.valueOf(lastPage);
                new Storage().addDiscover(s, page);
                for (int i = 0; i < page; i++) {
                    String pageUrl = url + "&limit=35&offset=" + i * 35;
                    if (TaskQueue.addBloom(pageUrl)) {
                        TaskQueue.DISCOVER_URL.add(pageUrl);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("discover url ==> " + url);
            e.printStackTrace();
        }
    }

    public void playlistUrl() {
        String discover_url = TaskQueue.DISCOVER_URL.poll();
        if (discover_url == null) {
            TaskQueue.DISCOVER_URL.add(discover_url);
            return;
        }

        try {
            Document doc = HttpClient.get(discover_url);
            if (doc == null) return;

            Elements links = doc.select("#m-pl-container").select(".dec").select("a[href]");

            for (Element link : links) {
                String linkStr = link.attr("abs:href");
                if (linkStr.contains("playlist") && TaskQueue.addBloom(linkStr)) {
                    TaskQueue.PLAY_LIST_URL.add(linkStr);
                }
            }
        } catch (Exception e) {
            System.out.println("playlist url ==> " + discover_url);
            e.printStackTrace();
        }
    }
}
