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
 * Тесты для создания заказов в системе Stellar Burgers.
 */
public class OrderCreationTest extends BaseTest {

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

    // Успешное создание заказа
    @Test
    public void createOrderSuccessfully() {
        // Получаем ингредиенты для заказа
        String[] ingredients = ApiSteps.getIngredients();

        // Выполняем запрос на создание заказа
        Response response = ApiSteps.createOrder(ingredients, accessToken);

        // Проверяем, что код ответа 200
        checkResponseStatus(response, HTTP_OK);

        // Проверяем, что заказ успешно создан
        assertTrue(response.jsonPath().getBoolean(TestConstants.SUCCESS_FIELD));
        assertNotNull(response.jsonPath().getString(TestConstants.FIELD_ORDER_NUMBER));
    }

    // Попытка создания заказа без ингредиентов
    @Test
    public void createOrderWithoutIngredients() {
        // Пытаемся создать заказ без ингредиентов
        Response response = ApiSteps.createOrder(new String[]{}, accessToken);

        // Проверяем, что код ответа 400
        checkResponseStatus(response, HTTP_BAD_REQUEST);

        // Проверяем сообщение об ошибке
        String errorMessage = response.jsonPath().getString(TestConstants.MESSAGE_FIELD);
        assertEquals(TestConstants.MESSAGE_NO_INGREDIENTS, errorMessage);
    }

    // Попытка создания заказа с невалидными ингредиентами
    @Test
    public void createOrderWithInvalidIngredients() {
        // Получаем валидные ингредиенты
        String[] validIngredients = ApiSteps.getIngredients();

        // Копируем валидные ингредиенты и немного изменяем хеши, чтобы сделать их невалидными
        String[] invalidIngredients = validIngredients.clone();
        invalidIngredients[0] = invalidIngredients[0].replace("a", "z"); // Изменяем один символ

        // Пытаемся создать заказ с невалидными ингредиентами
        Response response = ApiSteps.createOrder(invalidIngredients, accessToken);

        // Проверяем, что код ответа 500
        checkResponseStatus(response, HTTP_INTERNAL_ERROR);

        // Проверяем, что в теле ответа есть сообщение об ошибке
        String responseBody = response.getBody().asString();
        assertTrue(TestConstants.MESSAGE_INTERNAL_ERROR, responseBody.contains(TestConstants.MESSAGE_INTERNAL_ERROR));
    }
}
