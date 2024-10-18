package tests.user;

import base.BaseTest;
import io.restassured.response.Response;
import models.User;
import models.UserCredentials;
import org.junit.After;
import org.junit.Test;
import utils.ApiSteps;
import utils.TestConstants;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

/**
 * Тесты для регистрации пользователей в системе Stellar Burgers.
 * Включает тесты на успешную регистрацию и проверку ошибок при повторной регистрации.
 */
public class UserRegistrationTest extends BaseTest {

    private User testUser; // Созданный пользователь
    private String accessToken; // Токен для удаления пользователя

    @After
    public void tearDown() {
        //Удаление созданного пользователя после каждого теста
        if (testUser != null && accessToken != null) {
            deleteUserAfterTest(new UserCredentials(testUser.getEmail(), testUser.getPassword()), accessToken);
        }
    }

    //Успешное создание уникального пользователя
    @Test
    public void createUniqueUser() {
        testUser = new User(ApiSteps.generateUniqueEmail(), TestConstants.PASSWORD, TestConstants.TEST_USER_NAME);

        // Выполняем запрос на создание пользователя
        Response response = ApiSteps.createUser(testUser);

        // Проверяем, что код ответа 200 (успешно)
        checkResponseStatus(response, HTTP_OK);

        // Получаем токен для удаления пользователя
        accessToken = BaseTest.getAccessToken(testUser.getEmail(), testUser.getPassword());

        // Проверка полей ответа
        assertTrue(response.jsonPath().getBoolean(TestConstants.SUCCESS_FIELD));
        assertEquals(testUser.getEmail(), response.jsonPath().getString(TestConstants.FIELD_USER_EMAIL));
        assertEquals(testUser.getName(), response.jsonPath().getString(TestConstants.FIELD_USER_NAME));
    }

     //Попытка регистрации уже зарегистрированного пользователя.
    @Test
    public void createAlreadyRegisteredUser() {
        testUser = new User(ApiSteps.generateUniqueEmail(), TestConstants.PASSWORD, TestConstants.TEST_USER_NAME);

        // Создаем пользователя, чтобы гарантировать его существование
        Response response = ApiSteps.createUser(testUser);
        checkResponseStatus(response, HTTP_OK);  // Проверяем, что первый запрос на создание успешен

        // Получаем токен для удаления пользователя
        accessToken = BaseTest.getAccessToken(testUser.getEmail(), testUser.getPassword());

        // Пытаемся зарегистрировать пользователя повторно
        Response secondResponse = ApiSteps.createUser(testUser);

        // Проверяем, что код ответа 403 (Forbidden)
        checkResponseStatus(secondResponse, HTTP_FORBIDDEN);

        // Проверка сообщения об ошибке
        String errorMessage = secondResponse.jsonPath().getString(TestConstants.MESSAGE_FIELD);
        assertEquals(TestConstants.MESSAGE_USER_EXISTS, errorMessage);
    }

     //Создание пользователя без обязательного поля (name).
    @Test
    public void createUserWithoutRequiredField() {
        // Создаем пользователя без имени
        User userWithoutName = new User(ApiSteps.generateUniqueEmail(), TestConstants.PASSWORD, null);

        // Выполняем запрос на создание пользователя
        Response response = ApiSteps.createUser(userWithoutName);

        // Проверяем, что код ответа 403 (Forbidden)
        checkResponseStatus(response, HTTP_FORBIDDEN);

        // Проверка сообщения об ошибке
        String errorMessage = response.jsonPath().getString(TestConstants.MESSAGE_FIELD);
        assertEquals(TestConstants.MESSAGE_MISSING_FIELDS, errorMessage);
    }
}
