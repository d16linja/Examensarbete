 package Agents;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

import System.Resources;
import System.Block;
import World.WorldObject;

public class Player extends AgentObject {
	
	// The booleans makes the object aware of what keys is being hold
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private boolean use = false;
	private boolean flying = true;
	private boolean jumped = false;
	private boolean kicked = false;
	private int kickTimer = 0;
	private int score;
	private int health;
	private int spriteY = 0;
	private int spriteX = 0;
	private int spriteCounter = 0;
	private int animationFrame = 0;


	public Player(int x, int y) {
		super(x, y-1, 32, 16);
		health = 10;
	}

	// Getters and setters for methods unique for the player class
	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public int getScore() {
		return score;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	// Sets the image of the object
	@Override
	protected void setImage(Graphics2D img) {
		img.setComposite(AlphaComposite.Clear);
		img.fillRect(0, 0, getWidth(), getHeight());
		img.setComposite(AlphaComposite.Src);
		img.drawImage(Resources.player, spriteX+animationFrame, spriteY, null);
	}

	@Override
	public void update() {
		flying = true;
		//If we are not at the max speed, we can add more speed
		if (right) {
			xv+=0.1;
		}		
		if (left) {
			xv-=0.1;
		}
		if (xv > 2 || xv < -2) {
			xv*=0.95;
		}

		if (kickTimer > 0) {
			kickTimer++;
			kicked = true;
			if (kickTimer > 50) {
				kickTimer = 0;
			}
		}

		if (use && !kicked) {
			kicked = true;
			kickTimer++;
			yv=-2;
			if (left || spriteX == -32) {
				xv-=3;
				if (xv < -3) xv = -3;
			}
			if (right || spriteX == 0) {
				xv += 3;
				if (xv > 3) xv = 3;
			}
		}

		// There is some resistance so that we stop if we dont add more speed
		if (xv > 0.05) {
			xv-=0.05;
		}else if (xv < -0.05) {
			xv+=0.05;
		}else xv = 0;

		if (yv > 3) yv=3;
		else yv+=0.1;


		x+=xv;
		y+=yv;

		// Handles collision with world objects
		for (WorldObject wo: Block.conWorld()) {
			switch (wCollision(x, y, wo)) {
			case 1:
				if (wo.getSolid()) {
					y=wo.getY()+wo.getSize()+0.1;
					yv=0.1;
				}
				break;
			case 2:
				if (wo.getSolid()) {
					x=wo.getX()-width;
					xv=0;
					if (xv > 0) {
						xv=0;
					}
				}
				break;

			case 3:
				if (wo.getSolid()) {
					y=wo.getY()-height;
					yv=0;
					//if we are colliding with something solid from below, we are not flying
					flying = false;
				}
				break;
			case 4:
				if (wo.getSolid()) {
					x=wo.getX() + wo.getSize();
					if (xv < 0) {
						xv=0;
					}
				}
				break;
			default:
				break;
			}
		} // End of world collision

		//Since we need to be able to edit the list with objects, we can't use another for-each loop
		for (int i = 0; i < Block.conAgents().size(); i++) {
			if (aCollision(Block.conAgents().get(i))) {
				// if we collide with a coin, get some score and remove the coin
				if (Block.conAgents().get(i).getClass() == Coin.class) {
					Block.removeAgent(Block.conAgents().get(i));
					score++;
				}

				//If we collide with a crawler while falling, remove it, there is 10% chance that a heart spawn
				//otherwise take some damage and get some knockback
				else if (Block.conAgents().get(i).getClass() == Crawler.class) {
					if (yv > 0 || kickTimer > 0) {
						if (Math.random() < 0.1) {
							Health h = new Health(0,0);
							h.setX((int)Block.conAgents().get(i).getX());
							h.setY((int)Block.conAgents().get(i).getY());
							Block.getBlocks().get((int)Block.conAgents().get(i).getX()/960).getAgents().add(h);
						}
						Block.removeAgent(Block.conAgents().get(i));
						yv = -1.5;
						y-=2;
					} else if (Block.conAgents().get(i).x + (Block.conAgents().get(i).getWidth() / 2) > x + (width / 2)){
						xv=-2.5;
						yv=-1.5;
						health--;
					} else {
						xv=2.5;
						yv=-1.5;
						health--;
					}
				}
				//When we collide with a heart, we remove the heart and add 1 to the players health
				else if (Block.conAgents().get(i).getClass() == Health.class) {
					Block.removeAgent(Block.conAgents().get(i));
					health++;
				}
			}

		}

		//If the up key is held down, and the character is on the ground we can do a jump
		//The jumped variable is used so that only 1 jump is made per press
		if (up && !flying && !jumped) {
			yv=-4.2;
			jumped = !jumped;
			kickTimer = 0;
		}
		if (!up) {
			jumped = false;
		}

		if (!use) {
			kicked = false;
		}


		animate();
	}
    // Sets the animationFrame depending if we last moved left or right
	// Also have a counter for what frame in the image that should be shown
	// If the character is flying we override the animation with the correct frame
	private void animate() {
		if (xv < 0) {
			spriteX = -32;
			spriteY = -32;
		}
		if (xv > 0) {
			spriteX = 0;
			spriteY = -32;
		}		
		if (xv == 0) {
			spriteY=0;
			if (spriteCounter / 50 < 1) {
				animationFrame = -16;
			} else {
				animationFrame = 0;
			}
			if (spriteCounter > 100) {
				spriteCounter = 0;
			}
		} else {
			if (spriteCounter > 25) {
				animationFrame = -16;
			} else {
				animationFrame = 0;
			}
			if (spriteCounter > 50) {
				spriteCounter = 0;
			}
		}

		spriteCounter++;

		if (flying) {
			spriteY= -64;
			if (kickTimer > 0) {
				animationFrame = -16;
			} else {
				animationFrame = 0;
			}
		}
		setImage((Graphics2D) super.img.getGraphics());
	}
}
