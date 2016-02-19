package sprites;

import java.util.ArrayList;

public class SnowmanSprite extends CompositeSprite {
	
	public SnowmanSprite(double x, double y, double width, double height){
		double baseHeight = height * (3.0/6.0);
		double midHeight = height * (2.0/6.0);
		double topHeight = height * (1.0/6.0);
				
		double baseWidth = width;
		double midWidth = width * (4.5/6.0);
		double topWidth = width * (3.0/6.0);
		
		this.spriteList = new ArrayList<ISprite>();
		this.spriteList.add(new CircleSprite(x, y, baseWidth, baseHeight));
		this.spriteList.add(new CircleSprite(x, y+baseHeight, midWidth, midHeight));
		this.spriteList.add(new CircleSprite(x, y + baseHeight + midHeight, topWidth, topHeight));
	}
	
	

}
