package System;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Agents.Agent;
import Agents.AgentObject;
import Agents.Coin;
import Agents.Player;
import World.World;


public class Main extends JPanel implements Runnable, KeyListener {
	private Graphics2D g2d;
    private Block block;
    private Player player;
    private Agent agent;
    private int coinCount = 0;
    private double cameraPan = 0;
    
    // Creates all the necessary objects for the Game
    public Main() {
    	Resources.loadResources();
        block = new Block();
        player = new Player(0, 0);
        agent = new Agent();


        
    	// Get the players spawn point and remove the object from the list of objects
        // Also count all the coins so that we can compare it to the score later
    	for (int i = 0; i < block.getAgents().size(); i++) {
    		if (block.getAgents().get(i).getClass() == player.getClass()) {
    			player.setX(block.getAgents().get(i).getX());
    			player.setY(block.getAgents().get(i).getY());
				block.getAgents().remove(i);
    		}
    	}
    }
    // Creates the window, keylistener and starts a thread
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Sample Frame");
        Main main = new Main();
        main.setPreferredSize(new Dimension(60*16,60*16));
        frame.setContentPane(main);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setFocusable(true);
        main.addKeyListener(main);
        main.requestFocusInWindow();
        Thread thread = new Thread(main);
        thread.start();
    }
    
    // Prints the graphics to the JPane
    public void paint(Graphics g) {
        super.paint(g);

		if (((player.getX() - cameraPan) < 475) || (player.getX() - cameraPan) > 485) {
			cameraPan += 0.02 * (player.getX() - cameraPan - 480);
		}

        g2d = (Graphics2D) g;
        g2d.setColor(new Color(150,150,255));
        g2d.fillRect(0, 0, 60*16, 60*16);
        
        for (int i = 0; i < block.getWorld().size(); i++) {
        	g2d.drawImage(block.getWorld().get(i).getImg(),(int) (block.getWorld().get(i).getX()-cameraPan), block.getWorld().get(i).getY(), null);
        }
        
        for (int i = 0; i < block.getAgents().size(); i++) {
        	g2d.drawImage(block.getAgents().get(i).getImg(),(int) (block.getAgents().get(i).getX()-cameraPan),(int) block.getAgents().get(i).getY(), null);
        }
        
        for (int i = 0; i < player.getHealth(); i++) {
        	g2d.drawImage(Resources.heartBig, 20+40*i,20,null);
        }
        
//        // If we win, we show a winning screen
//        if (player.getScore() == coinCount) {
//        	g2d.setColor(Color.GREEN);
//        	g2d.setFont(new Font("Monospaced", Font.BOLD, 60));
//        	g2d.drawString("You win!", 100, 100);
//        	g2d.setFont(new Font("Monospaced", Font.BOLD, 30));
//        	g2d.drawString("Press ESC to exit", 100, 150);
//        }
//
//        // if we loose we remove the player from the game and show a message that we lost
//        if (player.getHealth() <= 0) {
//        	g2d.setColor(Color.RED);
//        	g2d.setFont(new Font("Monospaced", Font.BOLD, 60));
//        	g2d.drawString("You died!", 100, 100);
//        	g2d.setFont(new Font("Monospaced", Font.BOLD, 30));
//        	g2d.drawString("Press ESC to exit", 100, 150);
//        }
        
        g2d.drawImage(player.getImg(),(int) (player.getX()-cameraPan),(int) player.getY(), null);


        
    }
    
    // Runs the game loop
	public void run() {
        while (true) {
            try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            Block.setContext((int) player.getX() / 960);
			System.out.println(Block.getContext());
            for (AgentObject a : Agent.getAgentList()) {
            	a.update();
            }
            player.update();
            if(player.getHealth()<=0) {
            	player.setX(-100);
            }
            repaint();
        }		
	}

	// Sets the players booleans to true if the correct key is pressed
	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
			player.setLeft(true);
		}
		
		if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
			player.setRight(true);
		}
		
		if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
			player.setUp(true);
		}
		
		if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
			player.setDown(true);
		}
		
		if (e.getKeyCode() == 32) {
			player.setDown(true);
		}
		
		if (e.getKeyCode() == 27) {
			System.exit(0);
		}


	}

	// Sets the players booleans to false if the key is released
	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
			player.setLeft(false);
		}
		
		if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
			player.setRight(false);
		}
		
		if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
			player.setUp(false);
		}
		
		if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
			player.setDown(false);
		}
		
		if (e.getKeyCode() == 32) {
			player.setDown(false);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}