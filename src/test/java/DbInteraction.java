import com.github.javafaker.Faker;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbInteraction {
    @BeforeEach
    void setUp() throws SQLException {
        val faker = new Faker();
        val dataSQL = "INSERT INTO users(login, password, status) VALUES (?, ?, ?);";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
                val dataStmt = conn.prepareStatement(dataSQL);
        ) {
            dataStmt.setString(1, faker.name().username());
            dataStmt.setString(2, "password");
            dataStmt.setString(3, "active");
        }
    }

    @Test
    void stubTest() throws SQLException {
        val countSQL = "SELECT COUNT(*) FROM users;";
        val cardsSQL = "SELECT id, number, balance_in_kopecks FROM cards WHERE user_id = ?;";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
                val countStmt = conn.createStatement();
                val cardsStmt = conn.prepareStatement(cardsSQL);
        ) {
            try (val rs = countStmt.executeQuery(countSQL)) {
                if (rs.next()) {
                    // выборка значения по индексу столбца (нумерация с 1)
                    val count = rs.getInt(1);
                    assertEquals(2, count);
                }
            }

            cardsStmt.setInt(1, 1);
            try (val rs = cardsStmt.executeQuery()) {
                while (rs.next()) {
                    val id = rs.getInt("id");
                    val number = rs.getString("number");
                    val balanceInKopecks = rs.getInt("balance_in_kopecks");
                    // TODO: сложить всё в список
                    System.out.println(id + " " + number + " " + balanceInKopecks);
                }
            }
        }
    }
}
