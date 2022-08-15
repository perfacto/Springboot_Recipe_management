package com.recipe.recipemanagement.repository;

import com.recipe.recipemanagement.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ShubhamKumar
 * Repository class to interact with the Ingredient table
 */
@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
}
