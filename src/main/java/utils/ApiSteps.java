package utils;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import models.UserCredentials;

import static java.net.HttpURLConnection.*;

/**
 * Класс с шагами для выполнения API запросов.
 */
public class ApiSteps {

    // Создание пользователя
    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return RestAssured
                .given()
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(user)  // Передаем объект User
                .post(TestConstants.ENDPOINT_REGISTER);
    }


    // Шаг для создания пользователя без имени
    @Step("Создание пользователя с email: {email}, password: {password}, без имени")
    public static Response createUserWithoutName(String email, String password) {
        // Выполняем POST запрос, пропустив поле "name"
        return RestAssured
                .given()
                .contentType(TestConstants.CONTENT_TYPE_JSON)  // Устанавливаем тип контента JSON
                .body(RequestBuilder.buildRequestBody(email, password, null))  // Тело запроса без имени
                .post(TestConstants.ENDPOINT_REGISTER);  // Выполняем POST запрос на эндпоинт регистрации
    }

    // Генерация уникального email
    @Step("Генерация уникального email")
    public static String generateUniqueEmail() {
        return TestConstants.UNIQUE_EMAIL_PREFIX + System.currentTimeMillis() + TestConstants.EMAIL_DOMAIN;
    }

    // Логин пользователя
    @Step("Логин пользователя")
    public static Response loginUser(UserCredentials credentials) {
        return RestAssured
                .given()
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(credentials)  // Передаем объект UserCredentials
                .post(TestConstants.ENDPOINT_LOGIN);
    }

    // Удаление пользователя
    @Step("Удаление пользователя")
    public static void deleteUser(UserCredentials credentials) {
        String accessToken = loginUser(credentials)
                .then()
                .extract()
                .path(TestConstants.FIELD_ACCESS_TOKEN);
        RestAssured
                .given()
                .header(TestConstants.AUTHORIZATION_HEADER, TestConstants.BEARER_PREFIX + accessToken)
                .delete(TestConstants.ENDPOINT_USER);
    }

}
