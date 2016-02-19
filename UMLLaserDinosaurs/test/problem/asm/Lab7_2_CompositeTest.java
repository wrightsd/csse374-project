package problem.asm;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import problem_asm.DesignParser;

public class Lab7_2_CompositeTest {
	String contents;

	@Before
	public void setup() throws IOException {
		String[] arguments = { "problem.sprites.ISprite", "problem.client.AnimatorApp",
				"problem.graphics.AnimationPanel", "problem.graphics.MainWindow", "problem.sprites.AbstractSprite",
				"problem.sprites.CircleSprite", "problem.sprites.CompositeSprite",
				"problem.sprites.GuardedSnowmanSprite", "problem.sprites.RectangleSprite",
				"problem.sprites.SnowmanSprite", "problem.sprites.SpriteFactory" };
		DesignParser.parse(arguments, "./test/problem/asm/Lab7_2_test.txt", "uml");
		FileInputStream file = new FileInputStream("./test/problem/asm/Lab7_2_test.txt");
		int i = file.read();
		while (i != -1) {
			contents += (char) i;
			i = file.read();
		}
		file.close();
	}
	
	@Test
	public void testCompositeComponentLeafAppear() {
		assertTrue(contents.contains("composite"));
		assertTrue(contents.contains("composite component"));
		assertTrue(contents.contains("leaf"));
		assertTrue(contents.contains("yellow"));
	}
	
	@Test
	public void testCorrectNumberCompositesAppear() {
		assertEquals(7, numOccurencesInString("fillcolor =yellow1", contents));
		assertEquals(3, numOccurencesInString("\\<\\<composite\\", contents));
		assertEquals(1, numOccurencesInString("\\<\\<composite component", contents));
		assertEquals(3, numOccurencesInString("\\<\\<leaf", contents));

	}
	
	private int numOccurencesInString(String sub, String full) {
		int num = 0;
		int lastIndex = 0;
		int currentSpot = 0;
		while (lastIndex != -1) {
			lastIndex = full.substring(currentSpot).indexOf(sub);

			if (lastIndex != -1) {
				num++;
				currentSpot += lastIndex + sub.length();
			}
		}
		return num;
	}

}
