package problem.asm;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class PizzaFactoryTest {

	public String contents;

	@Before
	public void testOfPizzaFactory() throws IOException {
		String[] arguments = { "factory.pizzaaf.PizzaIngredientFactory", "factory.pizzaaf.NYPizzaIngredientFactory",
				"factory.pizzaaf.ChicagoPizzaIngredientFactory", "factory.pizzaaf.NYPizzaStore",
				"factory.pizzaaf.Dough", "factory.pizzaaf.Sauce", "factory.pizzaaf.Cheese", "factory.pizzaaf.Clams",
				"factory.pizzaaf.ThinCrustDough", "factory.pizzaaf.ThickCrustDough", "factory.pizzaaf.PlumTomatoSauce",
				"factory.pizzaaf.MarinaraSauce", "factory.pizzaaf.MozzarellaCheese", "factory.pizzaaf.ReggianoCheese",
				"factory.pizzaaf.FrozenClams", "factory.pizzaaf.FreshClams" };
		DesignParser.parse(arguments, "./test/problem/asm/pizzaaf_output.txt", "uml");

		FileInputStream file = new FileInputStream("./test/problem/asm/pizzaaf_output.txt");
		int i = file.read();
		while (i != -1) {
			contents += (char) i;
			i = file.read();
		}
	}

	@Test
	public void testFactoryOutput() {
		assertTrue(contents.contains("{\\<\\<abstract\\>\\>\\n\\<\\<interface\\>\\>\\nPizzaIngredientFactory|"));
		assertTrue(contents.contains("NYPizzaIngredientFactory->ThinCrustDough[arrowhead=\"ovee\", style=\"dashed\"];"));
		assertTrue(contents.contains("NYPizzaIngredientFactory->MarinaraSauce[arrowhead=\"ovee\", style=\"dashed\"];"));
		assertTrue(contents.contains("NYPizzaIngredientFactory->ReggianoCheese[arrowhead=\"ovee\", style=\"dashed\"];"));
		assertTrue(contents.contains("NYPizzaIngredientFactory->FreshClams[arrowhead=\"ovee\", style=\"dashed\"];"));
		assertTrue(contents.contains("ChicagoPizzaIngredientFactory->ThickCrustDough[arrowhead=\"ovee\", style=\"dashed\"];"));
		assertTrue(contents.contains("ChicagoPizzaIngredientFactory->PlumTomatoSauce[arrowhead=\"ovee\", style=\"dashed\"];"));
		assertTrue(contents.contains("ChicagoPizzaIngredientFactory->MozzarellaCheese[arrowhead=\"ovee\", style=\"dashed\"];"));
		assertTrue(contents.contains("ChicagoPizzaIngredientFactory->FrozenClams[arrowhead=\"ovee\", style=\"dashed\"];"));
		assertTrue(contents.contains("NYPizzaStore->NYPizzaIngredientFactory[arrowhead=\"ovee\", style=\"dashed\"];"));
	}

}
