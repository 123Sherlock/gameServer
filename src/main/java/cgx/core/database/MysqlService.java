package cgx.core.database;

import com.alibaba.druid.pool.DruidDataSource;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MysqlService {

    private static DruidDataSource dDataSrc;

    /**
     * 初始化，全局的Mysql连接，
     * 这个连接只用来读数据，全服通用
     */
    public static void initMysqlServer(Properties p) {
        dDataSrc = new DruidDataSource();
        dDataSrc.setDriverClassName("com.mysql.jdbc.Driver");
        dDataSrc.setUrl(String.format("jdbc:mysql://%s/?useUnicode=true&characterEncoding=UTF-8",
                p.getProperty("mysql_ip")));
        dDataSrc.setUsername(p.getProperty("mysql_user"));
        dDataSrc.setPassword(p.getProperty("mysql_password"));
        dDataSrc.setMaxActive(30); // 最大负载20个，10个用来查询
        dDataSrc.setMinIdle(1);
        dDataSrc.setMaxWait(60000L);
        dDataSrc.setInitialSize(1);
    }

    public static Connection getConnection() {
        try {
            return dDataSrc.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> queryList(String sql) {
        List<String> queryList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement pStatement = null;
        try {
            pStatement = connection.prepareStatement(sql);
            ResultSet rs = pStatement.executeQuery();
            while (rs.next()) {
                String resultValue = rs.getString(1);
                if (resultValue != null) {
                    String result = new String(resultValue.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    queryList.add(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return queryList;
    }
}
