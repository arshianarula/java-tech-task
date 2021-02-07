package com.rezdy.lunch.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rezdy.lunch.domain.Recipe;
import com.rezdy.lunch.service.LunchService;

@RestController
public class LunchController {

	private LunchService lunchService;

	@Autowired
	public LunchController(LunchService lunchService) {
		this.lunchService = lunchService;
	}

	@GetMapping("/lunch")
	public List<Recipe> getRecipes(@RequestParam(value = "date") String date) {
		return lunchService.getNonExpiredRecipesOnDate(LocalDate.parse(date));
	}

	@GetMapping(value = "/lunch/recipe/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Recipe getRecipesByTitle(@PathVariable("title") String title) {
		return lunchService.loadRecipe(title);
	}

	@PostMapping(value = "/lunch")
	public List<Recipe> getRecipes(@RequestParam(name = "exclIngredient") List<String> exclIngredient) {
		return lunchService.loadRecipesNotUsingIngredients(exclIngredient);
	}
}
