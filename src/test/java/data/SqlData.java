package data;

import com.github.javafaker.Faker;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlData {
    public static String getVerificationCode() {
        val selectSQL = "SELECT code FROM auth_codes WHERE created = (select MAX(created) FROM auth_codes);";
        val runner = new QueryRunner();
        String code = null;

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            code =  runner.query(conn, selectSQL, new ScalarHandler<>());
        }

        catch (SQLException throwables) {
        }
        return code;
    }

    public static String getUserStatus(String login){
        val selectSQL = "SELECT status FROM users WHERE login = ?;";
        val runner = new QueryRunner();
        String status = null;

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"

                );
        ) {
            status = runner.query(conn, selectSQL, new ScalarHandler<>(), login);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return status;
    }

    public static void setNewUser(){
        val faker = new Faker();
        val runner = new QueryRunner();
        val dataSQL = "INSERT INTO users(id, login, password) VALUES (?, ?, ?);";
        val cleanUsers = "DELETE FROM users WHERE id = '1';";
        val cleanAuth_Codes = "DELETE FROM auth_codes;";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );

        ) {
            runner.execute(conn, cleanAuth_Codes);
            runner.execute(conn, cleanUsers);
            runner.update(conn, dataSQL, 1, faker.name().username(), "$2a$10$LupomVF1pCYXveW2SuzwYuk6kTEuEr7lyhYq8jdv7TdLrIzhNmFd.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getNewUser() {
        setNewUser();
        String user = null;
        val runner = new QueryRunner();
        val dataSQL = "SELECT login FROM users WHERE id = '1';";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            user =  runner.query(conn, dataSQL, new ScalarHandler<>());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
}
