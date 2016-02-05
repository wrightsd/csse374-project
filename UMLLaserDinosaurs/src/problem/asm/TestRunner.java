package problem.asm;

import java.io.IOException;

public class TestRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = { "problem.sprites.ISprite", "problem.client.AnimatorApp",
				"problem.graphics.AnimationPanel", "problem.graphics.MainWindow", "problem.sprites.AbstractSprite",
				"problem.sprites.CircleSprite", "problem.sprites.CompositeSprite",
				"problem.sprites.GuardedSnowmanSprite", "problem.sprites.RectangleSprite",
				"problem.sprites.SnowmanSprite", "problem.sprites.SpriteFactory" };
		DesignParser.parse(arguments, "./output/sample_output.txt", "uml");
	}

}
