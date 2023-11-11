package utils;

import models.OrderEntity;
import models.PaymentEntity;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import models.CreditRequestEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionHelper {
    private static final String URL = System.getProperty("db.url");
    private static final String USER = System.getProperty("db.user");
    private static final String PASSWORD = System.getProperty("db.password");
    private static Connection connection;
    private static QueryRunner runner;

    @SneakyThrows
    public static Connection getConnection() {
        runner = new QueryRunner();
        connection = DriverManager.getConnection(URL, USER, PASSWORD);

        return null;
    }

    @SneakyThrows
    public static void dropDataBase() {
        getConnection();
        var runner = new QueryRunner();
        var order = "DELETE FROM order_entity";
        var payment = "DELETE FROM payment_entity";
        var creditRequest = "DELETE FROM credit_request_entity";

        runner.update(connection, order);
        runner.update(connection, payment);
        runner.update(connection, creditRequest);
    }

    @SneakyThrows
    public static PaymentEntity getCardRequestStatus() {
        getConnection();
        var runner = new QueryRunner();
        var idQuery = "SELECT * FROM payment_entity";
        return runner.query(connection, idQuery, new BeanHandler<>(PaymentEntity.class));
    }


    @SneakyThrows
    public static CreditRequestEntity getCreditRequestStatus() {
        getConnection();
        var runner = new QueryRunner();
        var IdQuery = "SELECT * FROM credit_request_entity";
        return runner.query(connection, IdQuery, new BeanHandler<>(CreditRequestEntity.class));

    }

    @SneakyThrows
    public static OrderEntity getOrderInfo() {
        getConnection();
        var runner = new QueryRunner();
        var idQuery = "SELECT * FROM order_entity";
        return runner.query(connection, idQuery, new BeanHandler<>(OrderEntity.class));

    }

}
