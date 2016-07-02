package my.vaadin.app;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Image;

@SuppressWarnings("serial")
public class RecipeOverviewImp extends RecipeOverview {
	
	public RecipeOverviewImp(String imageURL,
							String name, 
							String prepTime, 
							String cookTime,
							String servings) {
		Image image = new Image();
		image.setSource(new ExternalResource(imageURL)); 
		image.setHeight(this.imageLayout.getHeight()+"px");
		image.setWidth(this.imageLayout.getWidth()+"px");
		this.absoluteLayout.addComponent(image);
		
		this.recipeName.setValue(name);
		this.prepTime.setCaption(prepTime.contains("-")?prepTime.substring(0,prepTime.indexOf("-")):prepTime);
		this.cookTime.setCaption(cookTime.contains("-")?cookTime.substring(0,cookTime.indexOf("-")):cookTime);
		this.servings.setCaption(servings);
	}

}
