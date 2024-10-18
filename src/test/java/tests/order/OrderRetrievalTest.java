package tests.order;

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
 * Тесты для получения заказов пользователя в системе Stellar Burgers.
 */
public class OrderRetrievalTest extends BaseTest {

    private String accessToken; // Токен для авторизации пользователя
    private User testUser; // Тестовый пользователь

    @Before
    public void setUp() {
        // Регистрируем пользователя
        testUser = new User(ApiSteps.generateUniqueEmail(), TestConstants.PASSWORD, TestConstants.TEST_USER_NAME);
        ApiSteps.createUser(testUser);

        // Логинимся и получаем токен
        accessToken = getAccessToken(testUser.getEmail(), testUser.getPassword());
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после тестов
        if (accessToken != null && testUser != null) {
            deleteUserAfterTest(new UserCredentials(testUser.getEmail(), testUser.getPassword()), accessToken);
        }
    }

    // Тест: получение заказов авторизованным пользователем
    @Test
    public void getUserOrdersWithAuthorization() {
        // Создаем заказ для авторизованного пользователя
        String[] ingredients = ApiSteps.getIngredients();
        Response createOrderResponse = ApiSteps.createOrder(ingredients, accessToken);
        checkResponseStatus(createOrderResponse, HTTP_OK);

        // Выполняем запрос на получение заказов с авторизацией
        Response response = ApiSteps.getUserOrdersWithAuthorization(accessToken);

        // Проверяем, что код ответа 200
        checkResponseStatus(response, HTTP_OK);

        // Проверяем, что список заказов не пуст
        assertFalse(TestConstants.ORDERS_LIST_EMPTY_MESSAGE, response.jsonPath().getList(TestConstants.FIELD_ORDERS).isEmpty());
    }

    // Тест: получение заказов неавторизованным пользователем
    @Test
    public void getUserOrdersWithoutAuthorization() {
        // Создаем заказ для авторизованного пользователя
        String[] ingredients = ApiSteps.getIngredients();
        Response createOrderResponse = ApiSteps.createOrder(ingredients, accessToken);
        checkResponseStatus(createOrderResponse, HTTP_OK);

        // Выполняем запрос на получение заказов без авторизации
        Response response = ApiSteps.getUserOrdersWithoutAuthorization();

        // Проверяем, что код ответа 401
        checkResponseStatus(response, HTTP_UNAUTHORIZED);

        // Проверяем сообщение об ошибке
        String errorMessage = response.jsonPath().getString(TestConstants.MESSAGE_FIELD);
        assertEquals(TestConstants.MESSAGE_UNAUTHORIZED, errorMessage);
    }
}
