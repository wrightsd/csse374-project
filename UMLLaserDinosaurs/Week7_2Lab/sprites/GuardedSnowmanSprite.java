package sprites;

import java.util.ArrayList;

public class GuardedSnowmanSprite extends CompositeSprite {
	
	public GuardedSnowmanSprite(double x, double y, double width, double height){
		double halfHeight = height/3.0;
		double halfWidth = width / 3.0;
				
		this.spriteList = new ArrayList<ISprite>();
		this.spriteList.add(new SnowmanSprite(x,y,width,height));
		this.spriteList.add(new RectangleSprite(x - halfWidth, y - halfHeight, halfWidth, halfHeight));
		this.spriteList.add(new RectangleSprite(x - halfWidth, y + height , halfWidth, halfHeight));
		this.spriteList.add(new RectangleSprite(x + width, y - halfHeight, halfWidth, halfHeight));
		this.spriteList.add(new RectangleSprite(x + width, y + width, halfWidth, halfHeight));
	}

}
