package Agents;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

import System.Resources;
import System.Chunk;
import World.WorldObject;

public class Crawler extends AgentObject {
	private double speed = 0.3;
	private int spriteY = 0;
	private int spriteX = 0;
	private int spriteCounter = 0;
	private int animationFrame = 0;
	private int AIview;
	public Crawler(int x, int y) {
		super(x, y, 16, 16);
		xv=speed;
		width = 15;
	}

	@Override
	protected void setImage(Graphics2D img) {
		img.setComposite(AlphaComposite.Clear);
		img.fillRect(0, 0, getWidth(), getHeight());
		img.setComposite(AlphaComposite.Src);
		img.drawImage(Resources.crawler, spriteX+animationFrame, spriteY, null);
	}

	@Override
	public void update() {
		
		yv+=0.1;
		
		x+=xv;
		y+=yv;
		
		//AIview helps the crawler look for collision at the ground in front of him
		if (xv > 0) {
			AIview = 16;
		} else {
			AIview = -16;
		}
		
		// Handles collision with world objects so that the crawler doesn't 
		// fall though the ground
		for (WorldObject wo: Chunk.conWorld()) {
			switch (wCollision(x, y, wo)) {
			case 1:
				break;
			case 2:
				if (wo.getSolid()) {
					x=wo.getX()-width;
					xv=-speed;
				}
				break;

			case 3:
				if (wo.getSolid()) {
					y=wo.getY()-height;
					yv=0;
				}
				break;
			case 4:
				if (wo.getSolid()) {
					x=wo.getX() + wo.getSize();
					xv=speed;
				}
				break;
			default:
				break;
			}
		} // End of world collision
		
		boolean nextTileWalkabel = false;
		
		// Looks at the tile at the ground in front of the crawler 
		for (WorldObject wo: Chunk.conWorld()) {
			if (wCollision(x+AIview, y+16, wo) > 0) {
				nextTileWalkabel = true;
			}
		}
		
		//If we cant walk on the next tile, turn around
		if (!nextTileWalkabel) {
			xv*=-1;
		}
		
		animate();
	}
	
	// Makes the crawler animated
	private void animate() {
		if (spriteCounter > 50) {
			animationFrame = 0;
		} else {
			animationFrame = -16;
		}
		
		if (spriteCounter > 100) {
			spriteCounter = 0;
		}
		
	
		spriteCounter++;
		setImage((Graphics2D) super.img.getGraphics());
	}
}
