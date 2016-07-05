package my.vaadin.app.implementations;

import java.util.List;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import my.vaadin.app.RecipeView;

@SuppressWarnings("serial")
public class RecipeViewImp extends RecipeView {
	public RecipeViewImp(String imageURL, 
			String name, 
			List<List<String>> ingredients,
			List<List<String>> steps,
			List<List<String>> nutitionalInfo) {
		Image image = new Image();
		image.setSource(new ExternalResource(imageURL)); 
		image.setHeight(this.image.getHeight()+"px");
		image.setWidth(this.image.getWidth()+"px");
		this.image.addComponent(image);
		this.name.setValue(name);

		for (List<String> ingredient : ingredients) {
			Label l = new Label();
			l.setContentMode(ContentMode.HTML);
			l.setValue(FontAwesome.CIRCLE.getHtml() + "   " +ingredient.get(0));
			l.setHeight("50px");
			this.ingredients.addComponent(l);
		}
		int i = 0;
		for (List<String> step : steps) {
			Label l = new Label();
			l.setIcon(new ThemeResource("icons/"+step.get(1)+".png"));
			l.setContentMode(ContentMode.HTML);
			l.setValue(step.get(0));
			this.steps.addComponent(l);
			i += l.getHeight()+50;
		}
		List<String> n = nutitionalInfo.get(0);
		this.cholesterol.setValue("Cholesterol: " + n.get(0) + "gr");
		this.fibre.setValue("Fibre: " + n.get(2) + "gr");
		this.saturatedfat.setValue("Saturated Fat: " + n.get(3) + "gr");
		this.sodiumm.setValue("Sodiumm: " + n.get(4) + "gr");
		this.sugar.setValue("Sugar: " + n.get(5) + "gr");
		this.totalcarb.setValue("Total Carb: " + n.get(6) + "gr");
		this.totalfat.setValue("Total Fat: " + n.get(7) + "gr");
		this.searvings.setCaption(n.get(1) + "servings");
		this.dificulty.setCaption(n.get(9).substring(86, n.get(9).length()-1));
		this.dificulty.setIcon(new ThemeResource("icons/cook_time.png"));
		
		float size = i>this.ingredients.getHeight()? i:this.ingredients.getHeight();
		this.panel.setHeight(this.panel.getHeight()+size+350+"px");
	}
}
