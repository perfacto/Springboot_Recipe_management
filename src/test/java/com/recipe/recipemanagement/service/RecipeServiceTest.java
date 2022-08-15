package com.recipe.recipemanagement.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recipe.recipemanagement.entity.IngredientEntity;
import com.recipe.recipemanagement.entity.RecipeEntity;
import com.recipe.recipemanagement.entity.RecipeIngredientEntity;
import com.recipe.recipemanagement.entity.RecipeIngredientId;
import com.recipe.recipemanagement.enums.ItemType;
import com.recipe.recipemanagement.mapper.RecipeMapper;
import com.recipe.recipemanagement.model.Ingredient;
import com.recipe.recipemanagement.model.Recipe;
import com.recipe.recipemanagement.repository.RecipeRepository;
import com.recipe.recipemanagement.service.impl.RecipeServiceImpl;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

	@Mock
	private RecipeRepository recipeRepository;

	@InjectMocks
	private RecipeServiceImpl recipeService;

	@Spy
	private RecipeMapper recipeMapper;

	@Test
	@DisplayName("Retrieve all recipes")
	void getRecipes() {
		List<RecipeEntity> list = new ArrayList<>();
		list.add(getPizzaRecipeEntity());
		Mockito.when(recipeRepository.findAll()).thenReturn(list);
		List<Recipe> result = recipeService.findRecipes(Collections.emptyList());
		Assertions.assertEquals(result.size(), 1);

	}

	@Test
	@DisplayName("Retrieve recipe with the recipeId")
	void getRecipe() {
		Optional<RecipeEntity> optionalRecipeEntity = Optional.of(getPizzaRecipeEntity());

		Mockito.when(recipeRepository.findById(1L)).thenReturn(optionalRecipeEntity);
		Recipe recipe = recipeService.getRecipe(1L);
		Assertions.assertEquals(recipe.getName(), "Pizza");
		Assertions.assertEquals(recipe.getServing(), 4);
	}

	@Test
	@DisplayName("Update recipe")
	void updateRecipe() {
		Optional<RecipeEntity> optionalRecipeEntity = Optional.of(getPizzaRecipeEntity());
		Mockito.when(recipeRepository.findById(1L)).thenReturn(optionalRecipeEntity);
		recipeService.updateRecipe(getPizzaRecipe());
		verify(recipeRepository, times(1)).save(Mockito.any(RecipeEntity.class));
	}

	@Test
	@DisplayName("Add recipe")
	void addRecipe() {
		Mockito.when(recipeRepository.save(Mockito.any(RecipeEntity.class))).thenReturn(getPizzaRecipeEntity());
		Long recipeID = recipeService.addRecipe(getPizzaRecipe());
		Assertions.assertEquals(recipeID, 1L);
	}

	@Test
	@DisplayName("Delete recipe")
	void deleteRecipe() {
		recipeService.deleteRecipe(1L);
		verify(recipeRepository, times(1)).deleteById(1L);
	}

	private RecipeEntity getPizzaRecipeEntity() {
		RecipeEntity pizza = new RecipeEntity();

		pizza.setId(1L);
		pizza.setItemType(ItemType.VEG.name());
		pizza.setDescription("Chicken Pizza");
		pizza.setCookingInstructions("Instructions");
		pizza.setServing(4);
		pizza.setName("Pizza");
		IngredientEntity oil = new IngredientEntity();
		oil.setId(2L);
		oil.setName("Oil");
		oil.setDescription("Oil");
		RecipeIngredientEntity salt = new RecipeIngredientEntity();
		RecipeIngredientId recipeIngredientId = new RecipeIngredientId();
		recipeIngredientId.setIngredient(oil);
		recipeIngredientId.setRecipe(pizza);
		salt.setRecipeIngredientId(recipeIngredientId);
		salt.setQuantity(5.9);

		List<RecipeIngredientEntity> ingredientDtos = new ArrayList<>();
		ingredientDtos.add(salt);

		pizza.setRecipeIngredients(ingredientDtos);
		return pizza;
	}

	private Recipe getPizzaRecipe() {
		Recipe pizza = new Recipe();

		pizza.setId(1L);
		pizza.setItemType(ItemType.VEG);
		pizza.setDescription("Chicken Pizza");
		pizza.setCookingInstructions("Instructions");
		pizza.setServing(4);
		pizza.setName("Pizza");

		Ingredient salt = new Ingredient();
		salt.setId(1L);
		salt.setName("Salt");
		salt.setDescription("Salt");

		Ingredient oil = new Ingredient();
		oil.setId(2L);
		oil.setName("Oil");
		oil.setDescription("Oil");

		List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(salt);
		ingredients.add(oil);

		pizza.setIngredients(ingredients);
		return pizza;
	}
}