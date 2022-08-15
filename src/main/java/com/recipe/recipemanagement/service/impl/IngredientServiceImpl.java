package com.recipe.recipemanagement.service.impl;

import com.recipe.recipemanagement.entity.IngredientEntity;
import com.recipe.recipemanagement.model.Ingredient;
import com.recipe.recipemanagement.repository.IngredientRepository;
import com.recipe.recipemanagement.service.IngredientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShubhamKumar
 * Service class to deal with Ingredients
 */
@Service
 public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    /**
     * @param ingredientRepository IngredientRepository
     */
    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * This method returns all available Ingredients from the system .
     * As we are assuming we are dealing with limited number data Upto 1000,so we using find all method.
     * If data is in bigger number we should move to Pagination or Elastic Search.
     *
     * @return List of available Ingredients
     */
    public List<Ingredient> getIngredients() {

        return ingredientRepository.findAll().stream().map(ingredientEntity -> {
            var ingredientDto = new Ingredient();
            ingredientDto.setId(ingredientEntity.getId());
            ingredientDto.setName(ingredientEntity.getName());
            ingredientDto.setDescription(ingredientEntity.getDescription());
            return ingredientDto;
        }).collect(Collectors.toList());
    }

    /**
     * This method return the details of the given Ingredient id.
     * If Ingredient is not available then EntityNotFoundException is thrown with http status code 404
     *
     * @param ingredientId - Ingredient Id
     * @return Ingredient details
     */
    public Ingredient getIngredient(Long ingredientId) {

        var ingredientEntity = ingredientRepository.findById(ingredientId)
        		.orElseThrow(() -> 
        new EntityNotFoundException(
        		String.join("","The Ingredient id {" + ingredientId + "} is not present")));//TODO change it as per  line no :93
        var ingredient = new Ingredient();
        ingredient.setId(ingredientEntity.getId());
        ingredient.setName(ingredientEntity.getName());
        ingredient.setDescription(ingredientEntity.getDescription());
        return ingredient;
    }

    /**
     * This method add a new Ingredient.
     *
     * @param ingredient - Ingredient details to be stored
     * @return Ingredient ID
     */
    public Long createIngredient(Ingredient ingredient) {

        var ingredientEntity = new IngredientEntity();
        ingredientEntity.setName(ingredient.getName());
        ingredientEntity.setDescription(ingredient.getDescription());
        return ingredientRepository.save(ingredientEntity).getId();
    }

    /**
     * This method update the existing Ingredient
     *
     * @param ingredient - Ingredient details to be updated
     */
    public void updateIngredient(Ingredient ingredient) {
        var ingredientEntity = ingredientRepository.findById(ingredient.getId())
        		.orElseThrow(() -> 
        		new EntityNotFoundException(
        				String.join(" ","The Ingredient id {", ingredient.getId().toString() ,"} is not present")));
        ingredientEntity.setName(ingredient.getName());
        ingredientEntity.setDescription(ingredient.getDescription());
        ingredientRepository.save(ingredientEntity);
    }
}
