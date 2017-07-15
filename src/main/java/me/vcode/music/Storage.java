package me.vcode.music;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by peng_chao_b on 7/2/17.
 */
public class Storage {

    Runnable runnable_play = () -> play();
    Runnable runnable_song = () -> song();

    public void play() {
        if (!TaskQueue.PLAY_LIST_DATA.isEmpty()) {
            for (int i = 0; i < 100; i++) {
                Playlist playlist = TaskQueue.PLAY_LIST_DATA.poll();
                if (playlist == null) return;
                Storage storage = new Storage();
                storage.addPlaylist(playlist);
            }
        }
    }

    public void song() {
        if (!TaskQueue.SONG_DATA.isEmpty()) {
            for (int i = 0; i < 100; i++) {
                Song song = TaskQueue.SONG_DATA.poll();
                if (song == null) return;
                Storage storage = new Storage();
                storage.addSong(song);
            }
        }
    }


    public void addPlaylist(Playlist playlist) {
        String sql = "SET NAMES utf8mb4; " +
                "INSERT INTO `playlist` (`play_id`, `name`, `tag`, `favourite`, `share`, `like_count`) VALUES (?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE favourite = ?, share = ?, like_count = ?";
        try (Connection connection = DBConn.getConn();
             PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setLong(1, playlist.play_id);
            prep.setString(2, playlist.name);
            prep.setString(3, playlist.tag);
            prep.setLong(4, playlist.favourite);
            prep.setLong(5, playlist.share);
            prep.setLong(6, playlist.like_count);
            prep.setLong(7, playlist.favourite);
            prep.setLong(8, playlist.share);
            prep.setLong(9, playlist.like_count);
            prep.executeUpdate();
        } catch (Exception e) {
            Gson gson = new Gson();
            String json = gson.toJson(playlist);
            System.out.println("review json ==> " + json);
            e.printStackTrace();
        }
    }

    public void addSong(Song song) {
        String sql = "SET NAMES utf8mb4; " +
                "INSERT INTO `song` (`song_id`, `name`, `like_count`, `image`, `author`, `paly_id`) VALUES (?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE like_count = ?";
        try (Connection connection = DBConn.getConn();
             PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setLong(1, song.song_id);
            prep.setString(2, song.name);
            prep.setLong(3, song.like_count);
            prep.setString(4, song.image);
            prep.setString(5, song.author);
            prep.setLong(6, song.play_id);
            prep.setLong(7, song.like_count);
            prep.executeUpdate();
            List<Review.Content> list = song.reviewList;
            for (Review.Content content : list) {
                addReview(content);
            }
        } catch (Exception e) {
            Gson gson = new Gson();
            String json = gson.toJson(song);
            System.out.println("review json ==> " + json);
            e.printStackTrace();
        }
    }

    public void addReview(Review.Content review) {
        String sql = "SET NAMES utf8mb4;" +
                "INSERT INTO `review` (`song_id`, `content`, `like_count`, `time`, `nickname`, `comment_id`) VALUES (?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE like_count = ?";
        try (Connection connection = DBConn.getConn();
             PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setLong(1, review.song_id);
            prep.setString(2, review.content);
            prep.setLong(3, review.like_count);
            prep.setTimestamp(4, new Timestamp(review.time));
            prep.setString(5, review.nickname);
            prep.setLong(6, review.comment_id);
            prep.setLong(7, review.like_count);
            prep.executeLargeUpdate();
        } catch (Exception e) {
            Gson gson = new Gson();
            String json = gson.toJson(review);
            System.out.println("review json ==> " + json);
            e.printStackTrace();
        }
    }


    public void addDiscover(String keyword, int limit) {

        String sql = "SET NAMES utf8mb4;" +
                "INSERT INTO `discover` (`keyword`, `page`) VALUES (?,?) ON DUPLICATE KEY UPDATE page = ?";

        try (Connection connection = DBConn.getConn();
             PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setString(1, keyword);
            prep.setLong(2, limit);
            prep.setLong(3, limit);
            prep.executeLargeUpdate();
        } catch (Exception e) {
            System.out.println("discover json ==> " + keyword + " ==> " + limit);
            e.printStackTrace();
        }
    }

}
