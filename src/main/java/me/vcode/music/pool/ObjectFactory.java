package me.vcode.music.pool;

import java.util.List;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public interface ObjectFactory<T> {
    public abstract List<T> createNew();
}
