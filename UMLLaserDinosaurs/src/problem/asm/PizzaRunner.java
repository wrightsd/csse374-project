package problem.asm;

import java.io.IOException;

public class PizzaRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = { "factory.pizzaaf.PizzaIngredientFactory", "factory.pizzaaf.NYPizzaIngredientFactory",
				"factory.pizzaaf.ChicagoPizzaIngredientFactory", "factory.pizzaaf.NYPizzaStore",
				"factory.pizzaaf.Dough", "factory.pizzaaf.Sauce", "factory.pizzaaf.Cheese", "factory.pizzaaf.Clams",
				"factory.pizzaaf.ThinCrustDough", "factory.pizzaaf.ThickCrustDough", "factory.pizzaaf.PlumTomatoSauce",
				"factory.pizzaaf.MarinaraSauce", "factory.pizzaaf.MozzarellaCheese", "factory.pizzaaf.ReggianoCheese",
				"factory.pizzaaf.FrozenClams", "factory.pizzaaf.FreshClams" };
		DesignParser.parse(arguments, "./output/pizzaaf_output.txt");
	}

}
