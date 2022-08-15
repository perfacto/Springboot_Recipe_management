package com.recipe.recipemanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.recipe.recipemanagement.enums.ItemType;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ShubhamKumar
 * Model class for Recipe
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recipe {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
    private LocalDateTime createdOn;
    private ItemType itemType;
    private Integer serving;
    private String cookingInstructions;
    @NotEmpty(message = "Ingredients are mandatory")
    private List<Ingredient> ingredients;
}
