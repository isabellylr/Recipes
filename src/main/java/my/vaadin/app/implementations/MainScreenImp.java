package my.vaadin.app.implementations;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import my.vaadin.app.MainScreen;
import my.vaadin.app.util.Queries;
import my.vaadin.app.util.Util;

import com.vaadin.ui.CheckBox;

@SuppressWarnings("serial")
public class MainScreenImp extends MainScreen{
	List<List<String>> result;
	List<String> includeList = new ArrayList<String>();
	List<String> excludeList = new ArrayList<String>();
	List<String> difficultiesFilter = new ArrayList<String>(); 
	List<String> cuisinesFitler = new ArrayList<String>(); 

	public MainScreenImp() {
		try {
			addCheckBoxEvent(this.easyCheckBox, this.easyCheckBox.getCaption(), difficultiesFilter);
			addCheckBoxEvent(this.capableCooksCheckBox, this.capableCooksCheckBox.getCaption().replaceAll(" ", "%20"), difficultiesFilter);
			addCheckBoxEvent(this.superEasyCheckBox, this.easyCheckBox.getCaption().replaceAll(" ", "%20"), difficultiesFilter);
			addCheckBoxEvent(this.advancedCheckBox, this.easyCheckBox.getCaption(), difficultiesFilter);
			addCheckBoxEvent(this.challengingCheckBox, this.easyCheckBox.getCaption(), difficultiesFilter);
			addCheckBoxEvent(this.asianCheckBox, this.asianCheckBox.getCaption(), cuisinesFitler);
			addCheckBoxEvent(this.americanCheckBox, this.americanCheckBox.getCaption(), cuisinesFitler);
			addCheckBoxEvent(this.europeanCheckBox, this.europeanCheckBox.getCaption(), cuisinesFitler);

			this.includeButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					addIncludeAction();
				}
			});

			this.excludeButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					addExcludeAction();
				}
			});
			
			this.searchButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					searchAction();
				}
			});
			updateRecipes();
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
					try {
						removeIncludeTagAction(tag);
						updateRecipes();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			includeList.add(ingredient);
			this.includeLayout.addComponent(tag);
			this.includeTextField.clear();
			try {
				updateRecipes();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
					try {
						removeExcludeTagAction(tag);
						updateRecipes();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			excludeList.add(ingredient);
			this.excludeLayout.addComponent(tag);
			this.excludeTextField.clear();
			try {
				updateRecipes();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	
	private void searchAction() {
		String search = this.searchTextField.getValue();
		this.searchTextField.clear();
		if (!search.isEmpty()) {
			try {
				List<List<String>> searchResult = Util.runQuery(Queries.searchQuery(search));
				searchResult.retainAll(result);
				updateRecipesLayout(searchResult);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@SuppressWarnings("deprecation")
	private void addCheckBoxEvent(CheckBox checkBox, String checkBoxName, List<String> filter) {
		checkBox.addListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				try {
					if ((Boolean) event.getProperty().getValue()) {
						filter.add(checkBoxName);
					} else {
						filter.remove(checkBoxName);
					}
					updateRecipes();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void updateRecipes() throws Exception {
		result = Util.runQuery(Queries.allRecipesQuery());
		List<List<String>> cuisines = new ArrayList<List<String>>();
		for (String cuisine : cuisinesFitler) {
			cuisines.addAll(Util.runQuery(Queries.cusineQuery(cuisine)));
		}
		if (!cuisines.isEmpty()) result.retainAll(cuisines);
		List<List<String>> difficulties = new ArrayList<List<String>>();
		for (String difficulty : difficultiesFilter) {
			difficulties.addAll(Util.runQuery(Queries.dificultyQuery(difficulty)));
		}
		if (!difficulties.isEmpty()) result.retainAll(difficulties);
		List<List<String>> includeIngredients = new ArrayList<List<String>>();
		for (String ingredient : includeList) {
			includeIngredients.addAll(Util.runQuery(Queries.ingredientQuery(ingredient)));
		}
		if (!includeIngredients.isEmpty()) result.retainAll(includeIngredients);
		List<List<String>> excludeIngredients = new ArrayList<List<String>>();
		for (String ingredient : excludeList) {
			excludeIngredients.addAll(Util.runQuery(Queries.ingredientQuery(ingredient)));
		}
		result.removeAll(excludeIngredients);
		updateRecipesLayout(result);
	}

	private void updateRecipesLayout(List<List<String>> result) {
		this.recipesLayout.removeAllComponents();
		int i = 0;
		for (List<String> info : result) {
			if (i++ > 100) break;
			RecipeOverviewImp recipeOverview = new RecipeOverviewImp(
					info.get(0),
					info.get(5),
					info.get(1).replaceAll("DQOUTES", "\""), 
					info.get(2), 
					info.get(3), 
					Integer.valueOf(info.get(4))+"KJ");
			this.recipesLayout.addComponent(recipeOverview);
		}
		this.recipesLayout.setHeight((result.size()/3)*250+20+"px");
	}
}