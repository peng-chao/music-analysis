package me.vcode.music.harvester.http;

import me.vcode.learn.music.pool.Pool;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public class IProxyValidator implements Pool.Validator<IProxy> {


    @Override
    public boolean isValid(IProxy iProxy) {
        if (iProxy == null) {
            return false;
        }
        return iProxy.isAvailable();
    }

    @Override
    public void invalidate(IProxy iProxy) {

    }
}
