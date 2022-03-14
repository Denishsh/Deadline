package data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getUserWithWrongPassword() {
        val faker = new Faker();
        return new AuthInfo("vasya", faker.internet().password());
    }

    public static AuthInfo getUser(){
        return new AuthInfo(SqlData.getNewUser(), "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

}