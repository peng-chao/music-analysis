package me.vcode.music.pool;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public final class PoolFactory {
    private PoolFactory() {
    }

    public static <T> Pool<T> newBoundedBlockingPool(
            ObjectFactory<T> factory,
            Pool.Validator<T> validator) {

        return new BoundedBlockingPool<>(validator, factory);
    }
}
