package com.rezdy.lunch.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rezdy.lunch.domain.Recipe;
import com.rezdy.lunch.service.LunchService;

@Service
public class LunchServiceImpl implements LunchService {

	@Autowired
	private EntityManager entityManager;

	public List<Recipe> getNonExpiredRecipesOnDate(LocalDate date) {
		List<Recipe> recipes = loadRecipes(date);

		return sortRecipes(recipes, date);
	}

	public List<Recipe> loadRecipes(LocalDate date) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

		CriteriaQuery<Recipe> query = criteriaQuery.select(recipeRoot);

		Subquery<Recipe> nonExpiredIngredientSubquery = query.subquery(Recipe.class);
		Root<Recipe> nonExpiredIngredient = nonExpiredIngredientSubquery.from(Recipe.class);
		nonExpiredIngredientSubquery.select(nonExpiredIngredient);

		Predicate matchingRecipe = cb.equal(nonExpiredIngredient.get("title"), recipeRoot.get("title"));
		Predicate expiredIngredient = cb.lessThan(nonExpiredIngredient.join("ingredients").get("useBy"), date);

		Predicate allNonExpiredIngredients = cb
				.not(cb.exists(nonExpiredIngredientSubquery.where(matchingRecipe, expiredIngredient)));
		return entityManager.createQuery(query.where(allNonExpiredIngredients)).getResultList();
	}

	public List<Recipe> sortRecipes(List<Recipe> recipes, LocalDate date) {
		List<Recipe> recipesSorted = new ArrayList<Recipe>();
		recipesSorted.addAll(recipes.stream()
				.filter(recipe -> recipe.getIngredients().stream()
						.allMatch(ingredient -> !ingredient.getBestBefore().isBefore(date)))
				.collect(Collectors.toList()));
		recipesSorted.addAll(recipes.stream()
				.filter(recipe -> recipe.getIngredients().stream()
						.anyMatch(ingredient -> ingredient.getBestBefore().isBefore(date)))
				.collect(Collectors.toList()));

		return recipesSorted;
	}

	public Recipe loadRecipe(String title) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);
		return entityManager
				.createQuery(criteriaQuery.select(recipeRoot).where(cb.equal(recipeRoot.get("title"), title)))
				.getSingleResult();

	}

	public List<Recipe> loadRecipesNotUsingIngredients(List<String> excludeIngredients) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

		CriteriaQuery<Recipe> query = criteriaQuery.select(recipeRoot);

		Subquery<Recipe> exclIngredientsSubQuery = query.subquery(Recipe.class);
		Root<Recipe> exclIngredient = exclIngredientsSubQuery.from(Recipe.class);

		Predicate matchingRecipe = cb.equal(exclIngredient.get("title"), recipeRoot.get("title"));

		return entityManager
				.createQuery(query
						.where(cb.not(cb.exists(exclIngredientsSubQuery.select(exclIngredient).where(matchingRecipe,
								exclIngredient.join("ingredients").get("title").in(excludeIngredients))))))
				.getResultList();

	}

}
