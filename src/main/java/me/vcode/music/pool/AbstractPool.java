package me.vcode.music.pool;

import me.vcode.learn.music.pool.Pool;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public abstract class AbstractPool<T> implements Pool<T> {

    @Override
    public final void release(T t) {
        if (isValid(t)) {
            returnToPool(t);
        } else {
            handleInvalidReturn(t);
        }
    }

    protected abstract void handleInvalidReturn(T t);

    protected abstract void returnToPool(T t);

    protected abstract boolean isValid(T t);

}
