package com.recipe.recipemanagement.repository;

import com.recipe.recipemanagement.entity.RecipeEntity;
import com.recipe.recipemanagement.model.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShubhamKumar
 * This class is to build Request specification with multiple query parameters
 */
public class RecipeSpecificationBuilder {

    private final List<SearchCriteria> params;

    public RecipeSpecificationBuilder(List<SearchCriteria> searchParams) {
        params =searchParams;
    }

    public Specification<RecipeEntity> build() {
        if (params == null || params.size() == 0) {
            return null;
        }
        //Search query parameters can be multiple, so create list of RecipeSpecifications
        List<RecipeSpecification> specs = params.stream().map(RecipeSpecification::new).collect(Collectors.toList());
        //start where clause with null value
        Specification<RecipeEntity> spec = Specification.where(null);
        for (RecipeSpecification recipeSpecification : specs) {
            //Use 'and' to append to the where clause
            spec = spec.and(recipeSpecification);
        }
        return spec;
    }
}
