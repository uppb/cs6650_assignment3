package com.example.java_servlet;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.commons.dbcp2.BasicDataSource;


public class DatabaseConfig {
  private static BasicDataSource dataSource = new BasicDataSource();

  private static void init() {
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://cs6650.c6xff3divtzz.us-west-2.rds.amazonaws.com:3306/cs6650");
    //dataSource.setUrl("jdbc:mysql://localhost:3306/cs6650");
    dataSource.setUsername("myusername");
    dataSource.setPassword("mypassword");

    dataSource.setInitialSize(15); // initial number of connections
    dataSource.setMaxTotal(30);   // max number of connections
    dataSource.setMaxIdle(20);     // max number of idle connections
    dataSource.setMinIdle(10);     // min number of idle connections
    dataSource.setMaxWaitMillis(30000);  // timeout 30 seconds
  }

  static{
    init();
  }

  public static BasicDataSource getDataSource(){
    return dataSource;
  }
}





