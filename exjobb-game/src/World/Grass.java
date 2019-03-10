package World;

import java.awt.Color;
import java.awt.Graphics2D;

public class Grass extends WorldObject {

	public Grass(int x, int y) {
		super(x, y);
	}
	
	// Sets the image
	protected void setImage(Graphics2D img) {
		img.setColor(new Color(0,50,0));
		img.fillRect(0, 0, size, size);
	}
	
	// Makes the object solid
	protected boolean setSolid() {
		return true;
	}

}
