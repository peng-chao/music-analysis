package me.vcode.music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng_chao_b on 7/3/17.
 */
public class Init {
    private static List<String> category = new ArrayList<>();

    static {
        category.add("全部");
        category.add("华语");
        category.add("欧美");
        category.add("日语");
        category.add("韩语");
        category.add("粤语");
        category.add("小语种");

        category.add("流行");
        category.add("摇滚");
        category.add("民谣");
        category.add("电子");
        category.add("舞曲");
        category.add("说唱");
        category.add("轻音乐");
        category.add("爵士");
        category.add("乡村");
        category.add("R&B/Soul");
        category.add("古典");
        category.add("民族");
        category.add("英伦");
        category.add("金属");
        category.add("朋克");
        category.add("蓝调");
        category.add("雷鬼");
        category.add("世界音乐");
        category.add("拉丁");
        category.add("另类/独立");
        category.add("New Age");
        category.add("古风");
        category.add("后摇");
        category.add("Bossa Nova");

        category.add("清晨");
        category.add("夜晚");
        category.add("学习");
        category.add("工作");
        category.add("午休");
        category.add("下午茶");
        category.add("地铁");
        category.add("驾车");
        category.add("运动");
        category.add("旅行");
        category.add("散步");
        category.add("酒吧");

        category.add("怀旧");
        category.add("清新");
        category.add("浪漫");
        category.add("性感");
        category.add("伤感");
        category.add("治愈");
        category.add("放松");
        category.add("孤独");
        category.add("感动");
        category.add("兴奋");
        category.add("快乐");
        category.add("安静");
        category.add("思念");

        category.add("影视原声");
        category.add("ACG");
        category.add("校园");
        category.add("游戏");
        category.add("70后");
        category.add("80后");
        category.add("90后");
        category.add("网络歌曲");
        category.add("KTV");
        category.add("经典");
        category.add("翻唱");
        category.add("吉他");
        category.add("钢琴");
        category.add("器乐");
        category.add("儿童");
        category.add("榜单");
        category.add("00后");
    }

    public static void init() {
        new Thread(() -> {
            for (String s : category) {
                try {
                    Discover discover = new Discover();
                    discover.discoverUrl(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
