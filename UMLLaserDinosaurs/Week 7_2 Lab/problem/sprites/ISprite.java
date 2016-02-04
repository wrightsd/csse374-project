package problem.sprites;

import java.awt.Dimension;
import java.awt.Shape;
import java.util.ArrayList;

public interface ISprite {
	public void move(Dimension space);
	public ArrayList<Shape> getShape();
	public ISprite getChild(int i);
	void addSprite(ISprite spriteToAdd);
	void removeSprite(ISprite spriteToRemove);
}
