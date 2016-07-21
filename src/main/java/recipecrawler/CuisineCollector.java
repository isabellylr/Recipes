package recipecrawler;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

public class CuisineCollector {

	public static void main(String[] args) {
		try {
	        URL website = new URL("http://www.taste.com.au/recipes/collections/groups/recipes+by+cuisine");
	        URLConnection connection = website.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        Writer writer = new FileWriter("cuisines.txt");
	        
	        String line = in.readLine();
	        while (!line.contains("recipe-1col")) line = in.readLine();
	        while (!(line = in.readLine()).trim().contains("recipe-1col")) {
	        	if (line.contains("<div class=\"heading\">")) {
	        		String[] lineSplit = line.substring(line.indexOf("http"), line.indexOf("</a>")).split("\">");
	        		writer.write(lineSplit[1].split(" ")[0] + ": " + lineSplit[0] + "\n");
	        	}
	        }
	        in.close();
	        writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
