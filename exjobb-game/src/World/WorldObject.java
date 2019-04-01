package World;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import System.Block;


// The abstract class for all world objects
public abstract class WorldObject {
	private int x;
	private int y;
	protected int size = 16;
	private BufferedImage img;
	private boolean solid;
	
	// Sets the X and Y to * 16 since the loops in World only is 60*60, but the
	// game area is set to 60*16^2
	public WorldObject(int x, int y) {
		this.x = x*16+(Block.getBlocks().size())*960;
		this.y = y*16;
		img = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
		setImage((Graphics2D) img.getGraphics());
		solid = setSolid();
	}
	
	// Abstract methods so that we can call them in a loop from the list
	abstract protected void setImage(Graphics2D img);
	
	// I thought that i might add something not solid later like water or lava
	// so we need to force all objects to specify if they are solid or not
	abstract protected boolean setSolid();
	
	// Getters and setters for the variables
	public boolean getSolid() {
		return solid;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getSize() {
		return size;
	}
	
	public BufferedImage getImg() {
		return img;
	}

	abstract public void update();

}
