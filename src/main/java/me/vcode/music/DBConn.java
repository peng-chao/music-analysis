package me.vcode.music;

import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;


public class DBConn {

    private static class HolderClass {
        private static ConnectionPool connectionPool;

        static {
            PoolProperties p = new PoolProperties();
            p.setUrl("jdbc:mysql://localhost:3306/music?allowMultiQueries=true");
            p.setDriverClassName("com.mysql.jdbc.Driver");
            p.setUsername("root");
            p.setPassword("sees7&chanting");
            p.setJmxEnabled(true);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(30000);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMaxActive(100);
            p.setInitialSize(10);
            p.setMaxWait(10000);
            p.setRemoveAbandonedTimeout(60);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setMinIdle(10);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
            p.setJdbcInterceptors(
                    "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            DataSource dataSource = new DataSource();
            dataSource.setPoolProperties(p);
            connectionPool = dataSource.getPool();
        }
    }


    public static Connection getConn() throws SQLException {
        return HolderClass.connectionPool.getConnection();
    }
}

