package com.recipe.recipemanagement.service;

import java.util.List;



import com.recipe.recipemanagement.model.Recipe;
import com.recipe.recipemanagement.model.SearchCriteria;

/**
 * @author ShubhamKumar
 * Service class to deal with Ingredients
 */

public interface RecipeService {

	/**
	 * This method retrieve recipes based on the given name, if no name is present then all available recipes will retrieve
	 *
	 * @return - List of recipes
	 */
	List<Recipe> findRecipes(List<SearchCriteria> searchParams);

	/**
	 * This method retrieve the recipe details of the given recipe id
	 *
	 * @param recipeId - Recipe Id
	 * @return - Recipe Details
	 */
	Recipe getRecipe(Long recipeId);

	/**
	 * This method is to update the recipe
	 *
	 * @param recipeModel - Recipe details to update
	 */
	void updateRecipe(Recipe recipeModel);

	/**
	 * This method is to create new recipe
	 *
	 * @param recipeModel Recipe model
	 * @return recipe Id
	 */
	Long addRecipe(Recipe recipeModel);

	/**
	 * This method is to delete the recipe details of the given recipe id
	 *
	 * @param recipeId - Recipe ID
	 */
	void deleteRecipe(Long recipeId);

}