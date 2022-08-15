package com.recipe.recipemanagement.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author ShubhamKumar
 * Entity class to store Recipe and Ingredient mapping
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipe_ingredient")
public class RecipeIngredientEntity {

    @EmbeddedId
    private RecipeIngredientId recipeIngredientId;
    private Double quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientEntity that = (RecipeIngredientEntity) o;
        return Objects.equals(recipeIngredientId, that.recipeIngredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeIngredientId, quantity);
    }
}