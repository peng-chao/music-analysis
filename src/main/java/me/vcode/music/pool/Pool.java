package me.vcode.music.pool;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public interface Pool<T> {

    T get();

    void release(T t);

    void shutdown();


    public static interface Validator<T> {
        public boolean isValid(T t);

        public void invalidate(T t);
    }
}