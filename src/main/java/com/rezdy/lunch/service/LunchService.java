package com.rezdy.lunch.service;

import java.time.LocalDate;
import java.util.List;

import com.rezdy.lunch.domain.Recipe;

public interface LunchService {

	public List<Recipe> loadRecipes(LocalDate date);

	public List<Recipe> sortRecipes(List<Recipe> recipes, LocalDate date);

	public List<Recipe> loadRecipesNotUsingIngredients(List<String> excludeIngredients);

	public Recipe loadRecipe(String title);

	public List<Recipe> getNonExpiredRecipesOnDate(LocalDate date);

}
