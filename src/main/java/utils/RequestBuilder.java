package utils;

public class RequestBuilder {

    // Метод для создания тела запроса с тремя параметрами (email, password, name)
    public static String buildRequestBody(String email, String password, String name) {
        if (name != null) {
            return String.format("{ \"email\": \"%s\", \"password\": \"%s\", \"name\": \"%s\" }", email, password, name);
        } else {
            return buildRequestBody(email, password);
        }
    }

    // Перегруженный метод для создания тела запроса с двумя параметрами (email и password)
    public static String buildRequestBody(String email, String password) {
        return String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);
    }
}
