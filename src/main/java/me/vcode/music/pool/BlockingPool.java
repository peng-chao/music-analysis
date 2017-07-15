package me.vcode.music.pool;

import java.util.concurrent.TimeUnit;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public interface BlockingPool<T> extends Pool<T> {
    T get();

    T get(long time, TimeUnit unit) throws InterruptedException;

}
