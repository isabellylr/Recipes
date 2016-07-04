package my.vaadin.app.util;

import java.util.*;

public class Queries {
	private static List<String> ingredients = new ArrayList<String>(Arrays.asList("Meat", "Grain", "Nut", "Fruit", "Vegetable", "Cheese", "Poultry", "Fish", "Beef", "Pork"));
	
	public static String allRecipesQuery() {
		return "PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#> "
				+ "SELECT ?r  ?n ?p ?c ?s ?u "
				+ "WHERE {?r a :Recipe ; :name ?n ; :preptime ?p ; :cooktime ?c ; :energykj ?s ; :imageurl ?u } ";
	}

	public static String dificultyQuery(String difficulty) {
		return "PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#> "
				+ "SELECT ?r  ?n ?p ?c ?s ?u "
				+ "WHERE {?r a :Recipe ; :name ?n ; :preptime ?p ; :cooktime ?c ; :energykj ?s ; :imageurl ?u ; "
				+ ":hasDifficulty   <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#difficulty/%20" + difficulty+ "> }";
	}
	
	public static String cusineQuery(String cuisine) {
		return String.format("PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#> "
				+ "SELECT ?r ?n ?p ?c ?s ?u "
				+ "WHERE {?r a :Recipe ; :name ?n ; :preptime ?p ; :cooktime ?c ; :energykj ?s ; :imageurl ?u ; :originatesFrom ?a. ?a a :%s}",cuisine);
	}
	
	public static String ingredientQuery(String ingredient) {
		if (ingredients.contains(ingredient)) {
			String.format("PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#> "
					+ "SELECT ?r ?n ?p ?c ?s ?u "
					+ "WHERE {?r a :Recipe ; :name ?n ; :preptime ?p ; :cooktime ?c ; :energykj ?s ; :imageurl ?u ; :contains ?i . ?i a :%s}", ingredient);
		}
		return String.format("PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#> "
				+ "SELECT ?r ?n ?p ?c ?s ?u "
				+ "WHERE {?r a :Recipe ; :name ?n ; :preptime ?p ; :cooktime ?c ; :energykj ?s ; :imageurl ?u ; :contains ?i . ?i a :Ingredient ; :description ?d "
				+ "FILTER(REGEX(?d,'%s'))}", ingredient);
	}
	
	public static String searchQuery(String search) {
		return String.format("PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#> "
				+ "SELECT ?r ?n ?p ?c ?s ?u "
				+ "WHERE {?r a :Recipe ; :name ?n ; :preptime ?p ; :cooktime ?c ; :energykj ?s ; :imageurl ?u FILTER (REGEX(?n, '%s'))}", search);
	}
	
	public static String ingredientsQuery(String recipeId) {
		return String.format("PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#>SELECT ?d "
				+ "WHERE { %s a :Recipe ; :contains ?i . ?i a :Ingredient ; :description ?d}", recipeId);
	}
	
	public static String stepsQuery(String recipeId) {
		return String.format("PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#> "
				+ "SELECT ?i ?n WHERE {?s a :Step ; :describes %s ; :instruction ?i ; :stepnr ?n}", recipeId);
	}
	
	public static String nutritionalQuery(String recipeId) {
		return String.format("PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#>"
				+ "SELECT ?c ?e ?g ?s ?t ?u ?v ?w ?d ?a "
				+ "WHERE { %s a :Recipe ; :cholesterolgr ?c ; :fibregr ?g ; :saturatedfatkj ?s ; :sodiummgr ?t ; :sugargr ?u ; "
				+ ":totalcarbgr ?v ; :totalfatgr ?w ; :servings ?e ; :hasDifficulty ?d ; :originatesFrom ?a}", recipeId);
	}
}
