package me.vcode.music;

import com.google.gson.Gson;
import me.vcode.learn.music.harvester.http.HttpClient;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng_chao_b on 6/27/17.
 */
public class Review implements Serializable {

    public List<Content> review;
    public long all_count;

    public static Review commentAPI(long songId) {

        String url = "";
        try {
            url = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_song_id/".replaceAll("song_id", String.valueOf(songId));
            Document document = HttpClient.post(url);
            if (document == null) return null;

            Gson gson = new Gson();
            Comments comments = gson.fromJson(document.text(), Comments.class);
            if (comments == null) return null;

            List<Content> contents = new ArrayList<>();
            int allCount = comments.total;
            Review review = new Review();
            review.all_count = allCount;
            review.review = contents;

            if (comments.hotComments == null) {
                return review;
            }

            for (Comments.HotCommentsBean hotComment : comments.hotComments) {
                Content content = new Content();
                content.content = hotComment.content;
                content.nickname = hotComment.user.nickname;
                content.like_count = hotComment.likedCount;
                content.time = hotComment.time;
                content.song_id = songId;
                content.comment_id = hotComment.commentId;
                contents.add(content);
            }
            return review;
        } catch (Exception e) {
            System.out.println("[Review] ==> " + songId + " ==> " + e.getMessage());
        }
        return null;
    }


    public static class Content implements Serializable {
        public long song_id;
        public String content;
        public long like_count;
        public String nickname;
        public long time;
        public long comment_id;
    }

    public static class Comments {

        public boolean isMusician;
        public int userId;
        public boolean moreHot;
        public int code;
        public int total;
        public boolean more;
        public List<?> topComments;
        public List<HotCommentsBean> hotComments;
        public List<CommentsBean> comments;

        public static class HotCommentsBean {

            public UserBean user;
            public long time;
            public int commentId;
            public int likedCount;
            public boolean liked;
            public String content;
            public List<?> beReplied;


            public static class UserBean {

                public Object locationInfo;
                public String avatarUrl;
                public int authStatus;
                public Object remarkName;
                public Object expertTags;
                public int userType;
                public int userId;
                public int vipType;
                public String nickname;

            }
        }

        public static class CommentsBean {

            public UserBeanX user;
            public long time;
            public int commentId;
            public int likedCount;
            public boolean liked;
            public String content;
            public boolean isRemoveHotComment;
            public List<?> beReplied;


            public static class UserBeanX {

                public Object locationInfo;
                public String avatarUrl;
                public int authStatus;
                public Object remarkName;
                public Object expertTags;
                public int userType;
                public int userId;
                public int vipType;
                public String nickname;

            }
        }
    }


}
