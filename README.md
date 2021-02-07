# Lunch Microservice

The service provides an endpoint that will determine, from a set of recipes, what I can have for lunch at a given date, based on my fridge ingredient's expiry date, so that I can quickly decide what I’ll be having to eat, and the ingredients required to prepare the meal.

## Prerequisites

* [Java 11 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)

*Note: Docker is used for the local MySQL database instance, feel free to use your own instance or any other SQL database and insert data from lunch-data.sql script* 


### Run

1. Start database:

    ```
    docker-compose up -d
    ```
   
2. Add test data from  `sql/lunch-data.sql` to the database. Here's a helper script if you prefer:


    ```
    CONTAINER_ID=$(docker inspect --format="{{.Id}}" lunch-db)
    ```
    
    ```
    docker cp sql/lunch-data.sql $CONTAINER_ID:/lunch-data.sql
    ```
    
    ```
    docker exec $CONTAINER_ID /bin/sh -c 'mysql -u root -prezdytechtask lunch </lunch-data.sql'
    ```
    
3. Run Springboot LunchApplication

Task 1: Given that I am a consumer of the lunch API and have made a GET request to the /lunch endpoint with a given date then I should receive a JSON response of the recipes that I can prepare based on the availability of the ingredients in my fridge.
URL : http://localhost:8080/lunch/?date=dateString
Example : http://localhost:8080/lunch/?date=2020-01-01


Task 2 : Given that I am a user of the Lunch API and I have made a GET request to the /lunch endpoint and an ingredient is past its useBy date according to the date parameter then I should not recieve any recipes containing this ingredient
URL : http://localhost:8080/lunch/?date=dateString
Example : http://localhost:8080/lunch/?date=2020-01-01

Task 3: GIVEN that I am a consumer of The Lunch API AND I haae made a GET request of teh /lunch  endpoint AND an angredient is past its bestBefore date according to the date parameter and is still within its useBy date then any recipe containing this ingredient should be sorted to the bottom of the JSON response onject.
URL: http://localhost:8080/lunch/?date=dateString
Example: http://localhost:8080/lunch/?date=2020-01-01

Task 4: Given that I am consumer of the lunch API, I want to look up a recipe by its title. Create a new rest endpoint to lookup a recipe by its title AND return HTTP 404 status if the requested recipe cannot be found.
URL: http://localhost:8080/lunch/recipe/recipeTitle
Example1 : http://localhost:8080/lunch/recipe/Ham and Cheese Toastie
Example2 :http://localhost:8080/lunch/recipe/My recipe => returns 404 page

Task 5: Given that I am a consumer of the lunch API, I want to exclude recipes by a given set of ingredients. Create a new REST endpoiint to filter recipes provided ingredients
URL : http://localhost:8080/lunch?exclIngredient=Mushrooms&exclIngredient=Ingredient1&exclIngredient=Ingredient2
Example : http://localhost:8080/lunch?exclIngredient=Mushrooms&exclIngredient=Ketchup&exclIngredient=Bread => returns recipes NOT CONTAINING Mushrooms, Ketchup and Bread 


