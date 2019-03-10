package World;

import java.awt.Color;
import java.awt.Graphics2D;

public class Stone extends WorldObject {

	public Stone(int x, int y) {
		super(x, y);
	}
	
	// Sets the image for the object
	@Override
	protected void setImage(Graphics2D img) {
		img.setColor(new Color(100,100,100));
		img.fillRect(0, 0, size, size);
		img.setColor(new Color(50,50,50));
		img.drawRect(0, 0, size, size);
		img.drawRect(1, 1, size-2, size-2);
	}
	
	// Makes the object solid
	@Override
	protected boolean setSolid() {
		return true;
	}

}
