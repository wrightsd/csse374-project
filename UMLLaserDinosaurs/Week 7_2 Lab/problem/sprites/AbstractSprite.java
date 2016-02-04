package problem.sprites;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public abstract class AbstractSprite implements ISprite {
	protected double dx;
	protected double dy;
	protected Shape shape;

	// Subclasses need to chain this constructor
	public AbstractSprite(double x, double y, double width, double height) {
		this.dx = SpriteFactory.DX;
		this.dy = SpriteFactory.DY;
	}

	// Designed to be used by subclasses
	protected final Rectangle2D computeNewBoundsAfterMoving(Dimension space) {
		Rectangle2D bounds = shape.getBounds2D();

		if (bounds.getX() < 0 || bounds.getX() > space.getWidth())
			dx = -dx;

		if (bounds.getY() < 0 || bounds.getY() > space.getHeight())
			dy = -dy;

		Rectangle2D newBounds = new Rectangle2D.Double(bounds.getX() + dx, bounds.getY() + dy, bounds.getWidth(),
				bounds.getHeight());
		return newBounds;
	}

	@Override
	public final ArrayList<Shape> getShape() {
		ArrayList<Shape> singleSpriteArrayList = new ArrayList<Shape>();
		singleSpriteArrayList.add(this.shape);
		return singleSpriteArrayList;
	}

	@Override
	public abstract void move(Dimension space);

	@Override
	public final void addSprite(ISprite spriteToAdd) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void removeSprite(ISprite spriteToRemove) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final ISprite getChild(int i) {
		throw new UnsupportedOperationException();
	}

}
