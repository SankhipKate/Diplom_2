package tests;

import base.BaseTest;
import io.restassured.response.Response;
import models.User;
import models.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.ApiSteps;
import utils.TestConstants;

import static java.net.HttpURLConnection.*;

import static org.junit.Assert.*;

/**
 * Тесты для логина пользователей в системе Stellar Burgers.
 * Этот класс проверяет различные сценарии для логина.
 */
public class UserLoginTest extends BaseTest {

    private String testEmail;  // Email созданного пользователя
    private String password = TestConstants.PASSWORD;

    @Before
    public void setUp() {
        // Создаем пользователя перед тестами
        testEmail = ApiSteps.generateUniqueEmail();
        User user = new User(testEmail, password, TestConstants.TEST_USER_NAME);
        ApiSteps.createUser(user);
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после тестов
        UserCredentials credentials = new UserCredentials(testEmail, password);
        ApiSteps.deleteUser(credentials);    }

    // Успешный логин пользователя
    @Test
    public void successfulUserLogin() {
        UserCredentials credentials = new UserCredentials(testEmail, password);

        // Выполняем запрос на логин
        Response response = ApiSteps.loginUser(credentials);

        // Проверяем, что код ответа 200 (успешно)
        checkResponseStatus(response, HTTP_OK);

        // Проверяем, что accessToken был возвращен
        String accessToken = response.jsonPath().getString(TestConstants.FIELD_ACCESS_TOKEN);
        assertNotNull(accessToken);
        assertTrue(accessToken.startsWith(TestConstants.BEARER_PREFIX));
    }

    // Логин с неверным паролем
    @Test
    public void loginWithInvalidPassword() {
        UserCredentials credentials = new UserCredentials(testEmail, TestConstants.INVALID_PASSWORD);

        // Выполняем запрос на логин с неверным паролем
        Response response = ApiSteps.loginUser(credentials);

        // Проверяем, что код ответа 401 (Unauthorized)
        checkResponseStatus(response, HTTP_UNAUTHORIZED);

        // Проверяем сообщение об ошибке
        String errorMessage = response.jsonPath().getString(TestConstants.MESSAGE_FIELD);
        assertEquals(TestConstants.MESSAGE_INVALID_CREDENTIALS, errorMessage);
    }

    // Логин с несуществующим пользователем
    @Test
    public void loginWithNonExistentUser() {
        UserCredentials credentials = new UserCredentials(TestConstants.NON_EXISTENT_EMAIL, password);

        // Выполняем запрос на логин с несуществующим пользователем
        Response response = ApiSteps.loginUser(credentials);

        // Проверяем, что код ответа 401 (Unauthorized)
        checkResponseStatus(response, HTTP_UNAUTHORIZED);

        // Проверяем сообщение об ошибке
        String errorMessage = response.jsonPath().getString(TestConstants.MESSAGE_FIELD);
        assertEquals(TestConstants.MESSAGE_INVALID_CREDENTIALS, errorMessage);
    }
}
