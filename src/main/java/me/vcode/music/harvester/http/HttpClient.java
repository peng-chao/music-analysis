package me.vcode.music.harvester.http;

import me.vcode.learn.music.Utils;
import me.vcode.learn.music.pool.Pool;
import me.vcode.learn.music.pool.PoolFactory;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by peng_chao_b on 7/4/17.
 */
public class HttpClient {

    private static Pool<IProxy> pool = PoolFactory.newBoundedBlockingPool(new IProxyFactory(), new IProxyValidator());

    public static Document get(String url) {
        IProxy iProxy = null;
        try {
            iProxy = pool.get();
            Connection.Response response = Jsoup.connect(url)
                    .proxy(iProxy.getIp(), iProxy.getPort())
                    .method(Connection.Method.GET).execute();
            return response.parse();

        } catch (SocketTimeoutException e) {
            if (iProxy != null) {
                iProxy.setAvailable(false);
            }
        } catch (ConnectException e) {
            if (iProxy != null) {
                iProxy.setAvailable(false);
            }
        } catch (SocketException e) {
            if (iProxy != null) {
                iProxy.setAvailable(false);
            }
        } catch (HttpStatusException e) {
            int code = e.getStatusCode();
            if (iProxy != null && (code >= 500 || code == 403)) {
                iProxy.setAvailable(false);
            }
        } catch (IOException e) {
            System.out.println("[GET] ==> " + e.toString());
        } finally {
            pool.release(iProxy);
        }
        return null;
    }


    public static Document post(String url) {
        String[] param = Utils.Encryp.encryption();
        Document document = null;
        IProxy iProxy = null;
        try {
            iProxy = pool.get();
            Connection.Response response = Jsoup.connect(url).proxy(iProxy.getIp(), iProxy.getPort())
                    .cookie("appver", "1.5.0.75771")
                    .header("Referer", "http://music.163.com/")
                    .data("params", param[0]).data("encSecKey", param[1])
                    .ignoreContentType(true).method(Connection.Method.POST).execute();
            document = response.parse();
        } catch (SocketTimeoutException e) {
            if (iProxy != null) {
                iProxy.setAvailable(false);
            }
        } catch (ConnectException e) {
            if (iProxy != null) {
                iProxy.setAvailable(false);
            }
        } catch (SocketException e) {
            if (iProxy != null) {
                iProxy.setAvailable(false);
            }
        } catch (HttpStatusException e) {
            int code = e.getStatusCode();
            if (iProxy != null && (code >= 500 || code == 403)) {
                iProxy.setAvailable(false);
            }
        } catch (IOException e) {
            System.out.println("[POST] ==> " + e.toString());
        } finally {
            pool.release(iProxy);
        }
        return document;
    }

}

