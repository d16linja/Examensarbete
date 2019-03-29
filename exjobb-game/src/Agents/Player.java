 package Agents;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

import System.Resources;
import World.World;
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
	private int score,health;
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
		if (right && xv < 2) {
			xv+=0.1;
		}		
		if (left && xv > -2) {
			xv-=0.1;
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
		for (WorldObject wo: World.getBlocks()) {
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
		for (int i = 0; i < Agent.agentList.size(); i++) {
			if (aCollision(Agent.agentList.get(i))) {
				// if we collide with a coin, get some score and remove the coin
				if (Agent.agentList.get(i).getClass() == new Coin(0,0).getClass()) {
					Agent.agentList.remove(i);
					score++;
					System.out.println(score);
				}

				//If we collide with a crawler while falling, remove it, there is 10% chance that a heart spawn
				//otherwise take some damage and get some knockback
				else if (Agent.agentList.get(i).getClass() == new Crawler(0,0).getClass()) {
					if (yv > 0) {
						if (Math.random() < 0.1) {
							Agent.agentList.add(new Health((int)Agent.agentList.get(i).getX()/16,(int)Agent.agentList.get(i).getY()/16));
						}
						Agent.agentList.remove(i);
						yv = -1;
					} else if (Agent.agentList.get(i).x + (Agent.agentList.get(i).getWidth() / 2) > x + (width / 2)){
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
				else if (Agent.agentList.get(i).getClass() == new Health(0,0).getClass()) {
					Agent.agentList.remove(i);
					health++;
				}
			}

		}

		//If the up key is held down, and the character is on the ground we can do a jump
		//The jumped variable is used so that only 1 jump is made per press
		if (up && !flying && !jumped) {
			yv=-4;
			jumped = !jumped;
		}
		if (!up) {
			jumped = false;
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
			animationFrame = 0;
		}
		setImage((Graphics2D) super.img.getGraphics());
	}
}
