package com.recipe.recipemanagement.service.impl;

import com.recipe.recipemanagement.entity.RecipeEntity;
import com.recipe.recipemanagement.mapper.RecipeMapper;
import com.recipe.recipemanagement.model.Recipe;
import com.recipe.recipemanagement.repository.RecipeRepository;
import com.recipe.recipemanagement.repository.RecipeSpecificationBuilder;
import com.recipe.recipemanagement.service.RecipeService;
import com.recipe.recipemanagement.model.SearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShubhamKumar
// * This class maps the user input to the entities and interact with the Repository to insert, update, delete and get recipes
 */
@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    /**
     * This method retrieve recipes based on the given name, if no name is present then all available recipes will retrieve
     *
     * @return - List of recipes
     */
    public List<Recipe> findRecipes(List<SearchCriteria> searchParams) {

        //If there are no search parameters then retrieve all recipes from the database.
        if (searchParams.isEmpty()) {
            return recipeRepository.findAll().stream().map(recipeMapper::entityToModel).collect(Collectors.toList());
        }
        //Remove includeIngredients and excludeIngredients search params from database retrieval, because JPA doesn't support collection elements parameter retrieval. This has to be filtered after fetching the result from the database.
        var excludeIngredientSearchParams = searchParams.stream()
                .filter(searchCriteria -> !searchCriteria.getKey().contains("includeIngredients"))
                .filter(searchCriteria -> !searchCriteria.getKey().contains("excludeIngredients"))
                .collect(Collectors.toList());

        //Create Specification with the given search query parameters except includeIngredients and excludeIngredients
        Specification<RecipeEntity> spec = new RecipeSpecificationBuilder(excludeIngredientSearchParams).build();

        //Retrieve all recipes based on the search query parameters except includeIngredients and excludeIngredients
        List<Recipe> listRecipe = recipeRepository.findAll(spec).stream().map(recipeMapper::entityToModel)
                .collect(Collectors.toList());

        //Exclude the selected ingredients from the above result
        searchParams.stream().filter(searchCriteria -> searchCriteria.getKey().equalsIgnoreCase("excludeIngredients"))
                .findFirst().map(searchCriteria -> Arrays.asList(searchCriteria.getValue().split(",")))
                .ifPresent(strings -> listRecipe.removeIf(recipe -> recipe.getIngredients().removeIf(ingredient -> strings.contains(ingredient.getName()))));

        //Keep only the selected ingredients from the above result
        searchParams.stream().filter(searchCriteria -> searchCriteria.getKey().equalsIgnoreCase("includeIngredients"))
                .findFirst().map(searchCriteria -> Arrays.asList(searchCriteria.getValue().split(",")))
                .ifPresent(strings -> listRecipe.removeIf(recipe -> recipe.getIngredients().removeIf(ingredient -> !strings.contains(ingredient.getName()))));

        return listRecipe;
    }

    /**
     * This method retrieve the recipe details of the given recipe id
     *
     * @param recipeId - Recipe Id
     * @return - Recipe Details
     */
    public Recipe getRecipe(Long recipeId) {
        //If no recipe found with the given recipe id then throw Entity Not found exception
        var recipeEntity = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("The Recipe id {" + recipeId + "} is not present"));

        //Map Recipe Entity to Recipe Model
        return recipeMapper.entityToModel(recipeEntity);
    }

    /**
     * This method is to update the recipe
     *
     * @param recipeModel - Recipe details to update
     */
    public void updateRecipe(Recipe recipeModel) {
        //If no recipe found with the given recipe id then throw Entity Not found exception
        var recipeEntity = recipeRepository.findById(recipeModel.getId()).orElseThrow(() -> new EntityNotFoundException("Recipe is not present"));
        //Map Recipe Entity to Recipe Model
        recipeMapper.modelToEntity(recipeModel, recipeEntity);
        //Save recipe to the database
        recipeRepository.save(recipeEntity);
    }

    /**
     * This method is to create new recipe
     *
     * @param recipeModel Recipe model
     * @return recipe Id
     */
    public Long addRecipe(Recipe recipeModel) {

        var recipeEntity = new RecipeEntity();
        //Map Recipe Entity to Recipe Model
        recipeMapper.modelToEntity(recipeModel, recipeEntity);
        //Save recipe to the database
        var entity = recipeRepository.save(recipeEntity);
        return entity.getId();
    }

    /**
     * This method is to delete the recipe details of the given recipe id
     *
     * @param recipeId - Recipe ID
     */
    public void deleteRecipe(Long recipeId) {
        //If no recipe found with the given recipe id then exception is caught in the global exception handler
        recipeRepository.deleteById(recipeId);
    }
}
