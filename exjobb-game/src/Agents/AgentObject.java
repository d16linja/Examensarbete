package Agents;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import World.WorldObject;
import System.Chunk;
// The abstract class for all "agents"

public abstract class AgentObject {
	// A lot of protected variables so that we writing logic in the subclass wont
	// be so complex to read.
	protected double x;
	protected double y;
	protected double xv;
	protected double yv;
	
	protected int height = 16;
	protected int width = 16;
	protected BufferedImage img;

	// We set some values, makes x and y * 16 so that the position is relevant 
	// to the game area where every "block" is 16*16
	public AgentObject(int x, int y, int height, int width) {
		this.x = x*16+(Chunk.getChunks().size())*960;
		this.y = y*16;
		this.height = height;
		this.width = width;
		img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		setImage((Graphics2D) img.getGraphics());
	}
	
	// Getters and setters for variables
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public double getXv() {
		return xv;
	}

	public void setXv(double xv) {
		this.xv = xv;
	}

	public double getYv() {
		return yv;
	}

	public void setYv(double yv) {
		this.yv = yv;
	}
	
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	
	// Methods I want to be able to loop though in the "game loop" and call from outside the subclass
	abstract protected void setImage(Graphics2D img);
	
	abstract public void update();
	
	//Returns 1, if collision from above, 2 from right, 3 from below and 4 from left, 0 if no collision
	//Uses x and y as parameters so that we can also make objects be "aware" of its surroundings if we want
	protected int wCollision(double x, double y, WorldObject wo) {
		
		if ((y < wo.getY()+wo.getSize()) &&
			(y > wo.getY()) &&
			(x > wo.getX()-(wo.getSize()-2)) &&
			(x < wo.getX()+(wo.getSize()-2))) return 1;
		
		if ((y+height > wo.getY()) &&
			(y+height < wo.getY()+wo.getSize()) && 
			(x > wo.getX()-(wo.getSize()-2)) && 
			(x < wo.getX()+(wo.getSize()-2))) return 3;
		
		if ((y < wo.getY()+(wo.getSize()-2)) &&
			(y+height-2 > wo.getY()) && 
			(x+width > wo.getX()) &&
			(x+width < wo.getX()+(wo.getSize()-2))) return 2;
		
		if ((y < wo.getY()+(wo.getSize()-2)) &&
			(y+height-2 > wo.getY()) && 
			(x < wo.getX()+wo.getSize()) && 
			(x+width-2 > wo.getX())) return 4;
		
		return 0;
	}
	
	// a separate collision detection for collision with agents
	protected boolean aCollision(AgentObject ao) {
		if (x+width > ao.getX() &&
			x < ao.getX()+ao.getWidth() &&
			y+height >ao.getY() &&
			y < ao.getY()+ao.getHeight()) {
			return true;
		}
		return false;		
	}
}
