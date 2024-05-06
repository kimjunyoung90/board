package jdbc;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.StringReader;
import java.sql.DriverManager;
import java.util.Properties;

public class DBCPInitListener implements ServletContextListener {

    private void loadJDBCDriver(Properties prop) {
        try {
            //1. 커넥션 풀이 내부에서 사용할 JDBC 드라이버를 로드한다.
            String driverClass = prop.getProperty("jdbcdriver");
            Class.forName(driverClass);
        }catch (ClassNotFoundException ex) {
            throw new RuntimeException("fail to load JDBC Driver", ex);
        }
    }

    private void initConnectionPool(Properties prop) {
        try {
            String jdbcUrl = prop.getProperty("jdbcUrl");
            String dbUser = prop.getProperty("dbUser");
            String dbPass = prop.getProperty("dbPass");

            //2. 커넥션 풀이 새로운 커넥션을 생성할 때 사용할 커넥션 팩토리를 생성한다.
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbcUrl, dbUser, dbPass);

            //3. PoolableConnection을 생성하는 팩토리를 생성한다. DBCP는 커넥션 풀에 커넥션을 보관할 때 PoolableConnection을 사용한다.
            //이 클래스는 내부적으로 실제 커넥션을 담고 있으며, 커넥션 풀을 관리하는데 필요한 기능을 추가로 제공한다.
            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
            String validationQuery = prop.getProperty("validationQuery");
            if(validationQuery != null && !validationQuery.isEmpty()) {
                poolableConnectionFactory.setValidationQuery(validationQuery);
            }
            poolableConnectionFactory.setValidationQuery(validationQuery);

            //4. 커넥션 풀의 설정 정보를 생성한다.
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setTimeBetweenEvictionRunsMillis(100L * 60L * 5L);
            poolConfig.setTestWhileIdle(true);
            int minIdle = getIntProperty(prop, "minIdle", 5);
            poolConfig.setMinIdle(minIdle);
            int maxTotal = getIntProperty(prop, "maxTotal", 5);
            poolConfig.setMaxTotal(maxTotal);

            //5. 커넥션 풀을 생성한다.
            GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory, poolConfig);
            poolableConnectionFactory.setPool(connectionPool);

            //6. 커넥션 풀을 제공하는 JDBC 드라이버를 등록한다.
            Class.forName("org.apache.commons.dbcp2.PoolingDriver");

            //7. 커넥션 풀 드라이버에 생성한 커넥션 풀을 등록한다.
            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
            String poolName = prop.getProperty("poolName");
            driver.registerPool(poolName, connectionPool);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int getIntProperty(Properties prop, String propName, int defaultValue) {
        String value = prop.getProperty(propName);
        if(value == null) return defaultValue;
        return Integer.parseInt(value);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String poolConfig = servletContextEvent.getServletContext().getInitParameter("poolConfig");
        Properties prop = new Properties();
        try {
            prop.load(new StringReader(poolConfig));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadJDBCDriver(prop);
        initConnectionPool(prop);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
