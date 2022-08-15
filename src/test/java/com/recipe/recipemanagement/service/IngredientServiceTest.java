package com.recipe.recipemanagement.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recipe.recipemanagement.entity.IngredientEntity;
import com.recipe.recipemanagement.model.Ingredient;
import com.recipe.recipemanagement.repository.IngredientRepository;
import com.recipe.recipemanagement.service.impl.IngredientServiceImpl;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {
	@Mock
	private IngredientRepository ingredientRepository;

	@InjectMocks
	private IngredientServiceImpl ingredientService;

	@Test
	@DisplayName("Retrieve all ingredient")
	void getRecipes() {
		List<IngredientEntity> list = new ArrayList<>();
		list.add(getOilIngredientEntity());
		Mockito.when(ingredientRepository.findAll()).thenReturn(list);
		List<Ingredient> result = ingredientService.getIngredients();
		Assertions.assertEquals(result.size(), 1);

	}

	@Test
	@DisplayName("Retrieve ingredient with the ingredientId")
	void getRecipe() {
		Optional<IngredientEntity> optionalRecipeEntity = Optional.of(getOilIngredientEntity());

		Mockito.when(ingredientRepository.findById(1L)).thenReturn(optionalRecipeEntity);
		Ingredient ingredient = ingredientService.getIngredient(1L);
		Assertions.assertEquals(ingredient.getName(), "Oil");
	}

	@Test
	@DisplayName("Update ingredient")
	void updateRecipe() {
		Optional<IngredientEntity> optionalRecipeEntity = Optional.of(getOilIngredientEntity());
		Mockito.when(ingredientRepository.findById(1L)).thenReturn(optionalRecipeEntity);
		ingredientService.updateIngredient(getSaltIngredient());
		verify(ingredientRepository, times(1)).save(Mockito.any(IngredientEntity.class));
	}

	@Test
	@DisplayName("Add ingredient")
	void addRecipe() {
		Mockito.when(ingredientRepository.save(Mockito.any(IngredientEntity.class)))
				.thenReturn(getOilIngredientEntity());
		Long recipeID = ingredientService.createIngredient(getSaltIngredient());
		Assertions.assertEquals(recipeID, 2L);
	}

	private IngredientEntity getOilIngredientEntity() {
		IngredientEntity oil = new IngredientEntity();
		oil.setId(2L);
		oil.setName("Oil");
		oil.setDescription("Oil");

		return oil;
	}

	private Ingredient getSaltIngredient() {

		Ingredient salt = new Ingredient();
		salt.setId(1L);
		salt.setName("Salt");
		salt.setDescription("Salt");
		return salt;
	}
}