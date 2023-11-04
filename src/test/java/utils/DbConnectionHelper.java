package utils;

import data.OrderEntity;
import data.PaymentEntity;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import data.CreditRequestEntity;

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
    public static PaymentEntity getCardStatusForPayment() {
        getConnection();
        var runner = new QueryRunner();
        var statusQuery = "SELECT * FROM payment_entity";
        return runner.query(connection, statusQuery, new BeanHandler<>(PaymentEntity.class));

    }

    @SneakyThrows
    public static CreditRequestEntity getCardStatusForCreditRequest() {
        getConnection();
        var runner = new QueryRunner();
        var statusQuery = "SELECT * FROM credit_request_entity";
        return runner.query(connection, statusQuery, new BeanHandler<>(CreditRequestEntity.class));

    }

    @SneakyThrows
    public static OrderEntity getPaymentId() {
        getConnection();
        var runner = new QueryRunner();
        var idQueryForCardPay = "SELECT * FROM order_entity";
        return runner.query(connection, idQueryForCardPay, new BeanHandler<>(OrderEntity.class));

    }

    @SneakyThrows
    public static PaymentEntity getTransactionId() {
        getConnection();
        var runner = new QueryRunner();
        var idTransactionQuery = "SELECT * FROM payment_entity";
        return runner.query(connection, idTransactionQuery, new BeanHandler<>(PaymentEntity.class));

    }

    @SneakyThrows
    public static PaymentEntity getAmountPayment() {
        getConnection();
        var runner = new QueryRunner();
        var amountQuery = "SELECT * FROM payment_entity";
        return runner.query(connection, amountQuery, new BeanHandler<>(PaymentEntity.class));

    }

    @SneakyThrows
    public static CreditRequestEntity getBankId() {
        getConnection();
        var runner = new QueryRunner();
        var bankIdQuery = "SELECT * FROM credit_request_entity";
        return runner.query(connection, bankIdQuery, new BeanHandler<>(CreditRequestEntity.class));

    }

}
