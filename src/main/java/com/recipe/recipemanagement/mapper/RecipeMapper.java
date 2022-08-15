package com.recipe.recipemanagement.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.recipe.recipemanagement.entity.IngredientEntity;
import com.recipe.recipemanagement.entity.RecipeEntity;
import com.recipe.recipemanagement.entity.RecipeIngredientEntity;
import com.recipe.recipemanagement.entity.RecipeIngredientId;
import com.recipe.recipemanagement.enums.ItemType;
import com.recipe.recipemanagement.model.Ingredient;
import com.recipe.recipemanagement.model.Recipe;

/**
 * @author ShubhamKumar Mapper class to map entity to model and mode to entity
 */
@Component
public class RecipeMapper {

	/**
	 * This method converts Recipe model to Recipe entity
	 *
	 * @param recipe       - RecipeDto
	 * @param recipeEntity - RecipeEntity
	 */
	public void modelToEntity(Recipe recipe, RecipeEntity recipeEntity) {

		recipeEntity.setDescription(recipe.getDescription());
		recipeEntity.setCookingInstructions(recipe.getCookingInstructions());
		recipeEntity.setItemType(recipe.getItemType().name());
		recipeEntity.setName(recipe.getName());
		recipeEntity.setServing(recipe.getServing());
		var list = recipe.getIngredients().stream()
				.map(ingredient -> getRecipeIngredientEntity(ingredient,recipeEntity))
				.collect(Collectors.toList());
		recipeEntity.setRecipeIngredients(list);
	}

	/**
	 * This method converts Entity to Model
	 *
	 * @param recipeEntity - RecipeEntity
	 * @return Recipe
	 */
	public Recipe entityToModel(RecipeEntity recipeEntity) {
		Recipe recipe = new Recipe();
		recipe.setId(recipeEntity.getId());
		recipe.setCookingInstructions(recipeEntity.getCookingInstructions());
		recipe.setCreatedOn(recipeEntity.getCreatedOn());
		recipe.setDescription(recipeEntity.getDescription());
		recipe.setItemType(ItemType.valueOf(recipeEntity.getItemType()));
		recipe.setName(recipeEntity.getName());
		recipe.setServing(recipeEntity.getServing());
		var recipeIngredientEntities = recipeEntity.getRecipeIngredients();
		List<Ingredient> recipeIngredients = new ArrayList<>();
		recipeIngredientEntities
				.forEach(recipeIngredientEntity -> 
				recipeIngredients.add(getIngredient(recipeIngredientEntity)));
		recipe.setIngredients(recipeIngredients);
		return recipe;
	}

	private RecipeIngredientEntity getRecipeIngredientEntity(Ingredient ingredient,RecipeEntity recipeEntity) {

		var recipeIngredientEntity = new RecipeIngredientEntity();
		var recipeIngredientId = new RecipeIngredientId();
		recipeIngredientId.setRecipe(recipeEntity);
		IngredientEntity ingredientEntity = new IngredientEntity();
		ingredientEntity.setId(ingredient.getId());
		ingredientEntity.setName(ingredient.getName());
		ingredientEntity.setDescription(ingredient.getDescription());
		recipeIngredientId.setIngredient(ingredientEntity);
		recipeIngredientEntity.setQuantity(ingredient.getQuantity());
		recipeIngredientEntity.setRecipeIngredientId(recipeIngredientId);
		return recipeIngredientEntity;
	}

	private Ingredient getIngredient(RecipeIngredientEntity recipeIngredientEntity) {
		var ingredient = new Ingredient();
		ingredient.setId(recipeIngredientEntity.getRecipeIngredientId().getIngredient().getId());
		ingredient.setDescription(recipeIngredientEntity.getRecipeIngredientId().getIngredient().getDescription());
		ingredient.setName(recipeIngredientEntity.getRecipeIngredientId().getIngredient().getName());
		ingredient.setQuantity(recipeIngredientEntity.getQuantity());
		return ingredient;
		
	}
}
