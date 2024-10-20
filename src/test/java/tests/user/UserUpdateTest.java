package tests.user;

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
 * Тесты для изменения данных пользователя в системе Stellar Burgers.
 */
public class UserUpdateTest extends BaseTest {

    private String accessToken; // Токен авторизованного пользователя
    private User testUser; // Данные созданного пользователя

    @Before
    public void setUp() {
        // Создаем пользователя перед тестами и авторизуем его
        testUser = new User(ApiSteps.generateUniqueEmail(), TestConstants.PASSWORD, TestConstants.TEST_USER_NAME);
        ApiSteps.createUser(testUser);
        accessToken = getAccessToken(testUser.getEmail(), testUser.getPassword());
    }

    @After
    public void tearDown() {
        //Удаление созданного пользователя после каждого теста
        if (testUser != null && accessToken != null) {
            deleteUserAfterTest(new UserCredentials(testUser.getEmail(), testUser.getPassword()), accessToken);
        }
    }

    // Тест: успешное обновление данных с авторизацией
    @Test
    public void updateUserDataWithAuthorization() {
        // Создаем пользователя
        User updatedUser = new User(ApiSteps.generateUniqueEmail(), TestConstants.PASSWORD, TestConstants.UPDATED_USER_NAME);

        // Выполняем запрос на обновление данных пользователя с авторизацией
        Response response = ApiSteps.updateUserWithAuthorization(accessToken, updatedUser);

        // Проверяем, что код ответа 200
        checkResponseStatus(response, HTTP_OK);

        // Проверяем, что данные обновлены
        assertEquals(updatedUser.getEmail(), response.jsonPath().getString(TestConstants.FIELD_USER_EMAIL));
        assertEquals(updatedUser.getName(), response.jsonPath().getString(TestConstants.FIELD_USER_NAME));
    }


    // Тест: попытка изменения данных без авторизации
    @Test
    public void updateUserDataWithoutAuthorization() {

        // Создаем нового пользователя
        User updatedUser = new User(ApiSteps.generateUniqueEmail(), TestConstants.PASSWORD, TestConstants.UPDATED_USER_NAME);

        // Выполняем запрос на обновление данных пользователя без авторизации
        Response response = ApiSteps.updateUserWithoutAuthorization(updatedUser);

        // Проверяем, что код ответа 401 Unauthorized
        checkResponseStatus(response, HTTP_UNAUTHORIZED);

        // Проверяем сообщение об ошибке
        String errorMessage = response.jsonPath().getString(TestConstants.MESSAGE_FIELD);
        assertEquals(TestConstants.MESSAGE_UNAUTHORIZED, errorMessage);
    }
}
