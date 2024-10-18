package utils;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Order;
import models.User;
import models.UserCredentials;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static java.net.HttpURLConnection.*;

/**
 * Класс с шагами для выполнения API запросов
 */

public class ApiSteps {

    // Генерация уникального email
    @Step("Генерация уникального email")
    public static String generateUniqueEmail() {
        return TestConstants.UNIQUE_EMAIL_PREFIX + System.currentTimeMillis() + TestConstants.EMAIL_DOMAIN;
    }

    // Создание пользователя
    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return RestAssured
                .given().log().all()
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(user)  // Передаем объект User
                .post(TestConstants.ENDPOINT_REGISTER);
    }

    // Логин пользователя
    @Step("Логин пользователя")
    public static Response loginUser(UserCredentials credentials) {
        return RestAssured
                .given().log().all()
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(credentials)  // Передаем объект UserCredentials
                .post(TestConstants.ENDPOINT_LOGIN);
    }

    // Удаление пользователя
    @Step("Удаление пользователя")
    public static void deleteUser(UserCredentials credentials, String accessToken) {
        // Печатаем токен для проверки
        System.out.println("Access Token: " + accessToken);
        RestAssured
                .given().log().all()
                .header(TestConstants.AUTHORIZATION_HEADER, accessToken)
                .delete(TestConstants.ENDPOINT_USER)
                .then()
                .statusCode(anyOf(is(HTTP_OK), is(HTTP_ACCEPTED))); // Проверка успешного удаления с кодами 200 или 202
    }

    // Обновление данных пользователя с авторизацией
    @Step("Обновление данных пользователя с авторизацией")
    public static Response updateUserWithAuthorization(String accessToken, User updatedUser) {
        return RestAssured
                .given().log().all()
                .header(TestConstants.AUTHORIZATION_HEADER, accessToken)
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(updatedUser)  // Передаем объект User, который будет автоматически сериализован в JSON
                .patch(TestConstants.ENDPOINT_USER);
    }

    // Обновление данных пользователя без авторизации
    @Step("Обновление данных пользователя без авторизации")
    public static Response updateUserWithoutAuthorization(User user) {
        return RestAssured
                .given().log().all()
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(user)  // Передаем объект User
                .patch(TestConstants.ENDPOINT_USER);
    }

    // Получение списка ингредиентов
    @Step("Получение списка ингредиентов")
    public static String[] getIngredients() {
        Response response = RestAssured
                .given().log().all()
                .get(TestConstants.ENDPOINT_INGREDIENTS);

        response.then().statusCode(HTTP_OK); // Проверка статуса ответа
        return response.jsonPath().getList(TestConstants.FIELD_INGREDIENTS, String.class).toArray(new String[0]);
    }

    // Создание заказа
    @Step("Создание заказа")
    public static Response createOrder(String[] ingredients, String accessToken) {
        return RestAssured
                .given().log().all()
                .header(TestConstants.AUTHORIZATION_HEADER, accessToken)
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(new Order(ingredients)) // Подаем ингредиенты
                .post(TestConstants.ENDPOINT_ORDERS);
    }

    // Получение заказов с авторизацией
    @Step("Получение заказов пользователя с авторизацией")
    public static Response getUserOrdersWithAuthorization(String accessToken) {
        return RestAssured
                .given().log().all()
                .header(TestConstants.AUTHORIZATION_HEADER, accessToken)
                .get(TestConstants.ENDPOINT_ORDERS);
    }
    // Получение заказов без авторизации
    @Step("Получение заказов пользователя без авторизации")
    public static Response getUserOrdersWithoutAuthorization() {
        return RestAssured
                .given().log().all()
                .get(TestConstants.ENDPOINT_ORDERS);
    }

}
