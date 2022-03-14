import data.DataHelper;
import data.SqlData;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    private LoginPage loginPage;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    void shouldLoginWithExistUser() {

        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = SqlData.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldAddNewUserAndLogin() {

        val authInfo = DataHelper.getUser();
        val verificationPage = loginPage.validLogin(authInfo);
        sleep(900);
        val verificationCode = SqlData.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldBlockIfLoginWithWrongPasswordThreeTime() {

        val authInfo = DataHelper.getUserWithWrongPassword();
        loginPage.validLogin(authInfo);
        loginPage.errorMessage();
        loginPage.clearFields();
        loginPage.validLogin(authInfo);
        loginPage.errorMessage();
        loginPage.clearFields();
        loginPage.validLogin(authInfo);
        val status = SqlData.getUserStatus(authInfo.getLogin());
        assertEquals("blocked", status);
    }
}