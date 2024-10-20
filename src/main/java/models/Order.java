package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {

    private List<String> ingredients;

    // Конструктор, принимающий массив ингредиентов
    public Order(String[] ingredients) {
        this.ingredients = new ArrayList<>(Arrays.asList(ingredients));
    }

    // Геттер для ингредиентов
    public List<String> getIngredients() {
        return ingredients;
    }

    // Сеттер для ингредиентов
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
