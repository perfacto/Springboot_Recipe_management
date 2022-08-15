package com.recipe.recipemanagement.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author ShubhamKumar
 * Entity class to store Recipe details
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_sequence")
    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdOn;

    private String itemType;

    private Integer serving;

    @Column(length = 60000)
    private String cookingInstructions;

    @OneToMany(mappedBy = "recipeIngredientId.recipe", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RecipeIngredientEntity> recipeIngredients;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeEntity that = (RecipeEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(createdOn, that.createdOn) &&
                itemType == that.itemType &&
                Objects.equals(serving, that.serving) &&
                Objects.equals(cookingInstructions, that.cookingInstructions) &&
                Objects.equals(recipeIngredients, that.recipeIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdOn, itemType, serving, cookingInstructions, recipeIngredients);
    }

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }
}