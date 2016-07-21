package recipecrawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

public class RecipeCollector {
	private static BufferedReader reader;
	private static int id = 5899;
	
    public static void main(String[] args) throws Exception {
    	reader = new BufferedReader(new FileReader("cuisines.txt"));
    	Writer recipeWriter = new FileWriter("recipes.txt", true);
    	String cuisineLine;
    	while ((cuisineLine = reader.readLine()) != null) {
    		if (!cuisineLine.startsWith("#")) {
    			String[] cuisineLike = cuisineLine.split(": ");
    			String cuisine = cuisineLike[0];
    			String url = cuisineLike[1];
    			int numOfPages = getNumberOfPages(url+"/1");
    			for (int i = 1; i <= numOfPages; i++) {
    				URL website = new URL(url+"/"+i);
    		        URLConnection connection = website.openConnection();
    		        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    		        String line = in.readLine();
    		        while (!line.contains("recipe-1col")) line = in.readLine();
    		        while (!(line = in.readLine()).trim().contains("recipe-1col")) {
    		        	if (line.trim().startsWith("<a href=\"http")) {
    		        		try {
    		        			String[] lineSplit = line.substring(line.indexOf("http"), line.indexOf("</a>")).split("\">");
        		        		String recipeName = lineSplit[1];
        		        		String repipeInfo = getRecipeInfo(lineSplit[0]);
        		        		recipeWriter.write(id++ + "; " + cuisine + "; " + recipeName + "; " + repipeInfo + "\n");
        		        		System.out.println(id + "; " + cuisine + "; " + recipeName + "; " + repipeInfo);	
    		        		} catch (Exception e) {
    		        			e.printStackTrace();
    		        		}
    		        	}
    		        }
    		        in.close();
    			}
    		}
    	}
    	recipeWriter.close();
    }
	
	private static int getNumberOfPages(String url) throws Exception {
		URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = in.readLine();
        while (!line.contains("content-item pages")) line = in.readLine();
        line = in.readLine();
        return Integer.valueOf(line.substring(line.indexOf("of")+2, line.indexOf("</div>")).trim());
	}
	
	private static String getRecipeInfo(String url) throws Exception {
		String prepTime = "";
		String cookTime = "";
		String ingredients = "";
		String difficulty = "";
		String servings = "";
		String energy = "";
		String fatSaturated = "";
		String fatTotal = "";
		String carbSugars = "";
		String carbTotal = "";
		String dietaryFibre = "";
		String protein = "";
		String cholesterol = "";
		String sodium = "";
		String imageURL = "";
		
		URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
        	if (line.contains("recipe-image-wrapper")) {
        		while (!(line = in.readLine()).contains("src"));
        		imageURL = line.substring(line.indexOf("=")+2, line.length()-1);
        	} else if (line.contains("TO PREP")) {
        		prepTime = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("TO COOK")) {
        		cookTime = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("INGREDIENTS")) {
        		ingredients = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("DIFFICULTY")) {
        		difficulty = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("SERVINGS")) {
        		servings = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Step")) {
        		FileWriter stepsWriter = new FileWriter("steps.txt", true);
        		stepsWriter.write(id + ";" + (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</")).replaceAll(";", ",")+"\n");
        		stepsWriter.close();
        	} else if (line.contains("label for=\"ingredient")) {
        		FileWriter ingredientWriter = new FileWriter("ingredients.txt", true);
        		ingredientWriter.write(id + ";" + line.substring(line.indexOf(">")+1, line.indexOf("</")).replaceAll(";", ",")+"\n");
        		ingredientWriter.close();
        	} else if (line.contains("Energy")) {
        		energy = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Fat Saturated")) {
        		fatSaturated = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Fat Total")) {
        		fatTotal = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Carb. Sugars")) {
        		carbSugars = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Carb. Total")) {
        		carbTotal = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Dietary Fibre")) {
        		dietaryFibre = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Protein")) {
        		protein = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Cholesterol")) {
        		cholesterol = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} else if (line.contains("Sodium")) {
        		sodium = (line = in.readLine()).substring(line.indexOf(">")+1, line.indexOf("</"));
        	} 
        }
        return (prepTime + "; " + cookTime  + "; " +  ingredients + "; " + difficulty + 
        		"; " + servings  + "; " + energy  + "; " + fatSaturated +
        		"; " + fatTotal  + "; " + carbSugars  + "; " + carbTotal + "; " + dietaryFibre  + 
        		"; " + protein  + "; " + cholesterol  + "; " + sodium + "; " + imageURL);
	}
}
