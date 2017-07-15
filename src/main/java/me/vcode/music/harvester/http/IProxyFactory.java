package me.vcode.music.harvester.http;


import com.google.gson.Gson;
import me.vcode.learn.music.pool.ObjectFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public class IProxyFactory implements ObjectFactory<IProxy> {

    private Set<String> repeat = new HashSet<>();

    public List<IProxy> getIP() {
        List<IProxy> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://api.goubanjia.com/api/get.shtml" +
                    "?order=f89501aa912a56bee306de2a0a317054&num=1000" +
                    "&carrier=0&protocol=1&an1=1&an2=2&sp1=1&sp2=2" +
                    "&sort=1&rettype=0&seprator=%0D%0A")
                    .get();

            String json = document.text();
            Gson gson = new Gson();
            IP ip = gson.fromJson(json, IP.class);


            if (!ip.success) {
                return list;
            }

            for (IP.DataBean datum : ip.data) {
                if (!repeat.add(datum.ip)) {
                    continue;
                }

                try {
                    Jsoup.connect("http://music.163.com/").proxy(datum.ip, datum.port).timeout(3000).get();
                    IProxy iProxy = new IProxy();
                    iProxy.setAvailable(true);
                    iProxy.setIp(datum.ip);
                    iProxy.setPort(datum.port);
                    list.add(iProxy);
                } catch (Exception e) {
                    System.out.println("不合格的 IP ==> " + datum.ip + ":" + datum.port + " ==> " + e.toString());
                }
            }

            if (list.isEmpty()) {
                repeat.clear();
                getIP();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    private static class IP {
        public String msg;
        public boolean success;
        public List<DataBean> data;

        public static class DataBean {
            public String ip;
            public int port;
        }
    }

    @Override
    public List<IProxy> createNew() {
        return getIP();
    }
}
