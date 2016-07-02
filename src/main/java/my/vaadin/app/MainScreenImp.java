package my.vaadin.app;

import java.util.ArrayList;
import java.util.List;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class MainScreenImp extends MainScreen{
	List<String> includeList = new ArrayList<>();
	List<String> excludeList = new ArrayList<>();
	
	public MainScreenImp() {
		this.include.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				addIncludeAction();
			}
		});
		
		this.exclude.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				addExcludeAction();
			}
		});
		try {
			String sparqlQuery =
	                "PREFIX : <http://www.semanticweb.org/mariëlle/ontologies/2016/5/untitled-ontology-5#> \n" 
	                + "SELECT ?r ?n ?p ?c ?s ?u "
	                + "WHERE {?r a :Recipe ; :name ?n ; :preptime ?p ; :cooktime ?c ; :energykj ?s ; :imageurl ?u ; :originatesFrom ?a. ?a a :American}";

			List<List<String>> result = Util.runQuery(sparqlQuery);
			for (List<String> info : result) {
				RecipeOverviewImp recipeOverview = new RecipeOverviewImp(
						info.get(5),
						info.get(1), info.get(2), info.get(3), Integer.valueOf(info.get(4))+"KJ");
				this.recipesLayout.addComponent(recipeOverview);
			}
			this.recipesLayout.setHeight((result.size()/3)*250+20+"px");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void addIncludeAction() {
		String ingredient = this.includeTextField.getValue();
		if (!ingredient.isEmpty()) {
			Button tag = new Button(ingredient, FontAwesome.CLOSE);
			tag.setStyleName("borderless-colored");
			tag.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					removeIncludeTagAction(tag);
				}
			});
			includeList.add(ingredient);
			this.includeLayout.addComponent(tag);
			this.includeTextField.clear();
		}
	}
	
	private void addExcludeAction() {
		String ingredient = this.excludeTextField.getValue();
		if (!ingredient.isEmpty()) {
			Button tag = new Button(ingredient, FontAwesome.CLOSE);
			tag.setStyleName("borderless-colored");
			tag.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					removeExcludeTagAction(tag);
				}
			});
			excludeList.add(ingredient);
			this.excludeLayout.addComponent(tag);
			this.excludeTextField.clear();
		}
	}
	
	private void removeIncludeTagAction(Button tag) {
		includeList.remove(tag.getCaption());
		this.includeLayout.removeComponent(tag);
	}
	
	private void removeExcludeTagAction(Button tag) {
		excludeList.remove(tag.getCaption());
		this.excludeLayout.removeComponent(tag);
	}
}