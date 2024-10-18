package base;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import models.UserCredentials;
import org.junit.BeforeClass;
import utils.ApiSteps;
import utils.TestConstants;


/**
 * Базовый класс для всех тестов.
 * Здесь настраиваем базовый URL для RestAssured, а также предоставляем общие методы.
 */

public class BaseTest {

    //Метод выполняется один раз перед всеми тестами
    //Настраиваем базовый URI для API запросов

    @BeforeClass
    public static void setup() {
        // Настраиваем базовый URI для всех запросов RestAssured
        RestAssured.baseURI = TestConstants.BASE_URI;
        // Устанавливаем таймауты для всех тестов в проекте
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(TestConstants.HTTP_CONNECTION_TIMEOUT, TestConstants.CONNECTION_TIMEOUT)  // Таймаут соединения 5 секунд
                        .setParam(TestConstants.HTTP_SOCKET_TIMEOUT, TestConstants.SOCKET_TIMEOUT));    // Таймаут ожидания ответа 5 секунд
    }

    // Метод для получения accessToken для пользователя
    public static String getAccessToken(String email, String password) {
        UserCredentials credentials = new UserCredentials(email, password);

        Response response = RestAssured
                .given().log().all()
                .contentType(TestConstants.CONTENT_TYPE_JSON)
                .body(credentials)  // Передаем объект UserCredentials
                .post(TestConstants.ENDPOINT_LOGIN);

        return response
                .then()
                .extract()
                .path(TestConstants.FIELD_ACCESS_TOKEN);
    }

    // Вспомогательный метод для проверки статуса ответа
    protected void checkResponseStatus(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    //Удаление созданного пользователя
    protected void deleteUserAfterTest(UserCredentials credentials, String accessToken) {
        if (accessToken != null) {
            ApiSteps.deleteUser(credentials, accessToken);
        }
    }
}
