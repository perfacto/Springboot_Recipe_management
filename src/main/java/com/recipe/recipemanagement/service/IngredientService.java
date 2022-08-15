package com.recipe.recipemanagement.service;

import java.util.List;

import com.recipe.recipemanagement.model.Ingredient;

/**
 * @author ShubhamKumar 
 * Interface to deal with Ingredients
 */
public interface IngredientService {
	/**
	 * This method returns all available Ingredients from the system .
	 * As we are assuming we are dealing with limited number data Upto 1000,so we using find all method.
	 * If data is in bigger number we should move to Pagination or Elastic Search.
	 *
	 * @return List of available Ingredients
	 */
	public List<Ingredient> getIngredients();
	/**
	 * This method return the details of the given Ingredient id.
	 * If Ingredient is not available then EntityNotFoundException is thrown with http status code 404
	 *
	 * @param ingredientId - Ingredient Id
	 * @return Ingredient details
	 */

	public Ingredient getIngredient(Long ingredientId);
	/**
	 * This method add a new Ingredient.
	 *
	 * @param ingredient - Ingredient details to be stored
	 * @return Ingredient ID
	 */

	public Long createIngredient(Ingredient ingredient);

	/**
	 * This method update the existing Ingredient
	 *
	 * @param ingredient - Ingredient details to be updated
	 */

	public void updateIngredient(Ingredient ingredient);
}
