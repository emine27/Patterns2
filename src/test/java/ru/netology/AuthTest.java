package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.devtools.v102.network.Network.clearBrowserCookies;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//input[@name='login']").setValue(registeredUser.getLogin());
        $x("//input[@type='password']").setValue(registeredUser.getPassword());
        $x("//*[contains(@data-test-id,'action-login')]").click();
        $("h2").shouldHave(exactText("  Личный кабинет"));

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//input[@name='login']").setValue(notRegisteredUser.getLogin());
        $x("//input[@type='password']").setValue(notRegisteredUser.getPassword());
        $x("//*[contains(@data-test-id,'action-login')]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText("Ошибка! " +
                "Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//input[@name='login']").setValue(blockedUser.getLogin());
        $x("//input[@type='password']").setValue(blockedUser.getPassword());
        $x("//*[contains(@data-test-id,'action-login')]").click();
        $("[data-test-id=error-notification] .notification__content").
                shouldBe(exactText("Ошибка! " + "Пользователь заблокирован"));
    }


    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//input[@name='login']").setValue(wrongLogin);
        $x("//input[@type='password']").setValue(registeredUser.getPassword());
        $x("//*[contains(@data-test-id,'action-login')]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(exactText("Ошибка! " +
                "Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//input[@name='login']").setValue(registeredUser.getLogin());
        $x("//input[@type='password']").setValue(wrongPassword);
        $x("//*[contains(@data-test-id,'action-login')]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(exactText("Ошибка! " +
                "Неверно указан логин или пароль"));
    }
}
