package problem.asm;

import java.io.IOException;

public class Lab7_2Runner {
	
	public static void main(String[] args) throws IOException {
		String[] arguments = { "problem.sprites.ISprite", "problem.client.AnimatorApp",
				"problem.graphics.AnimationPanel", "problem.graphics.MainWindow", "problem.sprites.AbstractSprite",
				"problem.sprites.CircleSprite", "problem.sprites.CompositeSprite",
				"problem.sprites.GuardedSnowmanSprite", "problem.sprites.RectangleSprite",
				"problem.sprites.SnowmanSprite", "problem.sprites.SpriteFactory" };
		DesignParser.parse(arguments, "./output/Lab7_2.txt", "uml");
	}

}
