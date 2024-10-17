package utils;


public class TestConstants {
    // Базовый URL
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    // Константы для полей JSON
    public static final String SUCCESS_FIELD = "success";
    public static final String MESSAGE_FIELD = "message";
    public static final String FIELD_USER_EMAIL = "user.email";
    public static final String FIELD_USER_NAME = "user.name";
    public static final String FIELD_ACCESS_TOKEN = "accessToken";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";

    // Сообщения об ошибках
    public static final String MESSAGE_USER_EXISTS = "User already exists";
    public static final String MESSAGE_MISSING_FIELDS = "Email, password and name are required fields";
    public static final String MESSAGE_INVALID_CREDENTIALS = "email or password are incorrect";

    // Контент-тип
    public static final String CONTENT_TYPE_JSON = "application/json";

    // Эндпоинты
    public static final String ENDPOINT_REGISTER = "/api/auth/register";
    public static final String ENDPOINT_LOGIN = "/api/auth/login";
    public static final String ENDPOINT_USER = "/api/auth/user";

    // Префиксы и заголовки
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";


    // Тестовые данные
    public static final String PASSWORD = "password";
    public static final String INVALID_PASSWORD = "wrong_password";
    public static final String TEST_USER_NAME = "UniqueUser";
    public static final String EXISTING_USER_NAME = "ExistingUser";
    public static final String EMAIL_MISSING_NAME = "user_without_name@test.com";
    public static final String EXISTING_USER_EMAIL = "existing_user@test.com";
    public static final String NON_EXISTENT_EMAIL = "nonexistent_user@test.com";

    // Таймауты для HTTP-запросов
    public static final int CONNECTION_TIMEOUT = 5000; // Таймаут соединения (в миллисекундах)
    public static final int SOCKET_TIMEOUT = 5000;     // Таймаут ожидания ответа (в миллисекундах)

    // HTTP параметры
    public static final String HTTP_CONNECTION_TIMEOUT = "http.connection.timeout";
    public static final String HTTP_SOCKET_TIMEOUT = "http.socket.timeout";

    // Лог-сообщения
    public static final String LOG_USER_CREATED = "Успешно создан пользователь с email: ";
    public static final String LOG_ERROR_MESSAGE = "Получено сообщение об ошибке: ";
    public static final String LOG_USER_LOGIN_SUCCESS = "Успешный логин пользователя с email: ";
    public static final String LOG_USER_LOGIN_FAILURE = "Ошибка логина для пользователя с email: ";

    // Email
    public static final String UNIQUE_EMAIL_PREFIX = "unique_user_";
    public static final String EMAIL_DOMAIN = "@test.com";
}