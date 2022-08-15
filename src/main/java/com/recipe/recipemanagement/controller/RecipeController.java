package com.recipe.recipemanagement.controller;

import com.recipe.recipemanagement.model.Recipe;
import com.recipe.recipemanagement.model.SearchCriteria;
import com.recipe.recipemanagement.service.RecipeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ShubhamKumar
 * This REST API is to accept the user requests like create, update, delete and get recipes,
 * validate the user request and forward the call to the service for further processing
 */
@RestController
@RequestMapping("/recipes")
@Validated//Necessary to validate the List<@Valid SearchCriteria>, @Valid can't validate the collections in line#72
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * This method return the details of the given recipe id.
     * If recipe is not then EntityNotFoundException is thrown with http status code 404
     *
     * @param recipeId - Recipe Id
     * @return Recipe details
     */
    @GetMapping("{recipeId}")
    public Recipe getRecipe(@PathVariable Long recipeId) {
        return recipeService.getRecipe(recipeId);
    }

    /**
     * This method update the existing recipe
     *
     * @param recipeId    - Recipe ID
     * @param recipeModel - recipe details to be updated
     */
    @PutMapping("{recipeId}")
    public void updateRecipe(@PathVariable Long recipeId, @Valid @RequestBody Recipe recipeModel) {
        recipeModel.setId(recipeId);//Make sure to set the correct id
        recipeService.updateRecipe(recipeModel);
    }

    /**
     * This method add a new recipe.
     *
     * @param recipeModel - Recipe details to be stored
     * @return Recipe ID
     */
    @PostMapping
    public Long addRecipe(@Valid @RequestBody Recipe recipeModel) {
        return recipeService.addRecipe(recipeModel);
    }

    /**
     * This method is to search recipes.
     *
     * @param searchParams - Recipe details to be stored
     * @return Recipe ID
     */
    @PostMapping(value = "search")
    public List<Recipe> searchRecipes(@RequestBody(required = false) List<@Valid SearchCriteria> searchParams) {
        return recipeService.findRecipes(searchParams);
    }

    /**
     * This method deletes the recipe details of the given recipeId
     *
     * @param recipeId - Recipe Id
     */
    @DeleteMapping("{recipeId}")
    public void deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
    }
}
