package com.himanshu.kafkabinder;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class LiquibaseRunner {
    @Value("${spring.liquibase.change-log}")
    private String changeLog;

    @Value("${db.url}")
    private String jdbcUrl;

    @Value("${db.url2}")
    private String jdbcUrl2;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    //private final DataSource dataSource;


    @PostConstruct
    public void runLiquibase() {

        try (Connection connection =  DriverManager.getConnection(jdbcUrl, username, password);) {
            DatabaseConnection dbConnection = new JdbcConnection(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(dbConnection);
            Liquibase liquibase = new Liquibase(changeLog, new ClassLoaderResourceAccessor(), database);
            liquibase.update(""); // Apply changes
            connection.commit();
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace(); // Handle exceptions properly
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl2, username, password);
            Statement st = connection.createStatement();
            st.execute("CREATE DATABASE new_db_name TEMPLATE backup strategy FILE_COPY;");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
