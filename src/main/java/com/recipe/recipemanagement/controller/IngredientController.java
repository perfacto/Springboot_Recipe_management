package com.recipe.recipemanagement.controller;

import com.recipe.recipemanagement.model.Ingredient;
import com.recipe.recipemanagement.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author ShubhamKumar
 * REST API to create, update and Get Ingredient details
 */
@RestController
@RequestMapping("ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * This method returns all available Ingredients from the system
     *
     * @return List of available Ingredients
     */
    @GetMapping
    public List<Ingredient> getIngredients() {
        return ingredientService.getIngredients();
    }

    /**
     * This method return the details of the given Ingredient id.
     * If Ingredient is not available then EntityNotFoundException is thrown with http status code 404
     *
     * @param ingredientId - Ingredient Id
     * @return Ingredient details
     */
    @GetMapping("{ingredientId}")
    public Ingredient getIngredient(@PathVariable @Positive Long ingredientId) {
        return ingredientService.getIngredient(ingredientId);
    }

    /**
     * This method update the existing Ingredient
     *
     * @param ingredientId  - Ingredient ID
     * @param ingredient - Ingredient details to be updated
     */
    @PutMapping("{ingredientId}")
    public void updateIngredient(@PathVariable Long ingredientId, @RequestBody @Valid Ingredient ingredient) {
        ingredient.setId(ingredientId);
        ingredientService.updateIngredient(ingredient);
    }

    /**
     * This method add a new Ingredient.
     *
     * @param ingredient - Ingredient details to be stored
     * @return Ingredient ID
     */
    @PostMapping
    public Long addIngredient(@RequestBody @Valid Ingredient ingredient) {
        return ingredientService.createIngredient(ingredient);
    }
}
