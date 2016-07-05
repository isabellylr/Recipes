package my.vaadin.app;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class RecipeView extends VerticalLayout {
	protected CssLayout panel;
	protected AbsoluteLayout layout;
	protected AbsoluteLayout image;
	protected Label name;
	protected CssLayout ingredients;
	protected CssLayout steps;
	protected Label saturatedfat;
	protected Label sodiumm;
	protected Label sugar;
	protected Label totalcarb;
	protected Label totalfat;
	protected Label fibre;
	protected Button dificulty;
	protected Button searvings;
	protected Label cholesterol;

	public RecipeView() {
		Design.read(this);
	}
}
