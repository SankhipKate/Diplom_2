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
                .given().log().all()
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(user)  // Передаем объект User
                .post(TestConstants.ENDPOINT_REGISTER);
    }


    // Шаг для создания пользователя без имени
    @Step("Создание пользователя с email: {email}, password: {password}, без имени")
    public static Response createUserWithoutName(String email, String password) {
        // Выполняем POST запрос, пропустив поле "name"
        return RestAssured
                .given().log().all()
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
                .given().log().all()
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
                .given().log().all()
                .header(TestConstants.AUTHORIZATION_HEADER, TestConstants.BEARER_PREFIX + accessToken)
                .delete(TestConstants.ENDPOINT_USER);
    }

    // Метод для обновления данных пользователя
    @Step("Обновление данных пользователя")
    public static Response updateUser(String accessToken, User user) {
        return RestAssured
                .given().log().all()
                .header(TestConstants.AUTHORIZATION_HEADER, accessToken) // Передача токена
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(RequestBuilder.buildRequestBody(user.getEmail(), user.getPassword(), user.getName()))
                .put(TestConstants.ENDPOINT_USER);
    }

    // Обновление данных пользователя с авторизацией
    @Step("Обновление данных пользователя с авторизацией")
    public static Response updateUserWithAuthorization(String accessToken, User updatedUser) {
        return RestAssured
                .given().log().all()
                .header("Authorization", accessToken)
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(updatedUser)  // Передаем объект User, который будет автоматически сериализован в JSON
                .patch(TestConstants.ENDPOINT_USER);
    }

    // Обновление данных пользователя без авторизации
    @Step("Обновление данных пользователя без авторизации")
    public static Response updateUserWithoutAuthorization(String email, String name) {
        return RestAssured
                .given().log().all()
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(RequestBuilder.buildRequestBody(email, TestConstants.PASSWORD, name))
                .patch(TestConstants.ENDPOINT_USER);
    }

}
