package problem.sprites;

import java.awt.Dimension;
import java.awt.Shape;
import java.util.ArrayList;

public interface ISprite {
	public void move(Dimension space);
	public ArrayList<Shape> getShape();
	public default ISprite getChild(int i){
		throw new UnsupportedOperationException();
	}
	default void addSprite(ISprite spriteToAdd){
		throw new UnsupportedOperationException();
	}
	default void removeSprite(ISprite spriteToRemove){
		throw new UnsupportedOperationException();
	}
}
