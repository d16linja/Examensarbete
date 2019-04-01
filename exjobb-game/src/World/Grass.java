package World;

import java.awt.*;
import System.Resources;

public class Grass extends WorldObject {

	public Grass(int x, int y) {
		super(x, y);
	}
	
	// Sets the image
	protected void setImage(Graphics2D img) {
		img.setComposite(AlphaComposite.Src);
		img.drawImage(Resources.grass,((int) (Math.random()*4))*-16, ((int) (Math.random()*2))*-16, null);
	}
	
	// Makes the object solid
	protected boolean setSolid() {
		return true;
	}

	@Override
	public void update() {

	}

}
