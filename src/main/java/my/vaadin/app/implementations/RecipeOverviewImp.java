package my.vaadin.app.implementations;

import java.util.List;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import my.vaadin.app.RecipeOverview;
import my.vaadin.app.RecipeView;
import my.vaadin.app.util.Queries;
import my.vaadin.app.util.Util;

@SuppressWarnings("serial")
public class RecipeOverviewImp extends RecipeOverview {

	public RecipeOverviewImp(String id,
							String imageURL,
							String name, 
							String prepTime, 
							String cookTime,
							String calories) {

		Image image = new Image();
		image.setSource(new ExternalResource(imageURL)); 
		image.setHeight(this.imageLayout.getHeight()+"px");
		image.setWidth(this.imageLayout.getWidth()+"px");
		this.absoluteLayout.addComponent(image);
		this.recipeName.setValue(name);
		this.cookTime.setIcon(new ThemeResource("icons/cook_time.png"));
		this.prepTime.setCaption(prepTime.contains("-")?prepTime.substring(0,prepTime.indexOf("-")):prepTime);
		this.prepTime.setIcon(new ThemeResource("icons/prep_time.png"));
		this.cookTime.setCaption(cookTime.contains("-")?cookTime.substring(0,cookTime.indexOf("-")):cookTime);
		this.calories.setCaption(calories);
		this.calories.setIcon(new ThemeResource("icons/cal.png"));
		
		this.absoluteLayout.addLayoutClickListener(new LayoutClickListener() {
            public void layoutClick(final LayoutClickEvent event) {
            	try {
            		Window w = new Window(name);
					RecipeView content = new RecipeViewImp(imageURL,
							name, Util.runQuery(Queries.ingredientsQuery(id)),
							Util.runQuery(Queries.stepsQuery(id)),
							Util.runQuery(Queries.nutritionalQuery(id)));
	            	w.setContent(content);
	            	w.center();
	            	w.setResizable(false);
			        UI.getCurrent().addWindow(w);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
		});
	}
}