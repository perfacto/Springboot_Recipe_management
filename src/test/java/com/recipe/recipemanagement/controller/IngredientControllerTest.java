package com.recipe.recipemanagement.controller;

import com.recipe.recipemanagement.model.Ingredient;
import com.recipe.recipemanagement.service.IngredientService;
import com.recipe.recipemanagement.service.impl.IngredientServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    private IngredientServiceImpl ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    @Test
    @DisplayName("Retrieve all ingredients")
    void getIngredients() {
        List<Ingredient> list = getIngredientList();
        Mockito.when(ingredientService.getIngredients()).thenReturn(list);
        List<Ingredient> result = ingredientController.getIngredients();
        Assertions.assertEquals(result.size(), 2);

    }

    @Test
    @DisplayName("Retrieve ingredient with id")
    void getIngredient() {
        Mockito.when(ingredientService.getIngredient(1L)).thenReturn(getIngredientList().get(0));
        Ingredient result = ingredientController.getIngredient(1L);
        Assertions.assertEquals(result.getName(), "Salt");
    }

    @Test
    @DisplayName("Update ingredient")
    void updateIngredient() {
        ingredientController.updateIngredient(1L, getIngredientList().get(0));
        verify(ingredientService, times(1)).updateIngredient(Mockito.any(Ingredient.class));
    }

    @Test
    @DisplayName("Add ingredient")
    void addIngredient() {

        Mockito.when(ingredientService.createIngredient(getIngredientList().get(0))).thenReturn(1L);
        Long recipeID = ingredientController.addIngredient(getIngredientList().get(0));
        Assertions.assertEquals(recipeID, 1L);
    }

    private List<Ingredient> getIngredientList() {
        List<Ingredient> ingredients = new ArrayList<>();

        Ingredient salt = new Ingredient();
        salt.setId(1L);
        salt.setName("Salt");
        salt.setDescription("Salt");

        Ingredient oil = new Ingredient();
        oil.setId(2L);
        oil.setName("Oil");
        oil.setDescription("Oil");
        ingredients.add(salt);
        ingredients.add(oil);

        return ingredients;
    }
}