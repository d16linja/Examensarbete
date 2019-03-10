package Agents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import System.Resources;

public class Coin extends AgentObject {
	private int spriteCounter = 0;
	private int animationFrame = 0;
	public Coin(int x, int y) {
		super(x, y, 16, 16);
	}
	
	// Sets the image for the object
	@Override
	protected void setImage(Graphics2D img) {
		img.setComposite(AlphaComposite.Clear);
		img.fillRect(0, 0, getWidth(), getHeight());
		img.setComposite(AlphaComposite.Src);
		img.drawImage(Resources.coin, 0+animationFrame, 0, null);
	}
	
	// The coin doesn't do much except having an animation
	@Override
	public void update() {
		animate();
	}
	
	// Changes the animationFrame so that the correct part of the image is shown 
	// and the object is animated and updated with the new image
	private void animate() {
		if (spriteCounter == 0) {
			animationFrame = -0;
		}
		if (spriteCounter == 15) {
			animationFrame = -16;
		}
		if (spriteCounter == 30) {
			animationFrame = -32;
		}
		if (spriteCounter == 45) {
			animationFrame = -48;
		}
		spriteCounter++;
		if (spriteCounter == 60) {
			spriteCounter = 0;
		}
		setImage((Graphics2D) super.img.getGraphics());
	}

}
