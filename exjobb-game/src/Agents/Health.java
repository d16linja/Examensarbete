package Agents;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

import System.Resources;

public class Health extends AgentObject {

	public Health(int x, int y) {
		super(x, y, 16, 16);
	}
	
	// Sets the image for the health drop
	@Override
	protected void setImage(Graphics2D img) {
		img.setComposite(AlphaComposite.Clear);
		img.fillRect(0, 0, getWidth(), getHeight());
		img.setComposite(AlphaComposite.Src);
		img.drawImage(Resources.heartSmall, 0, 0, null);
	}

	// since the update is abstract and needs to exist its still here, 
	// since the health object does nothing, the update method is empty
	@Override
	public void update() {

	}

}
