package com.recipe.recipemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


/**
 * @author ShubhamKumar
 * Search criteria
 */
@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {

    @NotEmpty
    private String key;
    @NotEmpty
    private String operation;
    private String value;

    @Override
    public String toString() {
        return key+operation+value;
    }
}
