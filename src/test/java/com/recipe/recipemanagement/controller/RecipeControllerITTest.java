package com.recipe.recipemanagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.recipemanagement.entity.IngredientEntity;
import com.recipe.recipemanagement.entity.RecipeEntity;
import com.recipe.recipemanagement.entity.RecipeIngredientEntity;
import com.recipe.recipemanagement.entity.RecipeIngredientId;
import com.recipe.recipemanagement.enums.ItemType;
import com.recipe.recipemanagement.mapper.RecipeMapper;
import com.recipe.recipemanagement.model.Ingredient;
import com.recipe.recipemanagement.model.Recipe;
import com.recipe.recipemanagement.repository.IngredientRepository;
import com.recipe.recipemanagement.repository.RecipeRepository;
import com.recipe.recipemanagement.model.SearchCriteria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerITTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @DisplayName("Retrieve recipe with the recipeId")
    void getRecipe() throws Exception {

        //First set up the test data

        //Create ingredient
        IngredientEntity ingredientEntity = getTomatoesIngredientEntity();

        //Create Recipe
        var recipeEntity = new RecipeEntity();
        recipeEntity.setDescription("Pizza");
        recipeEntity.setCookingInstructions("Cooking instructions");
        recipeEntity.setItemType("VEG");
        recipeEntity.setName("Pizza");
        recipeEntity.setServing(3);
        //Convert Ingredient model into IngredientEntity and RecipeIngredientEntity

        var recipeIngredientEntity = new RecipeIngredientEntity();
        var recipeIngredientId = new RecipeIngredientId();
        recipeIngredientId.setRecipe(recipeEntity);

        recipeIngredientId.setIngredient(ingredientEntity);
        recipeIngredientEntity.setQuantity(2.0);
        recipeIngredientEntity.setRecipeIngredientId(recipeIngredientId);

        var recipeIngredientEntityList = List.of(recipeIngredientEntity);

        recipeEntity.setRecipeIngredients(recipeIngredientEntityList);

        var recipeEntityResponse = recipeRepository.save(recipeEntity);

        mvc
                .perform(get("/recipes/{id}", recipeEntityResponse.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(recipeEntity.getName()))
                .andExpect(jsonPath("$.cookingInstructions").value(recipeEntity.getCookingInstructions()))
                .andExpect(jsonPath("$.serving").value(recipeEntity.getServing()))
                .andExpect(jsonPath("$.itemType").value(recipeEntity.getItemType()))
                .andExpect(jsonPath("$.ingredients[0].name").value(recipeEntity.getRecipeIngredients().get(0).getRecipeIngredientId().getIngredient().getName()));

        //Clean up the entities created during the testing
        recipeRepository.deleteById(recipeEntityResponse.getId());
        ingredientRepository.deleteById(ingredientEntity.getId());
    }

    @Test
    @DisplayName("Search recipes with the name")
    void searchRecipes() throws Exception {

        //First set up the test data

        //Create ingredients
        var ingredientTomatoes = getTomatoesIngredientEntity();
        var ingredientSalt = getSaltIngredientEntity();

        //Create Recipe
        var recipeEntityPizza = new RecipeEntity();
        recipeEntityPizza.setDescription("Pizza");
        recipeEntityPizza.setCookingInstructions("Cooking instructions");
        recipeEntityPizza.setItemType("VEG");
        recipeEntityPizza.setName("Pizza");
        recipeEntityPizza.setServing(3);

        var recipeIngredientTomatoes = new RecipeIngredientEntity();
        var recipeIngredientIdTomatoes = new RecipeIngredientId();
        recipeIngredientIdTomatoes.setRecipe(recipeEntityPizza);

        recipeIngredientIdTomatoes.setIngredient(ingredientTomatoes);
        recipeIngredientTomatoes.setQuantity(2.0);
        recipeIngredientTomatoes.setRecipeIngredientId(recipeIngredientIdTomatoes);

        var recipeIngredientSalt = new RecipeIngredientEntity();
        var recipeIngredientIdSalt = new RecipeIngredientId();
        recipeIngredientIdSalt.setRecipe(recipeEntityPizza);

        recipeIngredientIdSalt.setIngredient(ingredientSalt);
        recipeIngredientSalt.setQuantity(2.0);
        recipeIngredientSalt.setRecipeIngredientId(recipeIngredientIdSalt);

        var recipeIngredientEntityList = List.of(recipeIngredientTomatoes,recipeIngredientSalt);

        recipeEntityPizza.setRecipeIngredients(recipeIngredientEntityList);

        var recipeEntityResponse = recipeRepository.save(recipeEntityPizza);

        var searchCriteria = new SearchCriteria("name", "=", "Pizza");
        var searchCriteriaList = List.of(searchCriteria);
        var objectMapper = new ObjectMapper();
        var searchCriteriaAsString = objectMapper.writeValueAsString(searchCriteriaList);

        mvc
                .perform(post("/recipes/search")
                        .content(searchCriteriaAsString)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].name").value(recipeEntityPizza.getName()));

        //Clean up the entities created during the testing
        recipeRepository.deleteById(recipeEntityResponse.getId());
        ingredientRepository.deleteById(ingredientSalt.getId());
        ingredientRepository.deleteById(ingredientTomatoes.getId());

    }


    @Test
    @DisplayName("Add recipe")
    void addRecipe() throws Exception {

        var recipe = new Recipe();
        recipe.setName("Pizza");
        recipe.setDescription("Chicken Pizza");
        recipe.setCookingInstructions("Pizza cooking instructions");
        recipe.setServing(3);
        recipe.setItemType(ItemType.NON_VEG);

        //Create ingredient
        var ingredientEntity = getTomatoesIngredientEntity();

        var ingredient = new Ingredient();
        ingredient.setName(ingredientEntity.getName());
        ingredient.setDescription(ingredientEntity.getName());
        ingredient.setQuantity(3.0);
        ingredient.setId(ingredientEntity.getId());
        var ingredients = List.of(ingredient);
        recipe.setIngredients(ingredients);

        var objectMapper = new ObjectMapper();
        var recipeAsString = objectMapper.writeValueAsString(recipe);

        var id = mvc
                .perform(post("/recipes")
                        .content(recipeAsString)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Clean up the entities created during the testing
        recipeRepository.deleteById(Long.valueOf(id));
        ingredientRepository.deleteById(ingredientEntity.getId());
    }

    //@Test
    @DisplayName("Update recipe")
    void updateRecipe() throws Exception {

        //First set up the test data

        //Create ingredient
        var ingredientEntity = getTomatoesIngredientEntity();;

        //Create Recipe
        var recipeEntity = new RecipeEntity();
        recipeEntity.setDescription("Pizza");
        recipeEntity.setCookingInstructions("Cooking instructions");
        recipeEntity.setItemType("VEG");
        recipeEntity.setName("Pizza");
        recipeEntity.setServing(3);
        //Convert Ingredient model into IngredientEntity and RecipeIngredientEntity

        var recipeIngredientEntity = new RecipeIngredientEntity();
        var recipeIngredientId = new RecipeIngredientId();
        recipeIngredientId.setRecipe(recipeEntity);

        recipeIngredientId.setIngredient(ingredientEntity);
        recipeIngredientEntity.setQuantity(2.0);
        recipeIngredientEntity.setRecipeIngredientId(recipeIngredientId);

        var recipeIngredientEntityList = List.of(recipeIngredientEntity);

        recipeEntity.setRecipeIngredients(recipeIngredientEntityList);

        var recipeEntityResponse = recipeRepository.save(recipeEntity);

        var recipeMapper = new RecipeMapper();
        var recipe = recipeMapper.entityToModel(recipeEntityResponse);
        recipe.setCreatedOn(null);
        recipe.setCookingInstructions("New cooking instructions");

        var objectMapper = new ObjectMapper();
        var recipeAsString = objectMapper.writeValueAsString(recipe);

        mvc
                .perform(put("/recipes/{id}", recipeEntityResponse.getId())
                        .content(recipeAsString)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        Assertions.assertEquals(recipeRepository.findById(recipeEntityResponse.getId()).get().getCookingInstructions(), recipe.getCookingInstructions());


        //Clean up the entities created during the testing
        recipeRepository.deleteById(recipeEntityResponse.getId());
        ingredientRepository.deleteById(ingredientEntity.getId());
    }

    @Test
    @DisplayName("Delete recipe with the recipeId")
    void deleteRecipe() throws Exception {

        //First set up the test data

        //Create ingredient
        var ingredientEntity = getTomatoesIngredientEntity();;

        //Create Recipe
        var recipeEntity = new RecipeEntity();
        recipeEntity.setDescription("Pizza");
        recipeEntity.setCookingInstructions("Cooking instructions");
        recipeEntity.setItemType("VEG");
        recipeEntity.setName("Pizza");
        recipeEntity.setServing(3);
        //Convert Ingredient model into IngredientEntity and RecipeIngredientEntity

        var recipeIngredientEntity = new RecipeIngredientEntity();
        var recipeIngredientId = new RecipeIngredientId();
        recipeIngredientId.setRecipe(recipeEntity);

        recipeIngredientId.setIngredient(ingredientEntity);
        recipeIngredientEntity.setQuantity(2.0);
        recipeIngredientEntity.setRecipeIngredientId(recipeIngredientId);

        var recipeIngredientEntityList = List.of(recipeIngredientEntity);

        recipeEntity.setRecipeIngredients(recipeIngredientEntityList);

        var recipeEntityResponse = recipeRepository.save(recipeEntity);

        mvc
                .perform(delete("/recipes/{id}", recipeEntityResponse.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        Assertions.assertFalse(recipeRepository.findById(recipeEntityResponse.getId()).isPresent());

        //Clean up the entities created during the testing
        ingredientRepository.deleteById(ingredientEntity.getId());
    }

    private IngredientEntity getTomatoesIngredientEntity() {
        var ingredientEntity = new IngredientEntity();
        ingredientEntity.setName("Tomatoes");
        ingredientEntity.setDescription("Tomatoes");
        ingredientEntity = ingredientRepository.save(ingredientEntity);
        return ingredientEntity;
    }

    private IngredientEntity getSaltIngredientEntity() {
        var ingredientSalt = new IngredientEntity();
        ingredientSalt.setName("Salt");
        ingredientSalt.setDescription("Salt");
        ingredientSalt = ingredientRepository.save(ingredientSalt);
        return ingredientSalt;
    }

}
