package sprites;

import java.awt.Dimension;
import java.awt.Shape;
import java.util.ArrayList;

public abstract class CompositeSprite implements ISprite {
	
	protected ArrayList<ISprite> spriteList;

	@Override
	public void move(Dimension space) {
		for(int i=0; i < spriteList.size(); i++){
			spriteList.get(i).move(space);
		}
	}

	@Override
	public ArrayList<Shape> getShape() {
		ArrayList<Shape> shapeList = new ArrayList<Shape>();
		for(int i=0; i < spriteList.size(); i++){
			for(int j = 0; j < spriteList.get(i).getShape().size(); j++){
				shapeList.add(spriteList.get(i).getShape().get(j));
			}
		}
		return shapeList;
	}

	@Override
	public ISprite getChild(int i) {
		return spriteList.get(i);
	}

	@Override
	public void addSprite(ISprite spriteToAdd) {
		spriteList.add(spriteToAdd);
	}

	@Override
	public void removeSprite(ISprite spriteToRemove) {
		spriteList.remove(spriteToRemove);
	}

}
